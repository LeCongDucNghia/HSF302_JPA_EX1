package fu.de200635.dao;

import fu.de200635.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;

public class ProjectDAO {
    public void projectStatistics() {

        EntityManager em = JPAUtil.getEmf().createEntityManager();

        try {

            String jpql = """
                SELECT p.projectName,
                       COUNT(e),
                       SUM(e.salary)
                FROM Project p
                JOIN p.employees e
                WHERE e.active = true
                GROUP BY p.projectName
                """;

            List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();

            for (Object[] row : results) {

                String projectName = (String) row[0];
                Long employeeCount = (Long) row[1];
                BigDecimal totalSalary = (BigDecimal) row[2];

                System.out.println("Project: " + projectName + " | Active Employees: " + employeeCount + " | Total Salary: " + totalSalary);
            }
        } finally {
            em.close();
        }
    }
}
