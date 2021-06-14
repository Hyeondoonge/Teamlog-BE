package com.test.teamlog.repository;

import com.test.teamlog.entity.Post;
import com.test.teamlog.entity.PostLiker;
import com.test.teamlog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikerRepository extends JpaRepository<PostLiker, Long> {
    <S extends PostLiker> S save(S entity);
    void delete(PostLiker entity);

    Optional<PostLiker> findByPostAndUser(Post post, User user);
    List<PostLiker> findAllByPost(Post post);
}
