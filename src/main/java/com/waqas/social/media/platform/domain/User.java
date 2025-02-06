package com.waqas.social.media.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})})
@JsonIgnoreProperties("hibernateLazyInitializer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 9042714210019954181L;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String profilePicture;

    @Column(nullable = true)
    private String bio;


    @Transient
    UserToken userToken;

    @Column(nullable = false)
    @JsonProperty("isActive")
    private boolean isActive;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_account_id")
    private List<UserToken> userTokens;

    @Transient
    private String errorMessage;

}
