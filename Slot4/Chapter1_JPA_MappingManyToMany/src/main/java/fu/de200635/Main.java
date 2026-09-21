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
        EntityManager em = JPAUtil.getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // ==========================
            // Employee
            // ==========================

            Employee emp1 = new Employee();
            emp1.setFullName("Nguyen Van A");
            emp1.setSalary(new BigDecimal("1500"));
            emp1.setHireDate(LocalDate.of(2024, 1, 10));
            emp1.setEmail("a@gmail.com");
            emp1.setGender(Gender.MALE);
            emp1.setActive(true);

            Employee emp2 = new Employee();
            emp2.setFullName("Tran Thi B");
            emp2.setSalary(new BigDecimal("1800"));
            emp2.setHireDate(LocalDate.of(2023, 6, 15));
            emp2.setEmail("b@gmail.com");
            emp2.setGender(Gender.FEMALE);
            emp2.setActive(true);

            Employee emp3 = new Employee();
            emp3.setFullName("Le Van C");
            emp3.setSalary(new BigDecimal("2000"));
            emp3.setHireDate(LocalDate.of(2022, 9, 20));
            emp3.setEmail("c@gmail.com");
            emp3.setGender(Gender.MALE);
            emp3.setActive(true);

            // ==========================
            // Project
            // ==========================

            Project projectA = new Project();
            projectA.setProjectCode("PRJ001");
            projectA.setProjectName("Project A");
            projectA.setBudget(new BigDecimal("50000"));
            projectA.setStartDate(LocalDate.of(2025, 1, 1));

            Project projectB = new Project();
            projectB.setProjectCode("PRJ002");
            projectB.setProjectName("Project B");
            projectB.setBudget(new BigDecimal("80000"));
            projectB.setStartDate(LocalDate.of(2025, 2, 1));

            em.persist(projectA);
            em.persist(projectB);

            em.persist(emp1);
            em.persist(emp2);
            em.persist(emp3);

            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();

        } finally {
            em.close();
        }

        // ==========================
        // Phân công dự án
        // ==========================

        EmployeeDAO dao = new EmployeeDAO();

        dao.assignEmployeeToProject(1L, 1L); // NV1 -> A
        dao.assignEmployeeToProject(1L, 2L); // NV1 -> B
        dao.assignEmployeeToProject(2L, 2L); // NV2 -> B
        dao.assignEmployeeToProject(3L, 1L); // NV3 -> A

        // ==========================
        // In kết quả
        // ==========================

        EntityManager em2 = JPAUtil.getEmf().createEntityManager();

        try {
            System.out.println("===== PROJECTS OF EMPLOYEES =====");
            for (long id = 1; id <= 3; id++) {
                Employee emp = em2.find(Employee.class, id);

                System.out.println("\nEmployee: " + emp.getFullName());

                emp.getProjects().forEach(project -> System.out.println(" - " + project.getProjectName()));
            }

        } finally {
            em2.close();
        }
        JPAUtil.getEmf().close();
    }
}