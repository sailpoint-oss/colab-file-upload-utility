package sailpoint.object;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Source {
	
	@SerializedName("id")
	String id;
	
	@SerializedName("name")
	String name;	
	
	@SerializedName("created")
	String created;
	
	@SerializedName("modified")
	String modified;
	
	@SerializedName("description")
	String description;	
	
	@SerializedName("owner")
	Reference owner;
	  
	@SerializedName("cluster")
	Reference cluster;
	
	@SerializedName("features")
	List<String> features;	
	
	@SerializedName("schemas")
	List<Reference> schemas;
	
	@SerializedName("type")
	String type;	
	
	@SerializedName("connector")
	String connector;	
	
	@SerializedName("connectorClass")
	String connectorClass;
	
	@SerializedName("connectorId")
	String connectorId;
	
	@SerializedName("connectorName")
	String connectorName;
	
	@SerializedName("connectionType")
	String connectionType;
	
	@SerializedName("connectorImplementationId")
	String connectorImplementationId;
	
	@SerializedName("connectorAttributes")
	Map<String,Object> connectorAttributes;
	
	@SerializedName("managerCorrelationMapping")
	Map<String,Object> managerCorrelationMapping;
	
	@SerializedName("accountCorrelationConfig")
	Reference accountCorrelationConfig;
	
	@SerializedName("accountCorrelationRule")
	Reference accountCorrelationRule;
	
	@SerializedName("managerCorrelationRule")
	Reference managerCorrelationRule;
	
	@SerializedName("beforeProvisioningRule")
	Reference beforeProvisioningRule;
	
	@SerializedName("passwordPolicies")
	List<Reference> passwordPolicies;
	
	@SerializedName("managementWorkgroup")
	Reference managementWorkgroup;
	
	@SerializedName("deleteThreshold")
	int deleteThreshold;
	
	@SerializedName("authoritative")
	boolean authoritative;
	
	@SerializedName("healthy")
	boolean healthy;
	
	@SerializedName("status")
	String status;
	
	@SerializedName("since")
	String since;
	
	/*
	 * Getters and Setters
	 */
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Reference getOwner() {
		return owner;
	}

	public void setOwner(Reference owner) {
		this.owner = owner;
	}

	public Reference getCluster() {
		return cluster;
	}

	public void setCluster(Reference cluster) {
		this.cluster = cluster;
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public List<Reference> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<Reference> schemas) {
		this.schemas = schemas;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConnector() {
		return connector;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	}

	public String getConnectorClass() {
		return connectorClass;
	}

	public void setConnectorClass(String connectorClass) {
		this.connectorClass = connectorClass;
	}

	public String getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(String connectorId) {
		this.connectorId = connectorId;
	}

	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getConnectorImplementationId() {
		return connectorImplementationId;
	}

	public void setConnectorImplementationId(String connectorImplementationId) {
		this.connectorImplementationId = connectorImplementationId;
	}

	public Map<String, Object> getConnectorAttributes() {
		return connectorAttributes;
	}

	public Object getConnectorAttribute( String connectorAttribute ) {
		return connectorAttributes.get( connectorAttribute );
	}

	public void setConnectorAttributes(Map<String, Object> connectorAttributes) {
		this.connectorAttributes = connectorAttributes;
	}

	public Map<String, Object> getManagerCorrelationMapping() {
		return managerCorrelationMapping;
	}

	public void setManagerCorrelationMapping(Map<String, Object> managerCorrelationMapping) {
		this.managerCorrelationMapping = managerCorrelationMapping;
	}

	public Reference getAccountCorrelationConfig() {
		return accountCorrelationConfig;
	}

	public void setAccountCorrelationConfig(Reference accountCorrelationConfig) {
		this.accountCorrelationConfig = accountCorrelationConfig;
	}

	public Reference getAccountCorrelationRule() {
		return accountCorrelationRule;
	}

	public void setAccountCorrelationRule(Reference accountCorrelationRule) {
		this.accountCorrelationRule = accountCorrelationRule;
	}

	public Reference getManagerCorrelationRule() {
		return managerCorrelationRule;
	}

	public void setManagerCorrelationRule(Reference managerCorrelationRule) {
		this.managerCorrelationRule = managerCorrelationRule;
	}

	public Reference getBeforeProvisioningRule() {
		return beforeProvisioningRule;
	}

	public void setBeforeProvisioningRule(Reference beforeProvisioningRule) {
		this.beforeProvisioningRule = beforeProvisioningRule;
	}

	public List<Reference> getPasswordPolicies() {
		return passwordPolicies;
	}

	public void setPasswordPolicies(List<Reference> passwordPolicies) {
		this.passwordPolicies = passwordPolicies;
	}

	public Reference getManagementWorkgroup() {
		return managementWorkgroup;
	}

	public void setManagementWorkgroup(Reference managementWorkgroup) {
		this.managementWorkgroup = managementWorkgroup;
	}

	public int getDeleteThreshold() {
		return deleteThreshold;
	}

	public void setDeleteThreshold(int deleteThreshold) {
		this.deleteThreshold = deleteThreshold;
	}

	public boolean isAuthoritative() {
		return authoritative;
	}

	public void setAuthoritative(boolean authoritative) {
		this.authoritative = authoritative;
	}

	public boolean isHealthy() {
		return healthy;
	}

	public void setHealthy(boolean healthy) {
		this.healthy = healthy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSince() {
		return since;
	}

	public void setSince(String since) {
		this.since = since;
	}

	public Reference getReference() {
		return new Reference( this.id, this.name, Source.getReferenceType() );
	}

	public static String getReferenceType() {
		return "SOURCE";
	}

}
