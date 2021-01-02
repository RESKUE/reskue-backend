package reskue.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import kueres.entities.BaseEntity;

@MappedSuperclass
public abstract class ReskueEntity extends BaseEntity<ReskueEntity> {

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
	
}
