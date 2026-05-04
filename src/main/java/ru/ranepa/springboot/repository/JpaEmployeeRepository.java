package ru.ranepa.springboot.repository;

import ru.ranepa.springboot.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.List;

public interface JpaEmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    List<EmployeeEntity> findByPosition(String position);
    List<EmployeeEntity> findBySalaryGreaterThanEqual(BigDecimal salary);
}