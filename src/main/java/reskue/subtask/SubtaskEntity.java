package reskue.subtask;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kueres.base.BaseEntity;
import reskue.task.TaskEntity;

@Entity
public class SubtaskEntity extends BaseEntity<SubtaskEntity>{
	
	@ManyToOne
	@JoinColumn(name = "subtask_id", referencedColumnName = "id")
	private TaskEntity task = null;
	public static final String TASK = "task";
	public TaskEntity getTask() { return this.task; }
	public void setTask(TaskEntity task) { this.task = task; }
	
	@Column(name = "state", nullable = false)
	private int state = 0;
	public static final String STATE = "state";
	public int getState() { return this.state; }
	public void setState(int state) { this.state = state; }
	
	@Column(name = "text", nullable = false, columnDefinition="TEXT")
	private String text = "";
	public static final String TEXT = "text";
	public String getText() { return this.text; }
	public void setText(String text) { this.text = text; }
	
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
		this.setState(state);
		if (text != null) {
			this.setText(text);
		}
		this.setIsRequired(isRequired);
		
	}

}
