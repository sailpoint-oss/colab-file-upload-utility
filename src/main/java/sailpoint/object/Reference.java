package sailpoint.object;


import com.google.gson.annotations.SerializedName;

public class Reference {
	
	@SerializedName("id")
	String id;
	
	@SerializedName("name")
	String name;	
	
	@SerializedName("type")
	String type;

	/*
	 * Constructor
	 */
	
	public Reference( String id, String name, String type ) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
