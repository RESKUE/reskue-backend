package reskue.backend.entities.demo;

import javax.persistence.Column;
import javax.persistence.Entity;

import reskue.backend.entities.BaseEntity;

@Entity
public class DemoEntity extends BaseEntity<DemoEntity>{

	@Column(name = "name", nullable = false)
	private String name;
	public static final String NAME = "name";
	public static final String NAME_MAPPING = "/{name}";
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "description", nullable = false)
	private String description;
	public static final String DESCRIPTION = "description";
	public static final String DESCRIPTION_MAPPING = "/{description}";
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public void applyPatch(DemoEntity patch) {
		String name = patch.getName();
		String description = patch.getDescription();
		if (name != null) {
			this.setName(name);
		}
		if (description != null) {
			this.setDescription(description);
		}
	}
	
}
