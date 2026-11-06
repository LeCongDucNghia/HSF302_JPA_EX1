package com.hsf302.chapter6.service.impl;

import com.hsf302.chapter6.dto.StudentForm;
import com.hsf302.chapter6.entity.Major;
import com.hsf302.chapter6.entity.Student;
import com.hsf302.chapter6.repository.MajorRepository;
import com.hsf302.chapter6.repository.StudentRepository;
import com.hsf302.chapter6.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)          // mặc định: mọi method chỉ đọc
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final MajorRepository majorRepository;

    public StudentServiceImpl(StudentRepository studentRepository, MajorRepository majorRepository) {
        this.studentRepository = studentRepository;
        this.majorRepository = majorRepository;
    }

    @Override
    public Page<Student> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return studentRepository.findAll(pageable);
        }
        String key = keyword.trim();
        return studentRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(key, key, pageable);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Optional<StudentForm> findFormById(Long id) {
        return studentRepository.findById(id).map(s -> {
            StudentForm form = new StudentForm();
            form.setId(s.getId());
            form.setName(s.getName());
            form.setEmail(s.getEmail());
            form.setAge(s.getAge());
            form.setGpa(s.getGpa());
            form.setMajorId(s.getMajor().getId());
            return form;
        });
    }

    @Override
    @Transactional
    public Student create(StudentForm form) {
        Student student = new Student();      // id = null → luôn INSERT
        copy(form, student);
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public boolean update(Long id, StudentForm form) {
        return studentRepository.findById(id)
                .map(existing -> {
                    copy(form, existing);
                    // entity đang "managed" → Hibernate tự UPDATE khi commit (dirty checking)
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (!studentRepository.existsById(id)) {
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean isEmailTaken(String email, Long excludeId) {
        if (email == null || email.isBlank()) return false;
        return excludeId == null
                ? studentRepository.existsByEmailIgnoreCase(email.trim())
                : studentRepository.existsByEmailIgnoreCaseAndIdNot(email.trim(), excludeId);
    }

    @Override
    public List<Major> getMajors() {
        return majorRepository.findAll(Sort.by("code"));
    }

    // DTO → Entity (chỗ duy nhất làm việc này)
    private void copy(StudentForm form, Student student) {
        Major major = majorRepository.findById(form.getMajorId())
                .orElseThrow(() -> new IllegalArgumentException("Chuyên ngành không tồn tại: " + form.getMajorId()));
        student.setName(form.getName().trim());
        student.setEmail(form.getEmail().trim());
        student.setAge(form.getAge());
        student.setGpa(form.getGpa());
        student.setMajor(major);
    }
}