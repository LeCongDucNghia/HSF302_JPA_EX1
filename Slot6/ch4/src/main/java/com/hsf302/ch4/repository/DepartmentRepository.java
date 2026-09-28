package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Department;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByCode(String code);        // dùng lại ở TODO 16, 22
    List<Department> findByStudentsIsEmpty();
}
