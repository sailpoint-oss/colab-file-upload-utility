package sailpoint.identitynow.api.object;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class Tenant {

	@SerializedName("url")
	public final String url;

	@SerializedName("client_id")
	public final String clientId;

	@SerializedName("client_secret")
	public final String clientSecret;

	public Tenant ( Map<String, String> map ) {
		this.url = map.get( "url" );
		this.clientId = map.get( "clientId" );
		this.clientSecret = map.get( "clientSecret" );
	}
	
	public Tenant ( String url, String clientId, String clientSecret ) {
		this.url = url;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public Tenant ( String alias, String url, String clientId, String clientSecret ) {
		this.url = url;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public String getUrl() {
		return url;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}
	
}
