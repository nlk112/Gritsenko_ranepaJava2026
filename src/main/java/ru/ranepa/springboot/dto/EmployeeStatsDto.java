package ru.ranepa.springboot.dto;

import java.math.BigDecimal;

public record EmployeeStatsDto(
        int totalEmployees,
        BigDecimal averageSalary,
        String highestPaidEmployee
) {}