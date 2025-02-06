package com.waqas.social.media.platform.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "follow")
public class Follow extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 9042714210019954181L;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User followed;
}
