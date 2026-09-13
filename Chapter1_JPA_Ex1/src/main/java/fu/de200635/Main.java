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
        // \\\[Lifecycle] emp dang o trang thai NEW/TRANSIENT (moi "new", chua lien quan DB)
        Employee emp = new Employee("Nguyen Van A", "a@fpt.edu.vn", new BigDecimal("15000000"), Gender.MALE, LocalDate.of(2022, 3, 1));
        dao.save(emp);
        // \\\[Lifecycle] sau save(): trong luc persist() emp la MANAGED; sau khi method
        // save() return (EntityManager da dong), emp tro thanh DETACHED.
        System.out.println("Da tao: " + emp);
        // ===== READ =====
        Employee found = dao.findById(emp.getId());
        // \\\[Lifecycle] found la mot object MANAGED trong pham vi EntityManager cua findById(),
        // nhung EntityManager cung da dong ngay sau khi return -> found cung la DETACHED
        // ngay khi ra khoi method.
        System.out.println("Doc lai: " + found);

        // ===== UPDATE =====
        System.out.println("\n===== UPDATE =====");
        found.setSalary(new BigDecimal("19000000"));
        // \\\[Lifecycle] found dang DETACHED, sua field luc nay KHONG tu dong sync xuong DB
        Employee updated = dao.update(found);
        // \\\[Lifecycle] update() goi merge(found) -> tra ve "updated" la MANAGED (trong luc
        // transaction dang chay); sau khi method return, "updated" tro thanh DETACHED.
        System.out.println("Sau update: " + updated);
        // Doc lai de kiem chung
        Employee reChecked = dao.findById(emp.getId());
        System.out.println("Kiem tra lai sau update: " + reChecked);

        // ===== DELETE =====
        System.out.println("\n===== DELETE =====");
        dao.delete(emp.getId());
        // \\\[Lifecycle] ben trong delete(): entity tim duoc chuyen MANAGED -> REMOVED,
        // bi xoa that su khoi DB khi commit().
        // ===== READ =====
        Employee afterDelete = dao.findById(emp.getId());
        System.out.println("Sau khi xoa, tim lai: " + afterDelete); // ky vong: null

        // ===== Test ====
        Employee dup1 = new Employee("User 1", "trung@fpt.edu.vn",
                new BigDecimal("10000000"), Gender.FEMALE, LocalDate.now());
        Employee dup2 = new Employee("User 2", "trung@fpt.edu.vn", // trung email
                new BigDecimal("11000000"), Gender.MALE, LocalDate.now());
        dao.save(dup1);
        try {
            dao.save(dup2); // ky vong: nem exception vi vi pham UNIQUE
            System.out.println("LOI: khong thay exception nhu ky vong!");
        } catch (RuntimeException ex) {
            System.out.println("Da bat duoc loi trung email nhu ky vong: " + ex.getClass().getSimpleName());
        }
    }
}