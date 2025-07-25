# MicroSushi

## Описание

Проект представляет собой серверное API приложение для заказа суши и роллов

## Сервисы

- [product-service](./product-service)

## Запуск

Зайти в configuration, убрать все расширения .origin у файлов с секретами. Прописать пароли.

Запустить docker-compose.yml

```bash
docker compose up --build
```

Собрать все сервисы

```bash
.\mvnw clean install  
```

Запустить, прописав env

```bash
java -jar .\target\service-name.jar
```