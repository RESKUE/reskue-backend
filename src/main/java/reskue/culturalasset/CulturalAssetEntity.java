package reskue.culturalasset;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class CulturalAssetEntity extends ReskueEntity<CulturalAssetEntity>{
	
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
	@OneToMany(mappedBy = "culturalAsset", cascade = CascadeType.PERSIST)
	@JsonIdentityReference(alwaysAsId = true)
	private List<TaskEntity> tasks = new ArrayList<TaskEntity>();
	public static final String TASKS = "tasks";
	public List<TaskEntity> getTasks() { return this.tasks; }
	public void setTasks(List<TaskEntity> tasks) { this.tasks = tasks; }
	
	/**
	 * The list of comments associated with the cultural asset.
	 */
	@OneToMany(mappedBy = "commentCulturalAsset", cascade = CascadeType.PERSIST)
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
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "parent_id", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private CulturalAssetEntity culturalAssetParent;
	public static final String CULTURAL_ASSET_PARENT = "culturalAssetParent";
	public CulturalAssetEntity getCulturalAssetParent() { return this.culturalAssetParent; }
	public void setCulturalAssetParent(CulturalAssetEntity culturalAssetParent) { this.culturalAssetParent = culturalAssetParent; }
	
	/**
	 * The list of children associated with the cultural asset.
	 */
	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "child_parent_id", referencedColumnName = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private List<CulturalAssetEntity> culturalAssetChildren = new ArrayList<CulturalAssetEntity>();
	public static final String CULTURAL_ASSET_CHILDREN = "culturalAssetChildren";	
	public List<CulturalAssetEntity> getCulturalAssetChildren() { return this.culturalAssetChildren; }
	public void setCulturalAssetChildren(List<CulturalAssetEntity> culturalAssetChildren) { this.culturalAssetChildren = culturalAssetChildren; }
	
	/**
	 * The list of notifications associated with the cultural asset.
	 */
	@OneToMany(mappedBy = "entity", cascade = CascadeType.PERSIST)
	@JsonIdentityReference(alwaysAsId = true)
	private List<NotificationEntity> notifications = new ArrayList<NotificationEntity>();
	public static final String NOTIFICATIONS = "notifications";	
	public List<NotificationEntity> getNotifications() { return this.notifications; }
	public void setNotifications(List<NotificationEntity> notifications) { this.notifications = notifications; }
	
	/**
	 * Doesnt allow changes to:
	 *  - culturalAssetChildren
	 *  - culturalAssetParent
	 *  - level
	 */
	@Override
	public void applyPatch(CulturalAssetEntity details) {
		
		String name = details.getName();
		String description = details.getDescription();
		int priority = details.getPriority();
		int isEndangered = details.getIsEndangered();	
		List<CommentEntity> comments = details.getComments();
		List<MediaEntity> media = details.getMedia();	
		
		String address = details.getAddress();
		String locationId = details.getLocationId();
		double longitude = details.getLongitude();
		double latitude = details.getLatitude();
		List<TaskEntity> tasks = details.getTasks();
		String label = details.getLabel();
		List<NotificationEntity> notifications = details.getNotifications();

		if (name != "unnamed" || this.getName() != "unnamed") {
			this.setName(name);
		}
		if (description != "" || this.getDescription() != "") {
			this.setDescription(description);
		}
		if (priority != 0 || this.getPriority() != 0) {
			this.setPriority(priority);
		}
		if (isEndangered != 0 || this.getIsEndangered() != 0) {
			this.setIsEndangered(isEndangered);	
		}
		if (comments != null) {
			this.setComments(comments);
		}
		if (media != null) {
			this.setMedia(media);
		}

		this.setAddress(address);
		this.setLongitude(longitude);
		this.setLatitude(latitude);
		this.setLabel(label);
		this.setLocationId(locationId);	
		if (tasks != null) {
			this.setTasks(tasks);
		}
		if (notifications != null) {
			this.setNotifications(notifications);
		}
		
	}

}
