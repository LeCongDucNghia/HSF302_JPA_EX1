package com.hsf302.chapter6.controller;

import com.hsf302.chapter6.dto.StudentForm;
import com.hsf302.chapter6.entity.Student;
import com.hsf302.chapter6.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final String FORM_VIEW = "students/form";
    private static final int MAX_PAGE_SIZE = 50;

    // Chỉ cho sắp xếp theo các cột này — không tin tham số ?sort= của người dùng
    private static final Set<String> SORTABLE = Set.of("id", "name", "email", "age", "gpa", "major.name");

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ==================== READ ALL: tìm kiếm + phân trang + sắp xếp ====================
    // GET /students?keyword=an&page=0&size=5&sort=gpa,desc

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       @RequestParam(defaultValue = "id,asc") String sort,
                       Model model) {

        String[] parts = sort.split(",");
        String sortField = SORTABLE.contains(parts[0]) ? parts[0] : "id";
        Sort.Direction dir = parts.length > 1 && "desc".equalsIgnoreCase(parts[1])
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Thêm id làm khoá phụ để thứ tự ổn định khi nhiều dòng trùng giá trị cột sắp xếp
        Sort order = Sort.by(dir, sortField);
        if (!sortField.equals("id")) {
            order = order.and(Sort.by(Sort.Direction.ASC, "id"));
        }

        Page<Student> studentPage = studentService.search(keyword,
                PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), MAX_PAGE_SIZE), order));

        model.addAttribute("studentPage", studentPage);
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("keyword", keyword.trim());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", dir.isAscending() ? "asc" : "desc");
        return "students/list";
    }

    // ==================== READ ONE ====================

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        return studentService.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    return "students/detail";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên ID: " + id);
                    return "redirect:/students";
                });
    }

    // ==================== CREATE ====================

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("studentForm", new StudentForm());
        return formView(model, false);
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("studentForm") StudentForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        if (!bindingResult.hasFieldErrors("email")
                && studentService.isEmailTaken(form.getEmail(), null)) {
            bindingResult.rejectValue("email", "duplicate", "Email đã tồn tại");
        }
        if (bindingResult.hasErrors()) {
            return formView(model, false);          // không redirect để giữ dữ liệu + lỗi
        }
        try {
            studentService.create(form);
        } catch (DataIntegrityViolationException e) {   // 2 người submit cùng lúc
            bindingResult.rejectValue("email", "duplicate", "Email đã tồn tại");
            return formView(model, false);
        }
        ra.addFlashAttribute("successMsg", "Thêm sinh viên thành công!");
        return "redirect:/students";                    // PRG pattern
    }

    // ==================== UPDATE ====================

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        return studentService.findFormById(id)
                .map(form -> {
                    model.addAttribute("studentForm", form);
                    return formView(model, true);
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên ID: " + id);
                    return "redirect:/students";
                });
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("studentForm") StudentForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        form.setId(id);   // form không gửi id → gắn từ URL để khi trả lỗi, form action vẫn đúng

        if (!bindingResult.hasFieldErrors("email")
                && studentService.isEmailTaken(form.getEmail(), id)) {
            bindingResult.rejectValue("email", "duplicate", "Email đã được sinh viên khác sử dụng");
        }
        if (bindingResult.hasErrors()) {
            return formView(model, true);
        }
        try {
            if (studentService.update(id, form)) {
                ra.addFlashAttribute("successMsg", "Cập nhật thành công!");
            } else {
                ra.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên ID: " + id);
            }
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("email", "duplicate", "Email đã được sinh viên khác sử dụng");
            return formView(model, true);
        }
        return "redirect:/students";
    }

    // ==================== DELETE ====================

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        if (studentService.delete(id)) {
            ra.addFlashAttribute("successMsg", "Xóa sinh viên thành công!");
        } else {
            ra.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên để xóa!");
        }
        return "redirect:/students";
    }

    // ==================== Helper ====================

    // Chỉ form mới cần danh sách chuyên ngành → nạp ở đây thay vì @ModelAttribute chạy cho mọi request
    private String formView(Model model, boolean isEdit) {
        model.addAttribute("majors", studentService.getMajors());
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Cập nhật sinh viên" : "Thêm sinh viên mới");
        return FORM_VIEW;
    }
}