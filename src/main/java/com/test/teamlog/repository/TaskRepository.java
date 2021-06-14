package com.test.teamlog.repository;

import com.test.teamlog.entity.Project;
import com.test.teamlog.entity.Task;
import com.test.teamlog.entity.TaskStatus;
import com.test.teamlog.entity.TeamFollower;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    <S extends Task> S save(S entity);
    void delete(Task entity);

    List<Task> findByProject(Project project, Sort sort);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project = :project AND t.status = :status")
    Integer getCountByPostAndStatus(@Param("project") Project project, @Param("status") TaskStatus status);

    @Modifying
    @Transactional
    @Query("update Task t set t.priority = t.priority+1 where t.project = :project and t.status = :status and t.priority between :target and :priority-1")
    void reorderFrontInSameStatus(@Param("project") Project project, @Param("status") TaskStatus status,
                                  @Param("priority") int priority, @Param("target") int target_priority);
    @Modifying
    @Transactional
    @Query("update Task t set t.priority = t.priority-1 where t.project = :project and t.status = :status and t.priority between :priority+1 and :target")
    void reorderBackInSameStatus(@Param("project") Project project, @Param("status") TaskStatus status,
                                 @Param("priority") int priority, @Param("target") int target_priority);

    @Modifying
    @Transactional
    @Query("update Task t set t.priority = t.priority-1 where t.project = :project and t.status = :status and t.priority > :priority")
    void reorderInPreviousStatus(@Param("project") Project project, @Param("status") TaskStatus status, @Param("priority") int priorit);

    @Modifying
    @Transactional
    @Query("update Task t set t.priority = t.priority+1 where t.project = :project and t.status = :status and t.priority >= :target")
    void reorderInNewStatus(@Param("project") Project project, @Param("status") TaskStatus status, @Param("target") int target);
}
