package ru.ranepa.repository;

import ru.ranepa.model.Employee;
import java.util.Collection;
import java.util.Optional;

public interface EmployeeRepository {
    Employee save(Employee employee);
    Optional<Employee> findById(Long id);
    Collection<Employee> findAll();
    boolean delete(Long id);
    boolean exists(Long id);
    void saveToFile(String filename);
    void loadFromFile(String filename);
}