package fu.de200635;

import fu.de200635.dao.DepartmentDAO;
import fu.de200635.pojo.Department;
import fu.de200635.pojo.Employee;
import fu.de200635.pojo.Gender;
import fu.de200635.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DepartmentDAO departmentDAO = new DepartmentDAO();

            List<Department> departments = departmentDAO.findAllWithEmployees();

            for (Department d : departments) {
                System.out.println("Department: " + d.getName());

                // kích hoạt LAZY LOAD
                System.out.println("So nhan vien: " + d.getEmployees().size());
            }
            JPAUtil.close();
    }
}