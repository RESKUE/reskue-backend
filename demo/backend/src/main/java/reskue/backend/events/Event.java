package reskue.backend.events;

import javax.persistence.Column;
import javax.persistence.Entity;

import reskue.backend.entities.BaseEntity;

@Entity
public class Event extends BaseEntity<Event> {

	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "type", nullable = false)
	private String type;
	
	public static final String TYPE = "type";
	public static final String TYPE_MAPPING = "/{type}";
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getType() {
		return type;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void applyPatch(Event details) {
		String updatedName = details.getName();
		String updatedDescription = details.getDescription();
		String updatedType = details.getType();
		
		if (updatedName != null) {
			setName(updatedName);
		}
		if (updatedDescription != null) {
			setDescription(updatedDescription);
		}
		if (updatedType != null) {
			setType(updatedType);
		}
	}
	
}
