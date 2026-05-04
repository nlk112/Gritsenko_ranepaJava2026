package ru.ranepa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ranepa.model.Employee;
import ru.ranepa.repository.InMemoryEmployeeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HrmServiceTest {

    private HrmService service;
    private InMemoryEmployeeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryEmployeeRepository();
        service = new HrmService(repository);
    }

    @Test
    void shouldCalculateAverageSalary() {
        service.addEmployee("Иванов", "Разработчик", new BigDecimal("100"), LocalDate.now());
        service.addEmployee("Петров", "Тестировщик", new BigDecimal("200"), LocalDate.now());
        service.addEmployee("Сидоров", "Менеджер", new BigDecimal("300"), LocalDate.now());

        BigDecimal avg = service.calculateAverageSalary();

        assertEquals(0, avg.compareTo(new BigDecimal("200.00")));
    }

    @Test
    void shouldFindHighestPaidEmployee() {
        service.addEmployee("Низов", "Стажёр", new BigDecimal("50"), LocalDate.now());
        service.addEmployee("Верхов", "Ведущий разработчик", new BigDecimal("500"), LocalDate.now());

        Optional<Employee> top = service.findHighestPaidEmployee();

        assertTrue(top.isPresent());
        assertEquals("Верхов", top.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenNoEmployees() {
        Optional<Employee> top = service.findHighestPaidEmployee();

        assertTrue(top.isEmpty());
    }

    @Test
    void shouldAddAndFindEmployee() {
        Employee emp = service.addEmployee("Иван Иванов", "Java-разработчик", new BigDecimal("1500"), LocalDate.now());

        Optional<Employee> found = service.getEmployee(emp.getId());

        assertTrue(found.isPresent());
        assertEquals("Иван Иванов", found.get().getName());
    }

    @Test
    void shouldDeleteEmployee() {
        Employee emp = service.addEmployee("Петр Петров", "Тестировщик", new BigDecimal("1500"), LocalDate.now());

        boolean deleted = service.removeEmployee(emp.getId());

        assertTrue(deleted);
        assertTrue(service.getEmployee(emp.getId()).isEmpty());
    }
}