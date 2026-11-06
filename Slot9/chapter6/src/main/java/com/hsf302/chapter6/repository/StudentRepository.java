package com.hsf302.chapter6.repository;

import com.hsf302.chapter6.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    // major là LAZY và open-in-view=false → phải nạp sẵn cùng Student,
    // nếu không view gọi student.major.code sẽ ném LazyInitializationException.
    @Override
    @EntityGraph(attributePaths = "major")
    Page<Student> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "major")
    Optional<Student> findById(Long id);

    // Tìm theo tên HOẶC email; Pageable đã chứa cả Sort nên dùng chung được cho
    // tìm kiếm + phân trang + sắp xếp.
    @EntityGraph(attributePaths = "major")
    Page<Student> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String nameKey, String emailKey, Pageable pageable);
}