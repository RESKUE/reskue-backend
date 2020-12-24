package reskue.backend.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository<E extends BaseEntity<E>> extends JpaRepository<E, Long>{

}
