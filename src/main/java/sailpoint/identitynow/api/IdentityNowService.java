package sailpoint.identitynow.api;

import okhttp3.Authenticator;
import java.net.Proxy;

import retrofit2.Call;
import sailpoint.identitynow.api.object.Session;
import sailpoint.identitynow.api.object.Tenant;
import sailpoint.identitynow.api.services.AggregationService;

public final class IdentityNowService {
	
    private Tenant tenant;
    
    private Proxy proxy;
    
    private Authenticator proxyAuthenticator;
    
    private Long timeout;
    
    private Session session;
    
    public IdentityNowService( String url, String clientId, String clientSecret, Proxy proxy, Authenticator proxyAuthenticator, Long timeout ) {
		this.tenant = new Tenant( url, clientId, clientSecret );
		this.proxy = proxy;
		this.proxyAuthenticator = proxyAuthenticator;
		this.timeout = timeout;
	}
    
	public IdentityNowService( Tenant tenant ) {
		this.tenant = tenant;
	}
	
	public Session createSession() throws Exception {
		return this.session = SessionFactory.createSession( this.tenant, this.proxy, this.proxyAuthenticator, this.timeout );
	}
  
	/*
	 * Services
	 */
	
	public AggregationService getAggregationService() throws Exception {
		return ServiceFactory.getService( AggregationService.class, this.tenant, this.session, this.proxy, this.proxyAuthenticator, this.timeout );
	}
	
	public static <T> T execute ( Call<T> call ) throws Exception {	  
		return call.execute().body();
	}
  
	public class Constants {
		public static final String HTTP_HEADER_AUTHORIZATION_KEY = "authorization";
	    public static final String HTTP_HEADER_CACHE_CONTROL_KEY = "cache-control";
	    public static final String HTTP_HEADER_CACHE_CONTROL_NO_CACHE = "no-cache";
	    public static final String HTTP_HEADER_CONTENT_TYPE_KEY = "content-type";
	    public static final String HTTP_HEADER_CONTENT_TYPE_JSON = "application/json";
	}
	
}