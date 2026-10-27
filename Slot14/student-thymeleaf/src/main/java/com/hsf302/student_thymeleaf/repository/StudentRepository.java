package com.hsf302.student_thymeleaf.repository;

import com.hsf302.student_thymeleaf.entity.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // [CÂU 1 + 2]: Tìm kiếm theo từ khóa + Lọc ngành + Sắp xếp động
    @Query("SELECT s FROM Student s WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            " OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:major IS NULL OR :major = '' OR s.major = :major)")
    List<Student> searchAndFilter(@Param("keyword") String keyword,
                                  @Param("major") String major,
                                  Sort sort);

    boolean existsByStudentCodeIgnoreCase(String studentCode);

    boolean existsByStudentCodeIgnoreCaseAndIdNot(String studentCode, Long id);
}
