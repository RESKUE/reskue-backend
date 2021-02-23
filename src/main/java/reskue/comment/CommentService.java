package reskue.comment;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kueres.base.BaseService;
import kueres.media.MediaEntity;
import kueres.query.EntitySpecification;
import kueres.utility.Utility;

@Service
public class CommentService extends BaseService<CommentEntity, CommentRepository>{
	
	@PersistenceContext
	private EntityManager em;

	@Override
	@PostConstruct
	public void init() {

	}

	@SuppressWarnings("unchecked")
	public Page<MediaEntity> getAllMedia(long id, EntitySpecification<MediaEntity> specification, Pageable pageable) {
		
		Utility.LOG.trace("CommentService.getAllMedia called.");

		CommentEntity entity = this.findById(id);
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

		return page;
		
	}

}
