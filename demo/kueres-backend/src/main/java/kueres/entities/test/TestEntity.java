package kueres.entities.test;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kueres.entities.BaseEntity;
import kueres.entities.event.EventEntity;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TestEntity extends BaseEntity<TestEntity> {
	
	public TestEntity() {
		
	}
	
	public TestEntity(String name) {
		this.name = name;
	}
	
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
	
	@OneToMany(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "events", nullable = true)
	private List<EventEntity> events;
	public static final String EVENTS = "events";
	public static final String EVENTS_MAPPING = "/{events}";
	public List<EventEntity> getEvents() {
		return this.events;
	}
	public void setEvents(List<EventEntity> events) {
		this.events = events;
	}
	
	@Override
	public void applyPatch(TestEntity patch) {
		String name = patch.getName();
		List<EventEntity> events = patch.getEvents();
		if (name != null) {
			this.setName(name);
		}
		if (events != null) {
			this.setEvents(events);
		}
	}
	
}
