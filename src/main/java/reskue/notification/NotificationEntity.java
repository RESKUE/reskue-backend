package reskue.notification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kueres.base.BaseEntity;
import reskue.user.UserEntity;

public class NotificationEntity extends BaseEntity<NotificationEntity> {

	@Column(name = "title", nullable = false)
	private String title = "";
	public static final String TITLE = "title";

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "message", nullable = false)
	private String message;
	public static final String MESSAGE = "message";

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "type", nullable = false)
	private int type = 0;
	public static final String TYPE = "type";

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "sender", nullable = false)
	private UserEntity sender;
	public static final String SENDER = "sender";

	public UserEntity getSender() {
		return this.sender;
	}

	public void setSender(UserEntity sender) {
		this.sender = sender;
	}

	@OneToMany(mappedBy = "notificationReceiver", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "receivers", nullable = false)
	private List<UserEntity> receivers = new ArrayList<UserEntity>();
	public static final String CULTURAL_ASSET_CHILDREN = "receivers";

	public List<UserEntity> getReceivers() {
		return this.receivers;
	}

	public void setReceivers(List<UserEntity> receivers) {
		this.receivers = receivers;
	}

	@Override
	public void applyPatch(NotificationEntity details) {
		String title = details.getTitle();
		String message = details.getMessage();
		int type = details.getType();
		UserEntity sender = details.getSender();
		List<UserEntity> receivers = details.getReceivers();

		if (title != null) {
			this.setTitle(title);
		}
		if (message != null) {
			this.setMessage(message);
		}
		this.setType(type);
		if (sender != null) {
			this.setSender(sender);
		}
		if (receivers != null) {
			this.setReceivers(receivers);
		}

	}

}
