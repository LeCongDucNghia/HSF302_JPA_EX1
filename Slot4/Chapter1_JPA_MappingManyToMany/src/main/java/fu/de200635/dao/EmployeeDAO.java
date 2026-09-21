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
                System.out.println("(không có nhân viên nào tham gia nhiều hơn 1 project)");
            } else {
                employees.forEach(e -> System.out.println(e.getId() + " - " + e.getFullName()));
            }
        }
        finally {
            em.close();
        }
    }

    /**
     * TODO 5.11 - Nhân viên nghỉ việc: chỉ set active = false.
     *
     * Nhân viên nghỉ việc có nên TỰ ĐỘNG bị gỡ khỏi tất cả project không?
     * => KHÔNG. Lý do:
     *  1. "Nghỉ việc" là thay đổi trạng thái nghiệp vụ (soft delete), không phải xóa dữ liệu.
     *     Các dòng trong bảng employee_project là lịch sử tham gia dự án, cần giữ lại
     *     để tra cứu, báo cáo, tính chi phí nhân sự của project sau này.
     *  2. Các query thống kê (TODO 5.8, 5.10) đã lọc e.active = true, nên nhân viên
     *     đã nghỉ tự động không bị tính vào kết quả mà không cần xóa quan hệ.
     *  3. Không dùng cascade REMOVE ở quan hệ N-N: Project được dùng chung bởi nhiều
     *     Employee, cascade sẽ có nguy cơ xóa nhầm Project của người khác.
     *
     * Cách xử lý phù hợp:
     *  - Ở đây chỉ update cờ active, KHÔNG động vào collection projects
     *    nên bảng employee_project giữ nguyên.
     *  - Nếu nghiệp vụ cần bàn giao, người quản lý chủ động gỡ nhân viên khỏi các
     *    project còn đang chạy bằng unassignEmployeeFromProject() (bước riêng, có chủ đích);
     *    các project đã kết thúc thì giữ nguyên để làm lịch sử.
     */
    public void deactivateEmployee(Long employeeId) {
        EntityManager em = JPAUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Employee employee = em.find(Employee.class, employeeId);
            if (employee == null) {
                throw new RuntimeException("Employee not found: " + employeeId);
            }

            employee.setActive(false); // entity đang managed -> tự UPDATE khi commit

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
}