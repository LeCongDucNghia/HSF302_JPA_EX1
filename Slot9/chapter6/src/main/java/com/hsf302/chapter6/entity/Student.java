package com.hsf302.chapter6.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gpa", nullable = false)
    private Double gpa;

    // Chuyên ngành là entity riêng (bảng majors). LAZY → khi cần hiển thị phải
    // nạp sẵn bằng @EntityGraph trong repository (vì open-in-view=false).
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    /** JPA bắt buộc có constructor không tham số */
    public Student() {}

    public Student(String name, String email, Integer age, Major major, Double gpa) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.major = major;
        this.gpa = gpa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }

    public Major getMajor() { return major; }
    public void setMajor(Major major) { this.major = major; }

    @Override
    public String toString() {
        // Không in major để tránh kích hoạt lazy loading ngoài transaction
        return "Student{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}