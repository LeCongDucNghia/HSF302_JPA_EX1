package com.hsf302.student_thymeleaf.controller;

import com.hsf302.student_thymeleaf.entity.Student;
import com.hsf302.student_thymeleaf.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @ModelAttribute("majors")
    public Map<String, String> majors() {
        Map<String, String> majors = new LinkedHashMap<>();
        majors.put("SE", "Kỹ thuật phần mềm");
        majors.put("AI", "Trí tuệ nhân tạo");
        majors.put("IA", "An toàn thông tin");
        majors.put("GD", "Thiết kế đồ họa");
        return majors;
    }

    // [CÂU 3]: Nhận tham số page (mặc định 0), truyền "page" vào Model
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String major,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "id") String sort,
                       @RequestParam(defaultValue = "asc") String dir,
                       Model model) {

        Page<Student> studentPage = studentService.search(keyword, major, page, sort, dir);

        model.addAttribute("page", studentPage);
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("totalStudents", studentPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("major", major);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("avgGpa", studentService.averageGpa(studentPage.getContent()));
        model.addAttribute("activeMenu", "list");

        return "students/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Student> opt = studentService.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên có id = " + id);
            return "redirect:/students";
        }
        model.addAttribute("student", opt.get());
        model.addAttribute("activeMenu", "list");
        return "students/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("activeMenu", "new");
        return "students/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Student> opt = studentService.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên có id = " + id);
            return "redirect:/students";
        }
        model.addAttribute("student", opt.get());
        model.addAttribute("activeMenu", "list");
        return "students/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("student") Student student,
                       BindingResult result,
                       Model model,
                       RedirectAttributes ra) {

        if (studentService.isStudentCodeDuplicated(student.getStudentCode(), student.getId())) {
            result.rejectValue("studentCode", "duplicate", "Mã sinh viên đã tồn tại");
        }

        if (result.hasErrors()) {
            model.addAttribute("activeMenu", student.getId() == null ? "new" : "list");
            return "students/form";
        }

        boolean isNew = (student.getId() == null);
        studentService.save(student);
        ra.addFlashAttribute("successMessage",
                (isNew ? "Thêm" : "Cập nhật") + " sinh viên " + student.getFullName() + " thành công!");
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        studentService.deleteById(id);
        ra.addFlashAttribute("successMessage", "Đã xóa sinh viên có id = " + id);
        return "redirect:/students";
    }
}
