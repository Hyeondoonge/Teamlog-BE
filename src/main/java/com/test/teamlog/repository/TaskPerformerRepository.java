package com.test.teamlog.repository;

import com.test.teamlog.entity.Task;
import com.test.teamlog.entity.TaskPerformer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPerformerRepository extends JpaRepository<TaskPerformer, Long> {
    <S extends TaskPerformer> S save(S entity);
    void delete(TaskPerformer entity);
}