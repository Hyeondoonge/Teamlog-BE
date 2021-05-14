package com.test.teamlog.repository;

import com.test.teamlog.entity.Project;
import com.test.teamlog.entity.ProjectMember;
import com.test.teamlog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByUser(User user);
    List<ProjectMember> findByProject(Project project);
    Optional<ProjectMember> findByProjectAndUser(Project project, User user);

}
