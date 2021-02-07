package reskue.subtask;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import kueres.base.BaseEntity;
import reskue.task.TaskEntity;

public class SubtaskEntity extends BaseEntity<SubtaskEntity>{
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Column(name = "task", nullable = false)
	private TaskEntity task;
	public static final String TASK = "task";
	public TaskEntity getTask() { return this.task; }
	public void setTask(TaskEntity task) { this.task = task; }
	
	@Column(name = "state", nullable = false)
	private int state = 0;
	public static final String STATE = "state";
	public int getState() { return this.state; }
	public void setState(int state) { this.state = state; }
	
	@Column(name = "text", nullable = false)
	private String text;
	public static final String TEXT = "text";
	public String getText() { return this.text; }
	public void setText(String text) { this.text = text; }
	
	@Column(name = "isRequired", nullable = false)
	private boolean isRequired;
	public static final String IS_REQUIRED = "isRequired";
	public boolean getIsRequired() { return this.isRequired; }
	public void setIsRequired(boolean isRequired) { this.isRequired = isRequired; }

	@Override
	public void applyPatch(SubtaskEntity details) {
		// TODO Auto-generated method stub
		
	}

}
