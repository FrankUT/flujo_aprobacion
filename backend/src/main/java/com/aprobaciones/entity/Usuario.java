package com.aprobaciones.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(name = "user_id")
    private String userId;

     @Column(name = "user_name")
    private String userName;
     @Column(name = "full_name")
    private String fullName;
}
