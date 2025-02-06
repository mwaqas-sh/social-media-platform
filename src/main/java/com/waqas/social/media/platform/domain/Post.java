package com.waqas.social.media.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 9042714210019954181L;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}


