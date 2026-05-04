package ru.ranepa;

import ru.ranepa.presentation.ConsoleUi;
import ru.ranepa.repository.InMemoryEmployeeRepository;
import ru.ranepa.service.HrmService;

public final class HrmApplication {

    private HrmApplication() {}

    public static void main(String[] args) {
        var repository = new InMemoryEmployeeRepository();
        var service = new HrmService(repository);
        var ui = new ConsoleUi(service);
        repository.loadFromFile("employees.csv");
        ui.start();
    }
}