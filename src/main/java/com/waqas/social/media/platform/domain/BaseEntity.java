package com.waqas.social.media.platform.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;

/**
 * Base class for all entities
 *
 * @param <T> Generic Parameter
 */

@Data
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public abstract class BaseEntity<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1608760034742763124L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    @CreatedDate
    private long createdDate;

    @LastModifiedDate
    private long lastModifiedDate;
}
