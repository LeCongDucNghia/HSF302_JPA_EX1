package fu.de200635;

import fu.de200635.dao.EmployeeDAO;
import fu.de200635.pojo.Employee;
import fu.de200635.pojo.Gender;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAO();

        // ===== CREATE =====
        Employee emp = new Employee("Nguyen Van A", "a@fpt.edu.vn", new BigDecimal("15000000"), Gender.MALE, LocalDate.of(2022, 3, 1));
        dao.save(emp);
        System.out.println("Da tao: " + emp);
        // ===== READ =====
        Employee found = dao.findById(emp.getId());
        System.out.println("Doc lai: " + found);

        // ===== UPDATE =====
        System.out.println("\n===== UPDATE =====");
        found.setSalary(new BigDecimal("19000000"));
        Employee updated = dao.update(found);
        System.out.println("Sau update: " + updated);
        // ===== READ =====
        Employee reChecked = dao.findById(emp.getId());
        System.out.println("Kiem tra lai sau update: " + reChecked);

        // ===== DELETE =====
        System.out.println("\n===== DELETE =====");
        dao.delete(emp.getId());
        // ===== READ =====
        Employee afterDelete = dao.findById(emp.getId());
        System.out.println("Sau khi xoa, tim lai: " + afterDelete); // ky vong: null

    }
}