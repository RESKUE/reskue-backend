package reskue.comment;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import kueres.base.BaseService;
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.media.MediaEntity;
import kueres.media.MediaService;
import kueres.query.EntitySpecification;
import kueres.utility.Utility;

/**
 * 
 * The CommentService provides services needed by the CommentController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Mar 25, 2021
 *
 */

@Service
public class CommentService extends BaseService<CommentEntity, CommentRepository>{
	
	@Autowired
	private MediaService mediaService;
	
	/**
	 * Set this EventSubscribers identifier and routing.
	 */
	@Override
	@PostConstruct
	public void init() {
		this.identifier = CommentController.ROUTE;
		this.routingKey = CommentController.ROUTE;
	}
	
	/**
	 * Delete a comment.
	 * 
	 * @param id - the comments identifier.
	 * @return The comment that was deleted.
	 * @throws ResourceNotFoundException if there is no comment with the
	 *                                   specified identifier.
	 */
	@Override
	public CommentEntity delete(Long id) throws ResourceNotFoundException {

		Utility.LOG.trace("CommentService.delete called.");

		CommentEntity entity = this.repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		for (Iterator<MediaEntity> iterator = entity.getMedia().iterator(); iterator.hasNext();) {
			MediaEntity media = iterator.next();
			iterator.remove();
			mediaService.delete(media.getId());
		}
		
		this.repository.delete(entity);

		EventConsumer.sendEvent("CommentService.delete", EventType.DELETE.type, this.getIdentifier(),
				EventConsumer.writeObjectAsJSON(entity));

		return entity;

	}
	
	/**
	 * Get all media of the comment.
	 * 
	 * @param id - the entity's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	public Page<MediaEntity> getAllMedia(Long id, EntitySpecification<MediaEntity> specification, Pageable pageable) {
		
		Utility.LOG.trace("CommentService.getAllMedia called.");

		CommentEntity entity = this.findById(id);
		List<MediaEntity> media = entity.getMedia();

		if (specification != null) {

			media = media.stream().filter(specification.toPredicate(MediaEntity.class)).collect(Collectors.toList());
			
		}

		Page<MediaEntity> page = new PageImpl<MediaEntity>(media, pageable, media.size());
		
		EventConsumer.sendEvent("CommentService.getAllMedia", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));		

		return page;
		
	}

}
