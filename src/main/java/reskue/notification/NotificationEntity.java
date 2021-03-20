package reskue.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kueres.base.BaseEntity;
import kueres.utility.Utility;
import reskue.culturalasset.CulturalAssetEntity;
import reskue.user.UserEntity;
import reskue.usergroup.UserGroupEntity;

/**
 * 
 * The NotificationEntity is a representation of notifications send by users.
 * The NotificationEntity works similar to the EventEntity in kueres.event
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

@Entity
public class NotificationEntity extends BaseEntity<NotificationEntity> {

	@Override
	public String[] getUpdateableFields() {
		return new String[] {
			NotificationEntity.TITLE,
			NotificationEntity.MESSAGE,
			NotificationEntity.TYPE,
			NotificationEntity.SENDER,
			NotificationEntity.RECEIVERS,
			NotificationEntity.ENTITY
		};
	}
	
	/**
	 * The title of the notification.
	 */
	@Column(name = "title", nullable = false)
	private String title = "Title";
	public static final String TITLE = "title";
	public String getTitle() { return this.title; }
	public void setTitle(String title) { this.title = title; }

	/**
	 * The message of the notification.
	 */
	@Column(name = "message", nullable = false, columnDefinition="TEXT")
	private String message = "";
	public static final String MESSAGE = "message";
	public String getMessage() { return this.message; }
	public void setMessage(String message) { this.message = message; }

	/**
	 * The type of the notification. See reskue.notification.NotificationType for all types.
	 */
	@Column(name = "type", nullable = false)
	private int type = 0;
	public static final String TYPE = "type";
	public int getType() { return this.type; }
	public void setType(int type) { this.type = type; }

	/**
	 * The user that send the notification.
	 */
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "notification_sender_id", referencedColumnName = "id")
	private UserEntity sender = null;
	public static final String SENDER = "sender";
	public UserEntity getSender() { return this.sender; }
	public void setSender(UserEntity sender) { this.sender = sender; }
	
	/**
	 * The list of user groups that should receive the notification.
	 */
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
			name = "notification_receivers",
			joinColumns = @JoinColumn(name = "notification_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "receiver_id", referencedColumnName = "id")
	)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private List<UserGroupEntity> receivers = new ArrayList<UserGroupEntity>();
	public static final String RECEIVERS = "receivers";
	public List<UserGroupEntity> getReceivers() { return this.receivers; }
	public void setReceivers(List<UserGroupEntity> receivers) { this.receivers = receivers; }
	
	/**
	 * The time when the notification was created.
	 */
	@Column(name = "sentAt", nullable = false)
	private Date sentAt = new Date();
	public static final String SENT_AT = "sentAt";
	public Date getSentAt() { return this.sentAt; }
	public void setSentAt(Date sentAt) { this.sentAt = sentAt; }
	
	/**
	 * The cultural asset that the notification refers to.
	 * This potentially marks the cultural asset as endangered.
	 */
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "notification_entity_id", referencedColumnName = "id")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private CulturalAssetEntity entity = null;
	public static final String ENTITY = "entity";
	public CulturalAssetEntity getEntity() { return this.entity; }
	public void setEntity(CulturalAssetEntity entity) { this.entity = entity; }
	
	@Override
	public void applyPatch(String json) {
		Utility.LOG.error("NotificationEntities can not be updated");
		throw new UnsupportedOperationException("NotificationEntities can not be updated!");
	}

}
