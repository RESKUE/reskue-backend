package reskue.notification;

/**
 * 
 * All notification types used.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0.0
 * @since Apr 26, 2021
 *
 */

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
	ALARM(2),
	
	/**
	 * The notification signals that an alarm is over.
	 */
	ALARM_OVER(3);
	
	public final int type;
	
	private NotificationType(int type) {
		this.type = type;
	}

}
