# Product Service

## Описание

Сервис, который управляет продуктами.

## Endpoints

### Получить все продукты

#### Запрос

GET `localhost:8080/products`

#### Ответ

```json
[
  {
    "id": "2a8dafa3-8070-4176-aa46-c12df7c8c211",
    "name": "Roll1",
    "price": 250.00,
    "description": "simple product",
    "url": "http://localhost:9000/product-bucket/8aab8b74-282c-4490-a847-da63490c0799"
  },
  {
    "id": "0418c2ce-1f9b-4822-b27d-7204651dd227",
    "name": "Roll2",
    "price": 350.00,
    "description": "simple product",
    "url": "http://localhost:9000/product-bucket/52a06e40-c840-42dc-ab85-ba7f51630023"
  }
]
```

### Получить один продукт по id

#### Запрос

GET `localhost:8080/products/{id}`

#### Ответ

```json
{
  "id": "0418c2ce-1f9b-4822-b27d-7204651dd227",
  "name": "Roll2",
  "price": 350.00,
  "description": "simple product",
  "url": "http://localhost:9000/product-bucket/52a06e40-c840-42dc-ab85-ba7f51630023"
}
```

### Создать новый продукт

#### Запрос

POST `localhost:8080/products`

#### Тело запроса

```json
{
  "name": "Roll2",
  "price": 250,
  "description": "simple product",
  "base64Image": "/9j/4AAQSkZJRgAB..."
}
```

#### Ответ

```json
{
  "id": "0418c2ce-1f9b-4822-b27d-7204651dd227",
  "name": "Roll2",
  "price": 250.00,
  "description": "simple product",
  "url": "http://localhost:9000/product-bucket/52a06e40-c840-42dc-ab85-ba7f51630023"
}
```

### Изменить продукт

#### Запрос

PUT `localhost:8080/products/{id}`

#### Тело запроса

Может принимать только часть полей

```json
{
  "name": "Roll3",
  "base64Image": "/9j/4AAQSkZJRgAB..."
}
```

#### Ответ

```json
{
  "id": "0418c2ce-1f9b-4822-b27d-7204651dd227",
  "name": "Roll3",
  "price": 250.00,
  "description": "simple product",
  "url": "http://localhost:9000/product-bucket/52a06e40-c840-42dc-ab85-ba7f51630023"
}
```

### Удалить продукт

### Запрос

DELETE `localhost:8080/products/{id}`

## Техническая информация

### Технологии

Технологии, которые реализованы в сервисе:

- Jpa + Postgres
- Validation
- Exception Handler
- TESTS
- s3 (MINIO)
- REDIS