package reskue.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kueres.base.BaseEntity;
import kueres.utility.Utility;
import reskue.culturalasset.CulturalAssetEntity;
import reskue.user.UserEntity;

@Entity
public class NotificationEntity extends BaseEntity<NotificationEntity> {

	@Column(name = "title", nullable = false)
	private String title = "";
	public static final String TITLE = "title";
	public String getTitle() { return this.title; }
	public void setTitle(String title) { this.title = title; }

	@Column(name = "message", nullable = false, columnDefinition="TEXT")
	private String message = "";
	public static final String MESSAGE = "message";
	public String getMessage() { return this.message; }
	public void setMessage(String message) { this.message = message; }

	@Column(name = "type", nullable = false)
	private int type = 0;
	public static final String TYPE = "type";
	public int getType() { return this.type; }
	public void setType(int type) { this.type = type; }

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "notificationSender")
	private UserEntity sender;
	public static final String SENDER = "sender";
	public UserEntity getSender() { return this.sender; }
	public void setSender(UserEntity sender) { this.sender = sender; }

	@ManyToMany(mappedBy = "notificationReceiver", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<UserEntity> receivers = new ArrayList<UserEntity>();
	public static final String RECEIVERS = "receivers";
	@JsonIgnore
	public List<UserEntity> getReceivers() { return this.receivers; }
	public void setReceivers(List<UserEntity> receivers) { this.receivers = receivers; }
	
	@Column(name = "sendAt", nullable = false)
	private Date sendAt = new Date();
	public static final String SEND_AT = "sendAt";
	public Date getSendAt() { return this.sendAt; }
	public void setSendAt(Date sendAt) { this.sendAt = sendAt; }
	
	@Column(name = "entityId", nullable = false)
	private long entityId = 0;
	public static final String ENTITY_ID = "entityId";
	public long getEntityId() { return this.entityId; }
	public void setEntityId(long entityId) { this.entityId = entityId; }
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "notifications")
	private CulturalAssetEntity entity;
	public static final String ENTITY = "entity";
	public CulturalAssetEntity getEntity() { return this.entity; }
	public void setEntity(CulturalAssetEntity entity) { this.entity = entity; }
	
	@Override
	public void applyPatch(NotificationEntity details) {
		Utility.LOG.error("NotificationEntities can not be updated");
		throw new UnsupportedOperationException("NotificationEntities can not be updated!");
	}

}
