package fu.de200635;

import fu.de200635.dao.EmployeeDAO;
import fu.de200635.pojo.Employee;
import fu.de200635.pojo.Gender;
import fu.de200635.pojo.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import fu.de200635.util.JPAUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAO();
// Trước khi gỡ
        EntityManager em = JPAUtil.getEmf().createEntityManager();
        Employee emp1 = em.find(Employee.class, 1L);
        System.out.println("===== BEFORE UNASSIGN =====");
        System.out.println("Projects of " + emp1.getFullName());
        emp1.getProjects().forEach(p -> System.out.println(" - " + p.getProjectName()));
        em.close();
// Gỡ NV1 khỏi Project B
        dao.unassignEmployeeFromProject(1L, 2L);
// Kiểm tra lại
        EntityManager em2 = JPAUtil.getEmf().createEntityManager();
        Employee empAfter = em2.find(Employee.class, 1L);
        System.out.println("\n===== AFTER UNASSIGN =====");
        System.out.println("Projects of " + empAfter.getFullName());
        empAfter.getProjects().forEach(p -> System.out.println(" - " + p.getProjectName()));
        em2.close();
    }
}