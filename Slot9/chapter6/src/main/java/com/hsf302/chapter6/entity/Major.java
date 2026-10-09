package com.hsf302.chapter6.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "majors")
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code; // CNTT, KTPM...

    @Column(nullable = false, length = 100)
    private String name; // Công nghệ thông tin...

    public Major() {}
    public Major(String code, String name) { this.code = code; this.name = name; }

    // Getters & Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}