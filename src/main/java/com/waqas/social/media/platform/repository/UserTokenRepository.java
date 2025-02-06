package com.waqas.social.media.platform.repository;

import com.waqas.social.media.platform.domain.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    UserToken findBySessionTokenAndActiveTrue(String sessionToken);

    @Query(value = "SELECT ep.is_allowed FROM user_token ut INNER JOIN user_account ua ON ut.user_account_id=ua.id INNER JOIN endpoint ep ON ua.account_type_id=ep.account_type_id WHERE ut.session_token=:sessionToken AND ep.url LIKE :url" + "% LIMIT 1", nativeQuery = true)
    boolean isEndpointAllowedForUser(String sessionToken, String url);
}
