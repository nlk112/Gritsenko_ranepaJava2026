package ru.ranepa.repository;

import ru.ranepa.model.Employee;
import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public final class InMemoryEmployeeRepository implements EmployeeRepository {

    private final Map<Long, Employee> storage = new HashMap<>();
    private static Long nextId = 1L;

    @Override
    public Employee save(Employee employee) {
        storage.put(employee.getId(), employee);
        return employee;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Collection<Employee> findAll() {
        return Collections.unmodifiableCollection(storage.values());
    }

    @Override
    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    @Override
    public boolean exists(Long id) {
        return id != null && storage.containsKey(id);
    }


    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new java.io.FileWriter(filename))) {
            writer.println("id,name,position,salary,hireDate");
            for (Employee employee : storage.values()) {
                writer.printf("%d,%s,%s,%s,%s%n",
                        employee.getId(),
                        employee.getName(),
                        employee.getPosition(),
                        employee.getSalary(),
                        employee.getHireDate());
            }
            System.out.println("Данные сохранены в файл: " + filename);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            int count = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",", -1);

                if (parts.length == 5) {
                    try {
                        Long id = Long.parseLong(parts[0]);
                        String name = parts[1];
                        String position = parts[2];
                        BigDecimal salary = new BigDecimal(parts[3]);
                        LocalDate hireDate = LocalDate.parse(parts[4]);

                        Employee employee = new Employee(name, position, salary, hireDate);
                        setId(employee, id);
                        storage.put(id, employee);

                        if (id >= nextId) {
                            nextId = id + 1;
                        }

                        count++;
                    } catch (Exception e) {
                        // Пропускаем некорректные строки
                    }
                }
            }
            if (count > 0) {
                System.out.println("Загружено сотрудников: " + count);
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
        }
    }

    private void setId(Employee employee, Long id) {
        try {
            var field = Employee.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(employee, id);
        } catch (Exception e) {
            throw new RuntimeException("Невозможно установить ID", e);
        }
    }

    public void clear() {
        storage.clear();
        nextId = 1L;
    }
}