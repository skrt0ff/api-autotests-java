# API Autotests (Java + REST Assured)

Учебный проект: автотесты для REST API на Java. Цель — на практике разобраться
с ООП, принципами проектирования и паттернами, строя небольшой тестовый фреймворк.

> Проект учебный. Тесты ходят в публичные тренировочные API, данные там
> «ненастоящие» (например, JSONPlaceholder не сохраняет созданные записи).

## Стек

- Java 17
- Gradle
- JUnit 5
- REST Assured
- Jackson

## Запуск

Нужны JDK 17+ и доступ в интернет.

```
./gradlew test
```

## Структура

```
src/test/java/api/
├── models/    модели данных (Post)
├── clients/   API-клиенты (BaseApi, PostsApi)
├── logging/   логгеры (TestLogger, ConsoleLogger, InMemoryLogger)
└── tests/     тесты
```

## Где какой принцип ООП показан

| Принцип | Где в проекте |
|---|---|
| Инкапсуляция | `Post` — приватные неизменяемые поля и проверки в конструкторе |
| Абстракция | `BaseApi` скрывает URL и детали отправки запроса |
| Абстрактный класс | `BaseApi` с абстрактным методом `basePath()` |
| Наследование | `PostsApi` наследуют `BaseApi` |
| Интерфейс | `TestLogger` и его реализации |
| Полиморфизм | один код работает с любым `TestLogger` |
| Внедрение зависимости | логгер передаётся в конструктор `BaseApi` |

## Планы

- [x] Основы ООП на примере API-клиентов
- [ ] Restful-Booker: CRUD и авторизация
- [ ] Преобразование JSON в Java-объекты
- [ ] Request/Response Specification
- [ ] Принципы SOLID, паттерны (Builder, Factory, Strategy)
- [ ] Отчёты Allure, запуск в GitHub Actions