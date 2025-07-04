package sailpoint.object.config;

public class ConfigProxy {
    private boolean enabled;
    private String host;
    private int port;
    private String user;
    private String password;
    
    public ConfigProxy(boolean enabled, String host, int port, String user, String password) {
        this.enabled = enabled;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
