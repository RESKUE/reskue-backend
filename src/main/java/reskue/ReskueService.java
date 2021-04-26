package reskue;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import kueres.base.BaseService;
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.media.MediaEntity;
import kueres.media.MediaService;
import kueres.query.EntitySpecification;
import kueres.utility.Utility;

/**
 * 
 * The ReskueService provides services needed by the ReskueController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0.0
 * @since Apr 26, 2021
 *
 */

public abstract class ReskueService<E extends ReskueEntity<E>, R extends ReskueRepository<E>>
		extends BaseService<E, R> {
	
	/**
	 * The MediaService needed to perform operations on related media entities.
	 */
	@Autowired
	protected MediaService mediaService;
	
	/**
	 * Get all media of the services ReskueEntity-type.
	 * 
	 * @param id - the entity's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	public Page<MediaEntity> getAllMedia(Long id, EntitySpecification<MediaEntity> specification, Pageable pageable) {
		
		Utility.LOG.trace("ReskueService.getAllMedia called.");

		E entity = this.findById(id);
		
		List<MediaEntity> media = entity.getMedia();
		
		if (specification != null) {
			
			media = media.stream().filter(specification.toPredicate(MediaEntity.class)).collect(Collectors.toList());
			
		}

		Page<MediaEntity> page = new PageImpl<MediaEntity>(media, pageable, media.size());
		
		EventConsumer.sendEvent("ReskueService.getAllMedia", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}
	
}
