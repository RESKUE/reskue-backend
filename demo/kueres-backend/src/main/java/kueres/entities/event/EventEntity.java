package kueres.entities.event;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import kueres.entities.BaseEntity;

@Entity
public class EventEntity extends BaseEntity<EventEntity> {
	
	@Column(name = "message", nullable = false)
	private String message;
	public static final String MESSAGE = "message";
	public static final String MESSAGE_MAPPING = "/{message}";
	public String getMessage() {
		return this.message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Column(name = "type", nullable = false)
	private int type = -1;
	public static final String TYPE = "type";
	public static final String TYPE_MAPPING = "/{type}";
	public int getType() {
		return this.type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	@Column(name = "sender", nullable = false)
	private String sender;
	public static final String SENDER = "sender";
	public static final String SENDER_MAPPING = "/{sender}";
	public String getSender() {
		return this.sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	@Column(name = "sendAt", nullable = false)
	private Date sendAt;
	public static final String SEND_AT = "sendAt";
	public static final String SEND_AT_MAPPING = "/{sendAt}";
	public Date getSendAt() {
		return this.sendAt;
	}
	public void setSendAt(Date sendAt) {
		this.sendAt = sendAt;
	}
	
	@Override
	public void applyPatch(EventEntity patch) {
		String message = patch.getMessage();
		int type = patch.getType();
		String sender = patch.getSender();
		Date sendAt = patch.getSendAt();
		if (message != null) {
			this.setMessage(message);
		}
		if (type != -1) {
			this.setType(type);
		}
		if (sender != null) {
			this.setSender(sender);
		}
		if (sendAt != null) {
			this.setSendAt(sendAt);
		}
	}
	
}
