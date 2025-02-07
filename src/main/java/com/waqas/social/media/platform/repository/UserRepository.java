package com.waqas.social.media.platform.repository;

import com.waqas.social.media.platform.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByEmail(String email);

    @Query(value = "Select * from user WHERE id =:userid", nativeQuery = true)
    User findByUserId(Long userid);

    Page<User> findByUserNameContainsOrEmailContainsOrBioContains(String userName, String email, String bio, Pageable pageable);

}
