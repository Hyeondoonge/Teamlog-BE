package com.test.teamlog.repository;

import com.test.teamlog.entity.User;
import com.test.teamlog.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    <S extends UserFollow> S save(S entity);
    void delete(UserFollow entity);
    Optional<UserFollow> findByFromUserAndToUser(User user, User targetUser);
    List<UserFollow> findByFromUser(User user);
}
