package com.hyunshop.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseInfoEntity implements Serializable {

    @Column(name = "created_time")
    private Long createdTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_time")
    private Long updatedTime;

    @Column(name = "updated_by")
    private String updatedBy;

}
