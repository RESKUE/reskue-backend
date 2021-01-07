package kueres.media;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MediaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;	
	public long getId() {
		return this.id;
	}
	
	@Column(name = "location", nullable = false)
	private String location;
	public String getLocation() {
		return this.location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
