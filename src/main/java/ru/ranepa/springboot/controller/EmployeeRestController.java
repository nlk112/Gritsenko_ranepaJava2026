package ru.ranepa.springboot.controller;

import ru.ranepa.springboot.dto.EmployeeRequestDto;
import ru.ranepa.springboot.dto.EmployeeResponseDto;
import ru.ranepa.springboot.dto.EmployeeStatsDto;
import ru.ranepa.springboot.model.EmployeeEntity;
import ru.ranepa.springboot.service.SpringEmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final SpringEmployeeService service;

    public EmployeeRestController(SpringEmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAll() {
        var employees = service.getAllEmployees();
        var response = employees.stream().map(this::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getById(@PathVariable Long id) {
        return service.getEmployee(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> create(@RequestBody EmployeeRequestDto dto) {
        var employee = new EmployeeEntity(dto.name(), dto.position(), dto.salary(), dto.hireDate());
        var saved = service.createEmployee(employee);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<EmployeeResponseDto>> getByPosition(@PathVariable String position) {
        var employees = service.findByPosition(position);
        var response = employees.stream().map(this::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<EmployeeStatsDto> getStats() {
        var employees = service.getAllEmployees();
        if (employees.isEmpty()) {
            return ResponseEntity.ok(new EmployeeStatsDto(0, java.math.BigDecimal.ZERO, null));
        }

        var avg = service.calculateAverageSalary();
        var top = service.findHighestPaidEmployee();

        return ResponseEntity.ok(new EmployeeStatsDto(
                employees.size(),
                avg,
                top.map(EmployeeEntity::getName).orElse(null)
        ));
    }

    private EmployeeResponseDto toResponse(EmployeeEntity e) {
        return new EmployeeResponseDto(
                e.getId(), e.getName(), e.getPosition(),
                e.getSalary(), e.getHireDate()
        );
    }
}