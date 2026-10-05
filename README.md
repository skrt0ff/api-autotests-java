# api-autotests-java

Учебный проект по автотестированию REST API на Java. Цель: на живом примере разобраться в ООП, принципах проектирования, паттернах и написании API-тестов.

Тесты работают только с открытыми демо-сервисами:
- [JSONPlaceholder](https://jsonplaceholder.typicode.com) — публичный фейковый API для постов
- [Restful-Booker](https://restful-booker.herokuapp.com) — API бронирований с авторизацией по токену

## Стек

- Java 17
- Gradle (wrapper)
- JUnit 5
- REST Assured 5
- Jackson (сериализация и десериализация JSON)

## Запуск

```bash
./gradlew cleanTest test
```

Адреса серверов и данные для авторизации лежат в `src/test/resources/config.properties`.

## Структура проекта

```
src/test/java/api/
├── builders/   BookingBuilder — сборка тестовых данных (паттерн Builder)
├── clients/    BaseApi, PostsApi, BookingApi — клиенты API
├── config/     Config — чтение настроек из config.properties
├── logging/    TestLogger, ConsoleLogger, InMemoryLogger
├── models/     модели запросов и ответов (record и классы)
│   ├── auth/       AuthRequest, AuthResponse
│   └── booking/    Booking, BookingDates, CreatedBooking
└── tests/      PostsApiTest, BookingApiTest, ConfigTest, LoggerTest, ...
```

## Что покрыто тестами

- **Posts:** получение списка и одного поста, ответ 404 для несуществующего поста, проверка логирования запросов
- **Booking:** получение списка и бронирования по id, создание, получение токена, удаление с проверкой, что бронирование исчезло
- **Config и логгеры:** чтение настроек, ошибка при отсутствующем ключе, неизменяемость списка сообщений
- **Билдер:** значения по умолчанию и переопределение полей

## Где что из ООП и принципов

| Тема | Где смотреть |
|---|---|
| Инкапсуляция | `Post` (проверки в конструкторе, `private final`), `InMemoryLogger` (`List.copyOf`) |
| Абстракция | `BaseApi` прячет URL и детали запросов, тест вызывает `getById(1)` |
| Наследование | `PostsApi` и `BookingApi` наследуют `BaseApi` |
| Абстрактный класс | `BaseApi` — общий код и состояние для семьи клиентов |
| Интерфейс | `TestLogger` —