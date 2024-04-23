package sailpoint.service;

import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import sailpoint.object.Session;
import sailpoint.object.Source;


import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static sailpoint.service.SailPointService.Constants.*;

public class SailPointService {
    private final String url;

    private final String clientId;

    private final String clientSecret;

    private Session session;

    private Builder builder;

    private SailPointServiceInterface authenticatedService;

    private SailPointServiceInterface unauthenticatedService;

    private SailPointService( Builder builder ) {
        this.builder = builder;
        this.url = builder.url;
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.session = null;
        this.authenticatedService = null;
        this.unauthenticatedService = null;
    }

    private SailPointServiceInterface getAuthenticatedService() throws Exception {

        if ( authenticatedService != null )
            return authenticatedService;

        if( session == null || session.getAccessToken() == null )
            this.session = createSession();

        OkHttpClient client = getBaseClientBuilder( builder )
                .addInterceptor( new BearerAuthInterceptor( session.getAccessToken() ) )
                .build();

        this.authenticatedService = getService( client );
        return this.authenticatedService;
    }

    private SailPointServiceInterface getUnauthenticatedService() {

        if ( unauthenticatedService != null )
            return unauthenticatedService;

        OkHttpClient client = getBaseClientBuilder( builder )
                .build();

        this.unauthenticatedService = getService( client );
        return this.unauthenticatedService;
    }

    private OkHttpClient.Builder getBaseClientBuilder( SailPointService.Builder builder ) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .retryOnConnectionFailure( true )
                .connectTimeout( builder.timeout, TimeUnit.MILLISECONDS )
                .readTimeout( builder.timeout, TimeUnit.MILLISECONDS )
                .writeTimeout( builder.timeout, TimeUnit.MILLISECONDS );

        if ( builder.proxy != null )
            clientBuilder.proxy( builder.proxy );

        if ( builder.proxyAuthenticator != null )
            clientBuilder.proxyAuthenticator( builder.proxyAuthenticator );

