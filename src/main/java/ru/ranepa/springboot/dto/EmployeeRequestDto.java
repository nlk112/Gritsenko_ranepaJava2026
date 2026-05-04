package ru.ranepa.springboot.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequestDto(
        String name,
        String position,
        BigDecimal salary,
        LocalDate hireDate
) {}