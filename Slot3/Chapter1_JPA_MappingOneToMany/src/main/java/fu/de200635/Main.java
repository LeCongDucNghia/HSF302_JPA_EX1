package fu.de200635;

import fu.de200635.dao.DepartmentDAO;
import fu.de200635.pojo.Department;
import fu.de200635.pojo.Employee;
import fu.de200635.pojo.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Department dept = new Department("Marketing", "Ha Noi");
        Employee emp = new Employee("test2@company.com", "Test", Gender.OTHER,
                new BigDecimal("1000"), LocalDate.now());
        Employee e2 = new Employee("bc@company.com", "B", Gender.FEMALE,
                new BigDecimal("1200"), LocalDate.of(2022, 2, 1));
        Employee e3 = new Employee("cd@company.com", "C", Gender.OTHER,
                new BigDecimal("1500"), LocalDate.of(2022, 3, 1));
        dept.addEmployee(emp);
        dept.addEmployee(e2);
        dept.addEmployee(e3);

        DepartmentDAO deptDAO = new DepartmentDAO();
        deptDAO.save(dept);
        System.out.println("Đã thêm: "+ dept.getName());
    }
}