        return clientBuilder;
    }

    private SailPointServiceInterface getService(OkHttpClient client ) {

        return new Retrofit.Builder()
                .baseUrl( url )
                .addConverterFactory( GsonConverterFactory.create() )
                .client( client )
                .build()
                .create( SailPointServiceInterface.class );
    }

    public Session createSession() throws Exception {

        Session session = null;

        try {

            Response<Session> response = getUnauthenticatedService()
                    .getSession( "client_credentials", this.clientId, this.clientSecret )
                    .execute();

            if ( response.isSuccessful() )
                session = response.body();
            else
                throw new Exception ( "Error obtaining session! " + response.code() + " " + response.message() );

        } catch ( IOException e ) {
            e.printStackTrace();
        }

        return session;
    }

    /**
     * SailPoint API Methods
     */

    public ResponseBody aggregateAccounts( String sourceId, boolean disableOptimization ) throws Exception {

        Response<ResponseBody> response = getAuthenticatedService()
                .aggregateAccounts(
                        sourceId,
                        RequestBody.create( MultipartBody.FORM, String.valueOf( disableOptimization ) ) )
                .execute();

        if( !response.isSuccessful() )
            throw new Exception( "HTTP Code[" + response.code() + "] Message[" + response.message()  + "]" );

        return response.body();
    }

    public ResponseBody aggregateAccounts( String sourceId, boolean disableOptimization, File file ) throws Exception {

        Response<ResponseBody> response = getAuthenticatedService()
                .aggregateAccounts(
                        sourceId,
                        RequestBody.create( MultipartBody.FORM, String.valueOf( disableOptimization ) ),
                        RequestBody.create( MediaType.parse( "text/csv" ), file ) ).execute();

        if( !response.isSuccessful() )
            throw new Exception( "HTTP Code[" + response.code() + "] Message[" + response.message()  + "] Body[" + response.errorBody().string() + "]" );

        return response.body();
    }

    public ResponseBody aggregateEntitlements( String sourceId, String entitlementType, File file ) throws Exception {

        Response<ResponseBody> response = getAuthenticatedService()
                .aggregateEntitlements(
                        sourceId,
                        RequestBody.create( MultipartBody.FORM, entitlementType ),
                        RequestBody.create( MediaType.parse( "text/csv" ), file ) ).execute();

        if( !response.isSuccessful() )
            throw new Exception( "HTTP Code[" + response.code() + "] Message[" + response.message()  + "]" );

        return response.body();
    }

    public int countSources() throws Exception {
        return countSources( null );
    }

    public Source getSource( String id ) throws Exception {
        return getAuthenticatedService().getSource( id ).execute().body();
    }

    public int countSources( String filters ) throws Exception {

        Response<List<Source>> response = getAuthenticatedService()
                .listSources( true, 1, 0, filters, DEFAULT_SORTER ).execute();

        return ( response.isSuccessful()) ? Integer.parseInt( response.headers().get( HEADER_COUNT ) ) : 0;
    }

    public Iterator<Source> listSources() {
        return listSources( null, DEFAULT_PAGE_SIZE, DEFAULT_SORTER );
    }

    public Iterator<Source> listSources( String filters ) {
        return listSources( filters, DEFAULT_PAGE_SIZE, DEFAULT_SORTER );
    }

    public Iterator<Source> listSources( String filters, int pageSize, String sorters ) {

        return new Iterator<Source>() {

            List<Source> objects = new ArrayList<Source>(pageSize);
            int offset = 0;
            int objectIndex = 0;
            int totalCount = -1;

            @Override
            public boolean hasNext() {

                if (totalCount < 0) {

                    try {
                        totalCount = countSources( filters );
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }

                return (objectIndex < totalCount);
            }

            @Override
            public Source next() {

                if ((objectIndex - offset) >= objects.size()) {

                    try {

                        this.offset = (objectIndex / pageSize) * pageSize;
                        this.objects = getAuthenticatedService().listSources( true, pageSize, offset, filters, sorters ).execute().body();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                int pageIndex = objectIndex - offset;

                objectIndex++;

                return objects.get( pageIndex );
            }
        };
    }

    /**
     * Builder
     */
    public static class Builder {

        /*
         * Required Variables
         */
        private String url;
        private String clientId;
        private String clientSecret;

        /*
         * Optional Variables
         */
        private int timeout;
        private Proxy proxy = null;
        private Authenticator proxyAuthenticator = null;

        private Session session = null;

        public Builder() {
            this.url = null;
            this.clientId = null;
            this.clientSecret = null;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder timeout( int timeout ) {
            this.timeout = timeout;
            return this;
        }

        public Builder session( Session session ) {
            this.session = session;
            return this;
        }

        public Builder proxy( String proxyHost, int proxyPort ) {
            if ( proxyHost != null && proxyPort > 0 )
                this.proxy = new Proxy( Proxy.Type.HTTP, new InetSocketAddress( proxyHost, proxyPort ) );
            return this;
        }

        public Builder proxyAuthentication( String proxyUser, String proxyPassword ) {
            this.proxyAuthenticator = new Authenticator() {
                @Override
                public Request authenticate( Route route, okhttp3.Response response ) throws IOException {
                    return response.request().newBuilder()
                            .header( "Proxy-Authorization", Credentials.basic( proxyUser, proxyPassword ) )
                            .build();
                }
            };
            return this;
        }

        public SailPointService build() {
            return new SailPointService(this);
        }
    }


    public static class Constants {
        public static final String DEFAULT_SORTER = "id";
        public static final int DEFAULT_PAGE_SIZE = 100;

        public static final String HEADER_COUNT = "X-Total-Count";

        public static final String QUERY_OFFSET = "offset";
        public static final String QUERY_LIMIT = "limit";
        public static final String QUERY_FILTERS = "filters";
        public static final String QUERY_SORTERS = "sorters";
        public static final String QUERY_COUNT = "count";
    }
}
