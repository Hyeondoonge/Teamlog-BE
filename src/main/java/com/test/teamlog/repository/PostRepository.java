package com.test.teamlog.repository;

import com.test.teamlog.entity.User;
import com.test.teamlog.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    <S extends Post> S save(S entity);
    void delete(Post entity);

    List<Post> findAllByProjectAndWriter(Project project, User user);

    @Query("SELECT p FROM Post p Where p.project = :project and p.location is not null")
    List<Post> findAllPostsWithLocationByProject(@Param("project") Project project);

    @Query("SELECT p FROM Post p Where p.project = :project and p.location is not null and p.accessModifier = :access")
    List<Post> findAllPostsWithLocationByProject(@Param("project") Project project, @Param("access") AccessModifier access);

    List<Post> findAllByWriter(User user);

    @Query("SELECT p FROM Post p Where p.writer in (:following)")
    List<Post> findAllByWriters(@Param("following") List<User> following);

    // 카운트
    @Query("SELECT COUNT(p) FROM Post p Where p.project = :project")
    long getPostsCount(@Param("project") Project project);

    @Query("SELECT COUNT(p) FROM Post p Where p.project = :project and p.accessModifier = :access")
    long getPostsCount(@Param("project") Project project, @Param("access") AccessModifier access);

    @Query("SELECT p FROM Post p Where p.project = :project")
    Slice<Post> findAllByProject(@Param("project") Project project, Pageable pageable);

    @Query("SELECT p FROM Post p Where p.accessModifier = :access and p.project = :project")
    Slice<Post> findAllByProject(@Param("project") Project project, @Param("access") AccessModifier access, Pageable pageable);

    @Query("SELECT p FROM Post p Where p.project = :project AND ((:cop = '<' and  p.id < :cursor) or (:cop = '>' and  p.id > :cursor))")
    Slice<Post> findAllByProjectAndCursor(@Param("project") Project project, @Param("cursor") Long cursor,
                                                 @Param("cop") String cop, Pageable pageable);

    @Query("SELECT p FROM Post p Where p.accessModifier = :access and p.project = :project " +
            "AND ((:cop = '<' and  p.id < :cursor) or (:cop = '>' and  p.id > :cursor))")
    Slice<Post> findAllByProjectAndCursor(@Param("project") Project project, @Param("cursor") Long cursor,
                                                 @Param("cop") String cop, @Param("access") AccessModifier access, Pageable pageable);

    List<Post> findAllByLocationIsNotNullAndAccessModifier(AccessModifier accessModifier);

    // 카운트
    @Query("SELECT COUNT(p) FROM Post p WHERE (p.contents LIKE concat('%',:keyword,'%') AND p.project = :project)")
    long getPostsCountByKeyword(@Param("project") Project project, @Param("keyword") String keyword);

    @Query("SELECT COUNT(p) FROM Post p WHERE (p.contents LIKE concat('%',:keyword,'%') AND p.project = :project and p.accessModifier=:access)")
    long getPostsCountByKeyword(@Param("project") Project project, @Param("keyword") String keyword, @Param("access") AccessModifier access);

    @Query("SELECT p FROM Post p WHERE (p.contents LIKE concat('%',:keyword,'%') AND p.project = :project)")
    Slice<Post> searchPostsInProject(@Param("project") Project project, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (p.contents LIKE concat('%',:keyword,'%') AND p.project = :project and p.accessModifier = :access)")
    Slice<Post> searchPostsInProject(@Param("project") Project project, @Param("keyword") String keyword,
                                                  @Param("access") AccessModifier access, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.project = :project AND ((:cop = '<' and  p.id < :cursor) or (:cop = '>' and  p.id > :cursor))" +
            "AND (p.contents LIKE concat('%',:keyword,'%'))")
    Slice<Post> searchPostsInProjectByCursor(@Param("project") Project project, @Param("cursor") Long cursor,
                                                    @Param("keyword") String keyword, @Param("cop") String cop, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.accessModifier = :access and p.project = :project " +
            "AND ((:cop = '<' and  p.id < :cursor) or (:cop = '>' and  p.id > :cursor))" +
            "AND (p.contents LIKE concat('%',:keyword,'%'))")
    Slice<Post> searchPostsInProjectByCursor(@Param("project") Project project, @Param("cursor") Long cursor,
                                                          @Param("keyword") String keyword, @Param("cop") String cop,
                                                          @Param("access") AccessModifier access, Pageable pageable);

    // 카운트
    @Query("SELECT p FROM Post p, PostTag h WHERE h.post = p AND h.name IN (:names) AND p.project = :project GROUP BY p.id")
    Page<Post> getPostsCountByHashTag(@Param("project") Project project, @Param("names") List<String> names, Pageable pageable);

    @Query("SELECT p FROM Post p, PostTag h WHERE p.accessModifier = :access and h.post = p AND h.name IN (:names) AND p.project = :project GROUP BY p.id")
    Page<Post> getPostsCountByHashTag(@Param("project") Project project, @Param("names") List<String> names, @Param("access") AccessModifier access, Pageable pageable);

    @Query("SELECT p FROM Post p, PostTag h WHERE h.post = p AND h.name IN (:names) AND p.project = :project GROUP BY p.id")
    Slice<Post> getPostsInProjectByHashTag(@Param("project") Project project, @Param("names") List<String> names, Pageable pageable);

    @Query("SELECT p FROM Post p, PostTag h WHERE p.accessModifier = :access and h.post = p AND h.name IN (:names) AND p.project = :project GROUP BY p.id")
    Slice<Post> getPostsInProjectByHashTag(@Param("project") Project project, @Param("names") List<String> names,
                                                  @Param("access") AccessModifier accees, Pageable pageable);


    @Query("SELECT p FROM Post p, PostTag h WHERE p.project = :project AND ((:cop = '<' and  p.id < :cursor) or (:cop = '>' and  p.id > :cursor)) " +
            "AND h.post = p AND h.name IN (:names) GROUP BY p.id")
    Slice<Post> getPostsInProjectByHashTagAndCursor(@Param("project") Project project, @Param("cursor") Long cursor,
                                                           @Param("names") List<String> names, @Param("cop") String cop, Pageable pageable);

    @Query("SELECT p FROM Post p, PostTag h WHERE p.accessModifier = :access and p.project = :project AND ((:cop = '<' and  p.id < :cursor) or (:cop = '>' and  p.id > :cursor)) " +
            "AND h.post = p AND h.name IN (:names) GROUP BY p.id")
    Slice<Post> getPostsInProjectByHashTagAndCursor(@Param("project") Project project, @Param("cursor") Long cursor,
                                                           @Param("names") List<String> names, @Param("cop") String cop,
                                                           @Param("access") AccessModifier access, Pageable pageable);
    // 카운트
    @Query("SELECT p FROM Post p, PostTag h WHERE h.post = p AND h.name IN (:names) AND (p.contents LIKE concat('%',:keyword,'%') AND p.project = :project) GROUP BY p.id")
    Page<Post> getPostsCountByHashtagAndKeyword(@Param("project") Project project, @Param("names") List<String> names,
                                                       @Param("keyword") String keyword, Pageable pageable);

    // 카운트
    @Query("SELECT p FROM Post p, PostTag h WHERE p.accessModifier =:access and h.post = p AND h.name IN (:names) AND (p.contents LIKE concat('%',:keyword,'%') AND p.project = :project) GROUP BY p.id")
    Page<Post> getPostsCountByHashtagAndKeyword(@Param("project") Project project, @Param("names") List<String> names,
                                                       @Param("keyword") String keyword, @Param("access") AccessModifier access, Pageable pageable);

    @Query("SELECT p FROM Post p, PostTag h WHERE h.post = p AND h.name IN (:names) AND (p.contents LIKE concat('%',:keyword,'%') AND p.project = :project) GROUP BY p.id")
    Slice<Post> searchPostsInProjectByHashtagAndKeyword(@Param("project") Project project, @Param("names") List<String> names,
                                                               @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p, PostTag h WHERE p.accessModifier = :access and h.post = p " +
            "and h.name IN (:names) AND (p.contents LIKE concat('%',:keyword,'%') AND p.project = :project) GROUP BY p.id")
    Slice<Post> searchPublicPostsInProjectByHashtagAndKeyword(@Param("project") Project project, @Param("names") List<String> names,
                                                                     @Param("keyword") String keyword, @Param("access") AccessModifier access, Pageable pageable);

    @Query("SELECT p FROM Post p, PostTag h WHERE p.project = :project AND ((:cop = '<' and  p.id < :cursor) or (:cop = '>' and  p.id > :cursor)) " +
            "AND h.post = p AND h.name IN (:names) AND (p.contents LIKE concat('%',:keyword,'%')) GROUP BY p.id")
    Slice<Post> searchPostsInProjectByHashtagAndKeywordAndCursor(@Param("project") Project project, @Param("cursor") Long cursor,
                                                                        @Param("names") List<String> names, @Param("keyword") String keyword,
                                                                        @Param("cop") String cop, Pageable pageable);

    @Query("SELECT p FROM Post p, PostTag h WHERE p.accessModifier = :access and p.project = :project " +
            "AND ((:cop = '<' and  p.id < :cursor) or (:cop = '>' and  p.id > :cursor)) " +
            "AND h.post = p AND h.name IN (:names) AND (p.contents LIKE concat('%',:keyword,'%')) GROUP BY p.id")
    Slice<Post> searchPublicPostsInProjectByHashtagAndKeywordAndCursor(@Param("project") Project project, @Param("cursor") Long cursor,
                                                                              @Param("names") List<String> names, @Param("keyword") String keyword,
                                                                              @Param("cop") String cop, @Param("access") AccessModifier access, Pageable pageable);

}
