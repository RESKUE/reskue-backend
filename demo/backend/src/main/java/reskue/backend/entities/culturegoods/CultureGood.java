package reskue.backend.entities.culturegoods;

import javax.persistence.Column;
import javax.persistence.Entity;

import reskue.backend.entities.BaseEntity;

@Entity
public class CultureGood extends BaseEntity<CultureGood> {
	
	@Column(name = "name", nullable = false)
	public String name;
	
	@Column(name = "description", nullable = true)
	public String description;
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void applyPatch(CultureGood details) {
		String updatedName = details.getName();
		String updatedDescription = details.getDescription();
		if (updatedName != null) {
			setName(updatedName);
		}
		if (updatedDescription != null) {
			setDescription(updatedDescription);
		}
	}
	
}
