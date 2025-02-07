package com.waqas.social.media.platform.repository;

import com.waqas.social.media.platform.domain.Follow;
import com.waqas.social.media.platform.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("SELECT f.follower FROM Follow f WHERE f.followed.id = :followedId")
    List<User> findFollowersByFollowedId(@Param("followedId") Long followedId);

    @Query("SELECT f.followed FROM Follow f WHERE f.follower.id = :followerId")
    List<User> findFollowedByFollowerId(@Param("followerId") Long followerId);
}
