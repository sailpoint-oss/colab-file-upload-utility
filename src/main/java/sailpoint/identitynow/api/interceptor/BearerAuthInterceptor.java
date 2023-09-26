package sailpoint.identitynow.api.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import sailpoint.identitynow.api.IdentityNowService.Constants;;

public class BearerAuthInterceptor implements Interceptor {

	private String token;
	
	public Response intercept( Chain chain ) throws IOException {
	
		final String authorization = "Bearer " + token;
		
		Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
        	.addHeader( Constants.HTTP_HEADER_AUTHORIZATION_KEY, authorization )
        	.addHeader( Constants.HTTP_HEADER_CACHE_CONTROL_KEY, Constants.HTTP_HEADER_CACHE_CONTROL_NO_CACHE )
            .method( original.method(), original.body() );

        Request request = requestBuilder.build();
        return chain.proceed(request);
	}

	public BearerAuthInterceptor( String token ) {
		super();
		this.token = token;
	}
	
}
