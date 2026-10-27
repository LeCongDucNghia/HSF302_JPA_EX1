package com.hsf302.student_thymeleaf.service;

import com.hsf302.student_thymeleaf.entity.Student;
import com.hsf302.student_thymeleaf.repository.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll(Sort.by("id"));
    }

    public List<Student> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        String kw = keyword.trim();
        return studentRepository
                .findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrderByIdAsc(kw, kw);
    }

    // [CÂU 1]: Hàm tìm kiếm có hỗ trợ sắp xếp linh hoạt
    public List<Student> search(String keyword, String sortField, String dir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String field = (sortField != null && !sortField.isBlank()) ? sortField : "id";
        Sort sort = Sort.by(direction, field);

        if (keyword == null || keyword.isBlank()) {
            return studentRepository.findAll(sort);
        }
        String kw = keyword.trim();
        return studentRepository
                .findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCase(kw, kw, sort);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    public boolean isStudentCodeDuplicated(String code, Long excludeId) {
        if (code == null || code.isBlank()) return false;
        String c = code.trim();
        return excludeId == null
                ? studentRepository.existsByStudentCodeIgnoreCase(c)
                : studentRepository.existsByStudentCodeIgnoreCaseAndIdNot(c, excludeId);
    }

    public double averageGpa(List<Student> list) {
        return list.stream()
                .map(Student::getGpa)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
}
