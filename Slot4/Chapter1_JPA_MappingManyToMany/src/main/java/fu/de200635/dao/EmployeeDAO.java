package fu.de200635.dao;

import fu.de200635.pojo.Employee;
import fu.de200635.pojo.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import fu.de200635.util.JPAUtil;

import java.util.List;

public class EmployeeDAO {
    public void assignEmployeeToProject(Long employeeId, Long projectId) {
        EntityManager em = JPAUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, projectId);

            if (employee == null) {
                throw new RuntimeException(
                        "Employee not found: " + employeeId);
            }

            if (project == null) {
                throw new RuntimeException(
                        "Project not found: " + projectId);
            }
            employee.assignToProject(project);
            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void unassignEmployeeFromProject(Long employeeId, Long projectId) {
        EntityManager em = JPAUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, projectId);

            if (employee == null || project == null) {
                throw new RuntimeException("Employee or Project not found");
            }

            employee.unassignFromProject(project);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void findEmployeesInMultipleProjects() {
        EntityManager em = JPAUtil.getEmf().createEntityManager();
        try {
            String jpql = """
                SELECT e
                FROM Employee e
                WHERE e.active = true
                AND SIZE(e.projects) > 1
                """;

            List<Employee> employees = em.createQuery(jpql, Employee.class).getResultList();

            System.out.println("\n===== EMPLOYEES IN MULTIPLE PROJECTS =====");

            if (employees.isEmpty()) {
                System.out.println("(No employee is involved in more than one project.)");
            } else {
                employees.forEach(e -> System.out.println(e.getId() + " - " + e.getFullName()));
            }
        }
        finally {
            em.close();
        }
    }
}