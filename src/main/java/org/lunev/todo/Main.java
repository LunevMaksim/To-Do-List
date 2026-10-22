package org.lunev.todo;

import org.lunev.todo.dao.UserDao;
import org.lunev.todo.dto.TaskCreateDto;
import org.lunev.todo.entity.User;
import org.lunev.todo.service.TaskService;

import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {

        try {
            User user = new User();
            user.setSurname("Лунёв");
            user.setName("Максим");
            user.setPatronymic("Михайлович");

            user = UserDao.getInstance().save(user);

            TaskCreateDto taskCreateDto = new TaskCreateDto(user.getId(), "Сон", "Пора лечь спать",
                    "Домашние дела", LocalDateTime.of(2026, 10, 22, 8, 30));

            Long id = TaskService.getInstance().createTask(taskCreateDto);

            System.out.println("Задача успешно создана с ID: " + id);
        } catch (Exception e) {
            System.err.println("Произошла ошибка при выполнении программы: " + e.getMessage());
            e.printStackTrace();
        }

    }
}