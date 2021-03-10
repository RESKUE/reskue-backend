package reskue;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import kueres.base.BaseService;
import kueres.event.EventType;
import kueres.eventbus.EventConsumer;
import kueres.media.MediaEntity;
import kueres.query.EntitySpecification;

/**
 * 
 * The ReskueService provides services needed by the ReskueController.
 *
 * @author Jan Strassburg, jan.strassburg@student.kit.edu
 * @version 1.0
 * @since Feb 25, 2021
 *
 */

public abstract class ReskueService<E extends ReskueEntity<E>, R extends ReskueRepository<E>>
		extends BaseService<E, R> {
	
	/**
	 * The EntityManager needed to create a CriteriaBuilder.
	 */
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Get all media of the services ReskueEntity-type.
	 * 
	 * @param id - the entity's identifier.
	 * @param specification - filter for the result.
	 * @param pageable - sort and pagination for the result.
	 * @return The result as a page.
	 */
	@SuppressWarnings("unchecked")
	public Page<MediaEntity> getAllMedia(long id, EntitySpecification<MediaEntity> specification, Pageable pageable) {

		E entity = this.findById(id);

		List<MediaEntity> media = entity.getMedia();
		
		if (specification != null) {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<MediaEntity> criteriaQuery = criteriaBuilder.createQuery(MediaEntity.class);
			Root<MediaEntity> root = criteriaQuery.from(MediaEntity.class);

			media = media.stream().filter(
					(Predicate<? super MediaEntity>) specification.toPredicate(root, criteriaQuery, criteriaBuilder))
					.collect(Collectors.toList());
		}

		Page<MediaEntity> page = new PageImpl<MediaEntity>(media, pageable, media.size());
		
		EventConsumer.sendEvent("ReskueService.getAllMedia", EventType.READ.type, this.getIdentifier(), EventConsumer.writeObjectAsJSON(page));

		return page;
		
	}
	
}
