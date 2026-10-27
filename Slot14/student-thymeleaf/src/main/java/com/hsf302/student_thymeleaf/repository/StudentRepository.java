package com.hsf302.student_thymeleaf.repository;

import com.hsf302.student_thymeleaf.entity.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Tìm kiếm cơ bản mặc định
    List<Student> findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrderByIdAsc(String code, String name);

    // [CÂU 1]: Tìm kiếm kết hợp Sort động theo cột và chiều (asc/desc)
    List<Student> findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCase(String code, String name, Sort sort);

    // Kiểm tra trùng mã sinh viên
    boolean existsByStudentCodeIgnoreCase(String studentCode);

    // Kiểm tra trùng mã sinh viên khi cập nhật (bỏ qua id hiện tại)
    boolean existsByStudentCodeIgnoreCaseAndIdNot(String studentCode, Long id);
}
