package ru.ranepa.presentation;

import ru.ranepa.service.HrmService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public final class ConsoleUi {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Scanner scanner = new Scanner(System.in);
    private final HrmService service;

    public ConsoleUi(HrmService service) {
        this.service = service;
    }

    public void start() {
        System.out.println("Привествую господа в нашей системе учёта сотрудников    ");
        boolean running = true;
        
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> showAllEmployees();
                case "2" -> addEmployeeFlow();
                case "3" -> deleteEmployeeFlow();
                case "4" -> findEmployeeFlow();
                case "5" -> showStatistics();
                case "6" -> {
                    service.saveToFile("employees.csv");
                    running = false;
                }
                default -> System.out.println("Произошла ошибка ввода.");
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("""
                Меню системы управления персоналом
                1. Показать всех сотрудников
                2. Добавить сотрудника
                3. Удалить сотрудника по id
                4. Найти сотрудника по id
                5. Показать статистику
                6. Выход
                Выберите опцию:""");
    }

    private void showAllEmployees() {
        var employees = service.getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("Сотрудников не найдено");
            return;
        }
        employees.forEach(System.out::println);
    }

    private void addEmployeeFlow() {
        try {
            System.out.print("Введите Имя: ");
            String name = scanner.nextLine();

            System.out.print("Введите должность: ");
            String position = scanner.nextLine();

            System.out.print("Введите оклад: ");
            BigDecimal salary = new BigDecimal(scanner.nextLine());

            System.out.print("Введите дату устройства в формате (YYYY-MM-DD): ");
            LocalDate hireDate = LocalDate.parse(scanner.nextLine(), DATE_FORMAT);

            var added = service.addEmployee(name, position, salary, hireDate);
            System.out.println("Сотрудник добавлен с id: " + added.getId());

        } catch (NumberFormatException e) {
            System.out.println("Неверный ввод для оклада.");
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты, используйте: YYYY-MM-DD.");
        } catch (IllegalArgumentException e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }

    private void deleteEmployeeFlow() {
        try {
            System.out.print("Введите id сотрудника на удаление: ");
            Long id = Long.parseLong(scanner.nextLine());
            service.removeEmployee(id);
            System.out.println("Сотрудник удалён.");
        } catch (NumberFormatException e) {
            System.out.println("Неверный id.");
        }
    }

    private void findEmployeeFlow() {
        try {
            System.out.print("Введите id сотрудника: ");
            Long id = Long.parseLong(scanner.nextLine());
            service.getEmployee(id)
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("Сотрудник не найден.")
                    );
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат id.");
        }
    }

    private void showStatistics() {
        System.out.println(service.getStatistics());
    }
}