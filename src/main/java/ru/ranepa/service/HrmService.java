package ru.ranepa.service;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public final class HrmService {

    private final EmployeeRepository repository;

    public HrmService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee addEmployee(String name, String position, BigDecimal salary, LocalDate hireDate) {
        Employee employee = new Employee(name, position, salary, hireDate);
        return repository.save(employee);
    }

    public boolean removeEmployee(Long id) {
        return repository.delete(id);
    }

    public Optional<Employee> getEmployee(Long id) {
        return repository.findById(id);
    }

    public Collection<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public BigDecimal calculateAverageSalary() {
        var employees = repository.findAll();
        if (employees.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return employees.stream()
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(employees.size()), 2, java.math.RoundingMode.HALF_UP);
    }

    public Optional<Employee> findHighestPaidEmployee() {
        return repository.findAll().stream()
                .max(Comparator.comparing(Employee::getSalary));
    }

    public Collection<Employee> findByPosition(String position) {
        if (position == null || position.isBlank()) {
            return getAllEmployees();
        }
        return repository.findAll().stream()
                .filter(e -> e.getPosition().equalsIgnoreCase(position.trim()))
                .toList();
    }

    public String getStatistics() {
        var employees = repository.findAll();
        if (employees.isEmpty()) {
            return "Нет сотрудников в системе.";
        }
        BigDecimal avgSalary = calculateAverageSalary();
        var topEmployee = findHighestPaidEmployee();
        return """
                Статистика компании
                Суммарно сотрудников: %d
                Средний оклад: %s
                Максимальный оклад: %s
                """.formatted(
                    employees.size(),
                    avgSalary,
                    topEmployee.map(Employee::getName).orElse("N/A")
        );
    }

    public void saveToFile(String filename) {
        repository.saveToFile(filename);
    }

    public void loadFromFile(String filename) {
        repository.loadFromFile(filename);
    }
}