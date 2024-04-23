package sailpoint.object;


import com.google.gson.annotations.SerializedName;

public class Session {
	
	@SerializedName("access_token")
	String accessToken;
	
	@SerializedName("token_type")
	String tokenType;
	
	@SerializedName("expires_in")
	int expiresIn;
	
	@SerializedName("scope")
	String scope;
	
	@SerializedName("tenant_id")
	String tenantId;
	
	@SerializedName("pod")
	String pod;
	
	@SerializedName("strong_auth_supported")
	boolean strongAuthSupported;
	
	@SerializedName("org")
	String org;
	
	@SerializedName("identity_id")
	String identityId;
	
	@SerializedName("user_name")
	String userName;
	
	@SerializedName("strong_auth")
	boolean strongAuth;
	
	@SerializedName("jti")
	String jti;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getPod() {
		return pod;
	}

	public void setPod(String pod) {
		this.pod = pod;
	}

	public boolean isStrongAuthSupported() {
		return strongAuthSupported;
	}

	public void setStrongAuthSupported(boolean strongAuthSupported) {
		this.strongAuthSupported = strongAuthSupported;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isStrongAuth() {
		return strongAuth;
	}

	public void setStrongAuth(boolean strongAuth) {
		this.strongAuth = strongAuth;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

}
