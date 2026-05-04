package ru.ranepa.springboot.service;

import ru.ranepa.springboot.model.EmployeeEntity;
import ru.ranepa.springboot.repository.JpaEmployeeRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SpringEmployeeService {

    private final JpaEmployeeRepository repository;

    public SpringEmployeeService(JpaEmployeeRepository repository) {
        this.repository = repository;
    }

    public EmployeeEntity createEmployee(EmployeeEntity employee) {
        return repository.save(employee);
    }

    public Optional<EmployeeEntity> getEmployee(Long id) {
        return repository.findById(id);
    }

    public List<EmployeeEntity> getAllEmployees() {
        return repository.findAll();
    }

    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }

    public List<EmployeeEntity> findByPosition(String position) {
        return repository.findByPosition(position);
    }

    public BigDecimal calculateAverageSalary() {
        var employees = repository.findAll();
        if (employees.isEmpty()) return BigDecimal.ZERO;

        return employees.stream()
                .map(EmployeeEntity::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(employees.size()), 2, BigDecimal.ROUND_HALF_UP);
    }

    public Optional<EmployeeEntity> findHighestPaidEmployee() {
        return repository.findAll().stream()
                .max((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()));
    }
}