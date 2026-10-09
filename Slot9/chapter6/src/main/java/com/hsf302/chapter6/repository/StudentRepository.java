package com.hsf302.chapter6.repository;

import com.hsf302.chapter6.entity.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Email đã tồn tại? (dùng khi thêm mới)
    boolean existsByEmailIgnoreCase(String email);

    // Email đã được sinh viên KHÁC dùng? (dùng khi cập nhật)
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    // Tìm kiếm theo tên HOẶC email có chứa keyword
    List<Student> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String nameKey, String emailKey, Sort sort);
}