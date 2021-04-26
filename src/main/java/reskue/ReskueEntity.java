package reskue;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import kueres.base.BaseEntity;
import kueres.media.MediaEntity;
import reskue.comment.CommentEntity;

/**
 * 
 * The ReskueEntity provides different entities with generic fields.
 * The ReskueEntity is used as a generic superclass for the CulturalAssetEntity and TaskEntity.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0.0
 * @since Apr 26, 2021
 *
 */

@MappedSuperclass
public abstract class ReskueEntity<E extends ReskueEntity<E>> extends BaseEntity<E> {
	
	/**
	 * The name of the entity.
	 */
	@Column(name = "name", nullable = false)
	private String name = "unnamed";
	public static final String NAME = "name";
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	/**
	 * The description of the entity.
	 */
	@Column(name = "description", nullable = true, columnDefinition="TEXT")
	private String description = null;
	public static final String DESCRIPTION = "description";
	public String getDescription() { return this.description; }
	public void setDescription(String description) { this.description = description; }
	
	/**
	 * The priority of the entity. The priority should range between 0 and 5.
	 */
	@Column(name = "priority", nullable = false)
	private int priority = 0;
	public static final String PRIORITY = "priority";
	public int getPriority() { return this.priority; }
	public void setPriority(int priority) { this.priority = priority; }
	
	/**
	 * If the entity is endangered. 
	 * Uses an integer instead of a boolean to make sorting easier.
	 * Use 1 for true and 0 for false.
	 */
	@Column(name = "isEndangered", nullable = false)
	private int isEndangered = 0;
	public static final String IS_ENDANGERED = "isEndangered";
	public int getIsEndangered() { return this.isEndangered; }
	public void setIsEndangered(int isEndangered) { this.isEndangered = isEndangered; }
	
	public abstract List<MediaEntity> getMedia();
	public abstract void setMedia(List<MediaEntity> media);
	
	public abstract List<CommentEntity> getComments();
	public abstract void setComments(List<CommentEntity> comments);
	
}
