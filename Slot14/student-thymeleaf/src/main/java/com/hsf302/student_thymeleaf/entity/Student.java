package com.hsf302.student_thymeleaf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;

// TODO 18: Ánh xạ entity với bảng SQL Server
@Entity
@Table(name = "Students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO 1: Thêm validation
    @NotBlank(message = "Mã sinh viên không được để trống")
    @Pattern(
            regexp = "^(SE|SS|SA|HE)\\d{6}$",
            message = "Mã SV phải có dạng SE/SS/SA/HE + 6 chữ số (VD: SE180001)"
    )
    @Column(name = "StudentCode", length = 8, nullable = false, unique = true)
    private String studentCode;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 2, max = 50, message = "Họ tên phải từ 2 đến 50 ký tự")
    @Nationalized
    @Column(name = "FullName", length = 50, nullable = false)
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Column(name = "Email", length = 100, nullable = false)
    private String email;

    // TODO 2: Thêm validation
    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DateOfBirth", nullable = false)
    private LocalDate dateOfBirth;

    @NotNull(message = "GPA không được để trống")
    @Min(value = 0, message = "GPA tối thiểu là 0.0")
    @Max(value = 10, message = "GPA tối đa là 10.0")
    @Column(name = "Gpa", nullable = false)
    private Double gpa;

    @NotBlank(message = "Vui lòng chọn ngành")
    @Column(name = "Major", length = 2, nullable = false)
    private String major;

    @Column(name = "Active", nullable = false)
    private boolean active = true;

    public Student() {
    }

    public Student(Long id, String studentCode, String fullName, String email,
                   LocalDate dateOfBirth, Double gpa, String major, boolean active) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gpa = gpa;
        this.major = major;
        this.active = active;
    }

    // TODO 3: Cài đặt 2 thuộc tính tính toán
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public String getRank() {
        if (gpa == null) {
            return "Chưa xếp loại";
        }
        if (gpa >= 9.0) {
            return "Xuất sắc";
        } else if (gpa >= 8.0) {
            return "Giỏi";
        } else if (gpa >= 6.5) {
            return "Khá";
        } else if (gpa >= 5.0) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }

    // TODO 0: Generate Getters & Setters cho tất cả field
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}