package sailpoint.identitynow.api.object;

import com.google.gson.annotations.SerializedName;

/*
 * {
 *   "access_token": "PpyWVC5QFxzJRIJ0",
 *   "token_type": "bearer",
 *   "expires_in": 3600
 * }
 */
public class Session {
		  
	@SerializedName("access_token")
	public final String accessToken;
		  
	@SerializedName("token_type")
	public final String tokenType;
		  
	@SerializedName("expires_in")
	public final int expiresIn;
		  
	@SerializedName("error")
	public final String error;
			  
	public Session ( String accessToken, String tokenType, int expiresIn, String error ) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
		this.error = error;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public String getError() {
		return error;
	}
	
}
