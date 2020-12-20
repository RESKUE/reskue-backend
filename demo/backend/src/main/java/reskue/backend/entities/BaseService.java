package reskue.backend.entities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

public class BaseService<E extends BaseEntity<E>, R extends JpaRepository<E, Long>> {

	@Autowired
	protected R repository;
	
	public List<E> getAll() {
		
		return repository.findAll();
		
	}
	
	public E findById(long id) throws ResourceNotFoundException {
		
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found: " + id));
		
	}
	
	public E create(E entity) {
		
		return repository.save(entity);
		
	}
	
	public E update(long id, E details) throws ResourceNotFoundException {
		
		E entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found: " + id));
		entity.applyPatch(details);
		final E updatedEntity = repository.save(entity);
		return updatedEntity;
		
	}
	
	public E delete(long id) throws ResourceNotFoundException {
		
		E entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found: " + id));
		repository.delete(entity);
		return entity;
		
	}
	
}
