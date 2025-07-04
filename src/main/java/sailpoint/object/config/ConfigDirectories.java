package sailpoint.object.config;

public class ConfigDirectories {
	private boolean enableFileJourney;
	private ConfigStructure[] structures;
	
	public boolean isEnableFileJourney() {
		return enableFileJourney;
	}
	public void setEnableFileJourney(boolean enableFileJourney) {
		this.enableFileJourney = enableFileJourney;
	}
	public ConfigStructure[] getStructures() {
		return structures;
	}
	public void setStructures(ConfigStructure[] structures) {
		this.structures = structures;
	}
}
