package reskue.notification;

public enum NotificationType {
	
	/**
	 * The type of the notification is not defined.
	 */
	UNDEFINED(0),
	
	/**
	 * The notification is a normal notification.
	 */
	NORMAL(1),
	
	/**
	 * The notification is an alarm.
	 */
	ALARM(2);
	
	public final int type;
	
	private NotificationType(int type) {
		this.type = type;
	}

}
