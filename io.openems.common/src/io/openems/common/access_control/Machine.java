package io.openems.common.access_control;

public class Machine {

	private String id;
	private String name;
	private String description;
	private String apiKey;
	private ApplicationType type;
	private RoleId role;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public ApplicationType getType() {
		return type;
	}

	public void setType(ApplicationType type) {
		this.type = type;
	}

	public RoleId getRole() {
		return role;
	}

	public void setRole(RoleId role) {
		this.role = role;
	}
}
