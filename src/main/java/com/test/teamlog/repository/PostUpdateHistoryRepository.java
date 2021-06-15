package com.test.teamlog.repository;

import com.test.teamlog.entity.Post;
import com.test.teamlog.entity.PostUpdateHistory;
import com.test.teamlog.entity.ProjectFollower;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostUpdateHistoryRepository extends JpaRepository<PostUpdateHistory, Long> {
    List<PostUpdateHistory> findAllByPost(Post post, Sort sort);
}
