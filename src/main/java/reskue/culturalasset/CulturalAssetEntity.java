package reskue.culturalasset;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import kueres.base.BaseEntity;
import kueres.media.MediaEntity;
import reskue.ReskueEntity;
import reskue.comment.CommentEntity;
import reskue.notification.NotificationEntity;
import reskue.task.TaskEntity;

/**
 * 
 * The CulturalAssetEntity is a representation of cultural assets.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

@Entity
public class CulturalAssetEntity extends ReskueEntity<CulturalAssetEntity>{
	
	@Override
	public String[] getUpdateableFields() {
		return new String[] {
				CulturalAssetEntity.NAME,
				CulturalAssetEntity.DESCRIPTION,
				CulturalAssetEntity.PRIORITY,
				CulturalAssetEntity.IS_ENDANGERED,
				CulturalAssetEntity.LABEL,
				CulturalAssetEntity.ADDRESS,
				CulturalAssetEntity.LONGITUDE,
				CulturalAssetEntity.LATITUDE,
				CulturalAssetEntity.TASKS,
				CulturalAssetEntity.COMMENTS,
				CulturalAssetEntity.MEDIA,
				CulturalAssetEntity.NOTIFICATIONS,
				CulturalAssetEntity.CULTURAL_ASSET_PARENT,
				CulturalAssetEntity.CULTURAL_ASSET_CHILDREN
		};
	}
	
	/**
	 * The address of the cultural asset.
	 */
	@Column(name = "address", nullable = true)
	private String address = null;
	public static final String ADDRESS = "address";
	public String getAddress() { return this.address; }
	public void setAddress(String address) { this.address = address; }
	
	/**
	 * The longitude of the cultural asset.
	 */
	@Column(name = "longitude", nullable = true)
	private Double longitude = null;
	public static final String LONGITUDE = "longitude";
	public Double getLongitude() { return this.longitude; }
	public void setLongitude(Double longitude) { this.longitude = longitude; }
	
	/**
	 * The latitude of the cultural asset.
	 */
	@Column(name = "latitude", nullable = true)
	private Double latitude = null;
	public static final String LATITUDE = "latitude";
	public Double getLatitude() { return this.latitude; }
	public void setLatitude(Double latitude) { this.latitude = latitude; }
	
	/**
	 * The list of tasks associated with the cultural asset.
	 */
	@OneToMany()
	@JoinColumn(name = "cultural_asset_id", referencedColumnName = "id")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private List<TaskEntity> tasks = new ArrayList<TaskEntity>();
	public static final String TASKS = "tasks";
	public List<TaskEntity> getTasks() { return this.tasks; }
	public void setTasks(List<TaskEntity> tasks) { this.tasks = tasks; }
	
	/**
	 * The list of comments associated with the cultural asset.
	 */
	@OneToMany()
	@JoinTable(
			name = "cultural_asset_comments",
			joinColumns = { @JoinColumn(name = "cultural_asset_id", referencedColumnName = BaseEntity.ID) },
			inverseJoinColumns = { @JoinColumn(name = "comment_id", referencedColumnName = BaseEntity.ID) }
	)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private List<CommentEntity> comments = new ArrayList<CommentEntity>();
	public static final String COMMENTS = "comments";
	public List<CommentEntity> getComments() { return this.comments; }
	public void setComments(List<CommentEntity> comments) { this.comments = comments; }
	
	/**
	 * The label of the cultural asset.
	 * Labels are similar to descriptions but are intended to be shorter and more important information.
	 */
	@Column(name = "label", nullable = true)
	private String label = null;
	public static final String LABEL = "label";
	public String getLabel() { return this.label; }
	public void setLabel(String label) { this.label = label; }
	
	/**
	 * The level of the cultural asset.
	 * A cultural asset without a parent is level 0
	 * The hierarchy only supports 4 levels 
	 */
	@Column(name = "level", nullable = false)
	private int level = 0;
	public static final String LEVEL = "level";
	public int getLevel() { return this.level; }
	public void setLevel(int level) { this.level = level; }
	
	/**
	 * The location id of the cultural asset.
	 * This is necessary because the FROST server cannot set specific ids.
	 */
	@Column(name = "locationId", nullable = true)
	private String locationId = null;
	public static final String LOCATION_ID = "locationId";
	public String getLocationId() { return this.locationId; }
	public void setLocationId(String locationId) { this.locationId = locationId; }
	
	/**
	 * The parent of the cultural asset.
	 */
	@ManyToOne()
	@JoinColumn(name = "parent_id", referencedColumnName = BaseEntity.ID)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private CulturalAssetEntity culturalAssetParent;
	public static final String CULTURAL_ASSET_PARENT = "culturalAssetParent";
	public CulturalAssetEntity getCulturalAssetParent() { return this.culturalAssetParent; }
	public void setCulturalAssetParent(CulturalAssetEntity culturalAssetParent) { this.culturalAssetParent = culturalAssetParent; }
	
	/**
	 * The list of children associated with the cultural asset.
	 */
	@OneToMany()
	@JoinColumn(name = "child_parent_id", referencedColumnName = BaseEntity.ID)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private List<CulturalAssetEntity> culturalAssetChildren = new ArrayList<CulturalAssetEntity>();
	public static final String CULTURAL_ASSET_CHILDREN = "culturalAssetChildren";	
	public List<CulturalAssetEntity> getCulturalAssetChildren() { return this.culturalAssetChildren; }
	public void setCulturalAssetChildren(List<CulturalAssetEntity> culturalAssetChildren) { this.culturalAssetChildren = culturalAssetChildren; }
	
	/**
	 * The list of notifications associated with the cultural asset.
	 */
	@OneToMany()
	@JoinColumn(name = "notification_entity_id", referencedColumnName = BaseEntity.ID)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private List<NotificationEntity> notifications = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATIONS = "notifications";	
	public List<NotificationEntity> getNotifications() { return this.notifications; }
	public void setNotifications(List<NotificationEntity> notifications) { this.notifications = notifications; }
	
	/**
	 * The list of media associated with the entity.
	 */
	@OneToMany()
	@JoinTable(
			name = "cultural_asset_media",
			joinColumns = @JoinColumn(name = "cultural_asset_id", referencedColumnName = BaseEntity.ID),
			inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = BaseEntity.ID)
	)
	private List<MediaEntity> media = new ArrayList<MediaEntity>();
	public static final String MEDIA = "media";
	public List<MediaEntity> getMedia() { return this.media; }
	public void setMedia(List<MediaEntity> media) { this.media = media; }
	
	/**
	 * Does not allow changes to:
	 *  - culturalAssetChildren
	 *  - culturalAssetParent
	 *  - level
	 *  - locationId
	 */
	@Override
	public void applyPatch(String json) throws JsonMappingException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, JsonProcessingException {
		
		CulturalAssetEntity details = CulturalAssetEntity.createEntityFromJSON(json, this.getUpdateableFields(), CulturalAssetEntity.class);
		
		if (this.containsFields(json, CulturalAssetEntity.NAME)) {
			this.setName(details.getName());
		}
		if (this.containsFields(json, CulturalAssetEntity.DESCRIPTION)) {
			this.setDescription(details.getDescription());
		}
		if (this.containsFields(json, CulturalAssetEntity.PRIORITY)) {
			this.setPriority(details.getPriority());
		}
		if (this.containsFields(json, CulturalAssetEntity.IS_ENDANGERED)) {
			this.setIsEndangered(details.getIsEndangered());	
		}
		if (this.containsFields(json, CulturalAssetEntity.COMMENTS)) {
			this.setComments(details.getComments());
		}
		if (this.containsFields(json, CulturalAssetEntity.MEDIA)) {
			this.setMedia(details.getMedia());
		}
		
		if (this.containsFields(json, CulturalAssetEntity.ADDRESS)) {
			this.setAddress(details.getAddress());
		}
		if (this.containsFields(json, CulturalAssetEntity.LABEL)) {
			this.setLabel(details.getLabel());
		}
		if (this.containsFields(json, CulturalAssetEntity.LONGITUDE)) {
			this.setLongitude(details.getLongitude());
		}
		if (this.containsFields(json, CulturalAssetEntity.LATITUDE)) {
			this.setLatitude(details.getLatitude());
		}

		if (this.containsFields(json, CulturalAssetEntity.TASKS)) {
			this.setTasks(details.getTasks());
		}
		if (this.containsFields(json, CulturalAssetEntity.NOTIFICATIONS)) {
			this.setNotifications(details.getNotifications());
		}
		if (this.containsFields(json, CulturalAssetEntity.CULTURAL_ASSET_PARENT)) {
			this.setCulturalAssetParent(details.getCulturalAssetParent());
		}
		if (this.containsFields(json, CulturalAssetEntity.CULTURAL_ASSET_CHILDREN)) {
			this.setCulturalAssetChildren(details.getCulturalAssetChildren());
		}
		
	}

}
