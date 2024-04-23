package sailpoint.service;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class BearerAuthInterceptor implements Interceptor {

	private String token;
	
	public Response intercept( Chain chain ) throws IOException {

		final String authorization = "Bearer " + token;
		
		Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
        	.addHeader( "authorization", authorization )
            .method( original.method(), original.body() );

        Request request = requestBuilder.build();
        return chain.proceed(request);
	}

	public BearerAuthInterceptor( String token ) {
		super();
		this.token = token;
	}
	
}
