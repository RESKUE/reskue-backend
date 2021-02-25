package reskue.subtask;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kueres.base.BaseEntity;
import reskue.task.TaskEntity;

/**
 * 
 * The SubtaskEntity is a representation a subtask.
 * A subtask is a step of a bigger task.
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
public class SubtaskEntity extends BaseEntity<SubtaskEntity>{
	
	/**
	 * The task the subtask belongs to.
	 */
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "task_id", referencedColumnName = "id")
	private TaskEntity task = null;
	public static final String TASK = "task";
	public TaskEntity getTask() { return this.task; }
	public void setTask(TaskEntity task) { this.task = task; }
	
	/**
	 * The state of the subtask.
	 */
	@Column(name = "state", nullable = false)
	private int state = 0;
	public static final String STATE = "state";
	public int getState() { return this.state; }
	public void setState(int state) { this.state = state; }
	
	/**
	 * The text of the subtask.
	 */
	@Column(name = "text", nullable = false, columnDefinition="TEXT")
	private String text = "";
	public static final String TEXT = "text";
	public String getText() { return this.text; }
	public void setText(String text) { this.text = text; }
	
	/**
	 * If the subtask is required to complete the task.
	 */
	@Column(name = "isRequired", nullable = false)
	private boolean isRequired = false;
	public static final String IS_REQUIRED = "isRequired";
	public boolean getIsRequired() { return this.isRequired; }
	public void setIsRequired(boolean isRequired) { this.isRequired = isRequired; }

	@Override
	public void applyPatch(SubtaskEntity details) {
		
		TaskEntity task = details.getTask();
		int state = details.getState();
		String text = details.getText();
		boolean isRequired = details.getIsRequired();
		
		if (task != null) {
			this.setTask(task);
		}
		if (state != 0 || this.getState() != 0) {
			this.setState(state);
		}
		if (text != "" || this.getText() != "") {
			this.setText(text);
		}
		this.setIsRequired(isRequired);

	}

}
