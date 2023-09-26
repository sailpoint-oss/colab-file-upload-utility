package sailpoint.identitynow.api;

import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sailpoint.identitynow.api.object.Session;
import sailpoint.identitynow.api.object.Tenant;
import sailpoint.identitynow.api.services.AuthorizationService;

public class SessionFactory {
	
	public static Session createSession ( Tenant tenant, Proxy proxy, Authenticator proxyAuthenticator, Long timeout ) throws Exception {

		if ( tenant == null )
			throw new Exception( "Invalid tenant configuration.  Expected non-null tenant configuration." );
		
		Session session = null;

		try {

			Response<Session> response = SessionFactory
					.createService( AuthorizationService.class, tenant.getUrl(), proxy, proxyAuthenticator, timeout )
					.getSession( "client_credentials", tenant.getClientId(), tenant.getClientSecret() )
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
	
	public static <S> S createService( Class<S> serviceClass, String url, Proxy proxy, Authenticator proxyAuthenticator, Long timeout ) {
		    
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
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
