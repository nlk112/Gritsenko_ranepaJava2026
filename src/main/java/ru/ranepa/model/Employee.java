package ru.ranepa.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class Employee {
    
    private static Long nextId = 1L;
    
    private final Long id;
    private String name;
    private String position;
    private final BigDecimal salary;
    private final LocalDate hireDate;

    public Employee(String name, String position, BigDecimal salary, LocalDate hireDate) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не можт быть пустым");
        }
        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("Позиция не может быть пустой");
        }
        if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Зарпалата не может быть пустой");
        }
        if (hireDate == null) {
            throw new IllegalArgumentException("Дата приёма не может быть пустой");
        }

        this.id = nextId++;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public BigDecimal getSalary() { return salary; }
    public LocalDate getHireDate() { return hireDate; }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не можт быть пустым");
        }
        this.name = name;
    }

    public void setPosition(String position) {
        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("Позиция не может быть пустой");
        }
        this.position = position;
    }

    @Override
    public String toString() {
        return "Employee{id=%d, name='%s', position='%s', salary=%s, hireDate=%s}"
                .formatted(id, name, position, salary, hireDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}