package fu.de200635;

import fu.de200635.dao.DepartmentDAO;
import fu.de200635.pojo.Department;
import fu.de200635.pojo.Employee;
import fu.de200635.pojo.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        DepartmentDAO departmentDAO = new DepartmentDAO();

        Department found =
                departmentDAO.findByIdWithEmployees(1L);

        System.out.println("Phong ban: " + found.getName());

        for (Employee e : found.getEmployees()) {
            System.out.println(
                    e.getFullName()
                            + " - "
                            + e.getEmail()
            );
        }
    }
}