package com.hsf302.chapter6.service;

import com.hsf302.chapter6.dto.StudentForm;
import com.hsf302.chapter6.entity.Major;
import com.hsf302.chapter6.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    /** Tìm theo tên/email (keyword rỗng = lấy tất cả), có phân trang + sắp xếp. */
    Page<Student> search(String keyword, Pageable pageable);

    Optional<Student> findById(Long id);

    /** Lấy dữ liệu để đổ vào form sửa. */
    Optional<StudentForm> findFormById(Long id);

    Student create(StudentForm form);

    // @return true nếu tìm thấy và cập nhật; false nếu không tồn tại id
    boolean update(Long id, StudentForm form);

    // @return true nếu xoá được; false nếu không tồn tại id
    boolean delete(Long id);

    // excludeId = null khi thêm mới, = id hiện tại khi cập nhật
    boolean isEmailTaken(String email, Long excludeId);

    List<Major> getMajors();
}
