package sailpoint.object.config;

public class ConfigTenant {
    String url;
    String clientId;
    String clientSecret;
    
    public ConfigTenant(String url, String clientId, String clientSecret) {
        this.url = url;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
    
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getClientSecret() {
        return clientSecret;
    }
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
