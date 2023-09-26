package sailpoint.identitynow.api;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sailpoint.identitynow.api.interceptor.BearerAuthInterceptor;
import sailpoint.identitynow.api.object.Session;
import sailpoint.identitynow.api.object.Tenant;
import sailpoint.identitynow.api.services.AggregationService;

public class ServiceFactory {

	public static AggregationService getAggregationService ( Tenant tenant, Session session, Proxy proxy, Authenticator proxyAuthenticator, Long timeout ) {
		return getService( 
				AggregationService.class, 
				tenant.getUrl(), 
				session.getAccessToken(),
				proxy,
				proxyAuthenticator,
				timeout
				);
		
	}
	
	public static <S> S getService ( Class<S> serviceClass, Tenant tenant, Session session, Proxy proxy, Authenticator proxyAuthenticator, Long timeout ) {
		return getService( 
				serviceClass, 
				tenant.getUrl(), 
				session.getAccessToken(),
				proxy,
				proxyAuthenticator,
				timeout
				);	
	}
	
	public static <S> S getService ( Class<S> serviceClass, String url, String token, Proxy proxy, Authenticator proxyAuthenticator, Long timeout ) {
		
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
			.addInterceptor( new BearerAuthInterceptor( token ) )
			.retryOnConnectionFailure( true )
			.connectTimeout( timeout, TimeUnit.MILLISECONDS )
			.readTimeout( timeout, TimeUnit.MILLISECONDS )
			.writeTimeout( timeout, TimeUnit.MILLISECONDS );
				
		if ( proxy != null )
			clientBuilder.proxy( proxy );
		
		if ( proxyAuthenticator != null )
			clientBuilder.proxyAuthenticator( proxyAuthenticator );
			
		OkHttpClient client = clientBuilder.build();	
		
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl( url )
			.addConverterFactory( GsonConverterFactory.create() )
			.client( client )
			.build();

		return retrofit.create( serviceClass );
	}
}
