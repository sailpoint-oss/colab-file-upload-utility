package sailpoint.object.config;

public class ConfigAggregation {
	private String sourceId;
    private boolean disableOptimization;
    private String objectType;
    private boolean recursive;
    private boolean simulate;
    private int timeout;
    private String[] extensions;
    private boolean enableFileJourney;
    private ConfigStructure structure;
    
    public ConfigAggregation(boolean disableOptimization, String objectType, boolean recursive, boolean simulate,
            int timeout, String[] extensions) {
        this.disableOptimization = disableOptimization;
        this.objectType = objectType;
        this.recursive = recursive;
        this.simulate = simulate;
        this.timeout = timeout;
        this.extensions = extensions;
    }

    public boolean isDisableOptimization() {
        return disableOptimization;
    }

    public void setDisableOptimization(boolean disableOptimization) {
        this.disableOptimization = disableOptimization;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    public boolean isSimulate() {
        return simulate;
    }

    public void setSimulate(boolean simulate) {
        this.simulate = simulate;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public boolean isEnableFileJourney() {
		return enableFileJourney;
	}

	public void setEnableFileJourney(boolean enableFileJourney) {
		this.enableFileJourney = enableFileJourney;
	}

	public ConfigStructure getStructure() {
		return structure;
	}

	public void setStructure(ConfigStructure structure) {
		this.structure = structure;
	}

    
}
