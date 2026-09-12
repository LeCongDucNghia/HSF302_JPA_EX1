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
        // [Lifecycle] emp dang o trang thai NEW/TRANSIENT (moi "new", chua lien quan DB)
        Employee emp = new Employee("Nguyen Van A", "a@fpt.edu.vn", new BigDecimal("15000000"), Gender.MALE, LocalDate.of(2022, 3, 1));

        dao.save(emp);
        // [Lifecycle] sau save(): trong luc persist() emp la MANAGED; sau khi method
        // save() return (EntityManager da dong), emp tro thanh DETACHED.
        System.out.println("Da tao: " + emp);

        // ===== READ =====
        Employee found = dao.findById(emp.getId());
        // [Lifecycle] found la mot object MANAGED trong pham vi EntityManager cua findById(),
        // nhung EntityManager cung da dong ngay sau khi return -> found cung la DETACHED
        // ngay khi ra khoi method.
        System.out.println("Doc lai: " + found);

    }
}