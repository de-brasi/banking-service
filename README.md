### Описание
Приложение требует запущенной в **Docker** базы данных PostgreSQL:
```<shell>
docker compose up -d
```

Для управления версиями в БД используется **Liquibase**.

С текущими настройками сервер работает по адресу `localhost:8080`.

Просмотр спецификации OpenAPI/Swagger осуществляется по адресу: `localhost:8080/swagger-ui`.

Тесты проводятся в отдельной тестовой базе данных с использованием библиотеки **Testcontainers** и не влияют на содержимое основной базы данных.
