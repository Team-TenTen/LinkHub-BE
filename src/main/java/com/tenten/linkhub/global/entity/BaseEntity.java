package com.tenten.linkhub.global.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends BaseTimeEntity {
    protected boolean isDeleted;

    @PrePersist
    public void prePersist() {
        isDeleted = false;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void changeIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
