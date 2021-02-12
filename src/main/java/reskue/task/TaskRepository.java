package reskue.task;

import org.springframework.stereotype.Repository;

import reskue.ReskueRepository;

@Repository
public interface TaskRepository extends ReskueRepository<TaskEntity>{

}
