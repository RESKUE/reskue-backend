package reskue.subtask;

import java.lang.reflect.InvocationTargetException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import kueres.base.BaseEntity;
import reskue.task.TaskEntity;

/**
 * 
 * The SubtaskEntity is a representation a subtask.
 * A subtask is a step of a bigger task.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Mar 25, 2021
 *
 */

@Entity
public class SubtaskEntity extends BaseEntity<SubtaskEntity>{
	
	@Override
	public String[] getUpdateableFields() {
		return new String[] {
			SubtaskEntity.TASK,
			SubtaskEntity.STATE,
			SubtaskEntity.TEXT,
			SubtaskEntity.IS_REQUIRED
		};
	}
	
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
	
	/**
	 * The state of the subtask.
	 */
	@Column(name = "state", nullable = false)
	private int state = 0;
	public static final String STATE = "state";
	public int getState() { return this.state; }
	public void setState(int state) { this.state = state; }
	
	/**
	 * The task the subtask belongs to.
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = BaseEntity.ID)
	@JsonIdentityReference(alwaysAsId = true)
	private TaskEntity task = null;
	public static final String TASK = "task";
	public TaskEntity getTask() { return this.task; }
	public void setTask(TaskEntity task) { this.task = task; }

	@Override
	public void applyPatch(String json) throws JsonMappingException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, JsonProcessingException {
		
		SubtaskEntity details = SubtaskEntity.createEntityFromJSON(json, this.getUpdateableFields(), SubtaskEntity.class);
		
		if (this.containsFields(json, SubtaskEntity.TASK)) {
			this.setTask(details.getTask());
		}
		
		if (this.containsFields(json, SubtaskEntity.STATE)) {
			this.setState(details.getState());
		}
		
		if (this.containsFields(json, SubtaskEntity.TEXT)) {
			this.setText(details.getText());
		}
		
		this.setIsRequired(isRequired);

	}

}
