package ru.ranepa.springboot.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponseDto(
        Long id,
        String name,
        String position,
        BigDecimal salary,
        LocalDate hireDate
) {}