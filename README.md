# 🚛 Система управления транспортной компанией

<div align="center">
  
### Курсовая работа по дисциплине «Современные технологии программирования»

## Тема: "Информационно-справочная система для управления транспортной компанией: Создание системы для планирования маршрутов, учета водителей и транспортных средств"

### Выполнил студент 3-его курса Гизатулин Никита Александрович
### Группа: ПИ22-1

---

<div align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen" />
  <img src="https://img.shields.io/badge/Java-17-orange" />
  <img src="https://img.shields.io/badge/Thymeleaf-3.x-green" />
  <img src="https://img.shields.io/badge/PostgreSQL-latest-blue" />
  <img src="https://img.shields.io/badge/Yandex%20Maps-API-red" />
</div>

<div align="center">
  <p>
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" title="Java" width="40" height="40"/>
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" title="Spring" width="40" height="40"/>
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" title="PostgreSQL" width="40" height="40"/>
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/html5/html5-original.svg" title="HTML5" width="40" height="40"/>
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/css3/css3-original.svg" title="CSS3" width="40" height="40"/>
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/javascript/javascript-original.svg" title="JavaScript" width="40" height="40"/>
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/bootstrap/bootstrap-original.svg" title="Bootstrap" width="40" height="40"/>
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/git/git-original.svg" title="Git" width="40" height="40"/>
    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" title="Maven" width="40" height="40"/>
  </p>
</div>

</div>

## 📋 Обзор проекта
Данный проект представляет собой комплексную систему управления транспортной компанией, реализованную с использованием клиент-серверной архитектуры. Система обеспечивает полный цикл управления транспортными средствами, маршрутами, водителями и формированием отчетности.

## 🔧 Технологический стек

### Серверная часть (Backend)
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

- Spring Web MVC для создания RESTful API
- Spring Data JPA для работы с базой данных
- Spring Security для обеспечения безопасности
- JWT для аутентификации и авторизации
- Hibernate для ORM
- Lombok для уменьшения шаблонного кода

### Клиентская часть (Frontend)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Bootstrap](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)

- Thymeleaf для создания динамических веб-страниц
- HTML5 для разметки веб-страниц
- CSS3 для стилизации (Flexbox, Grid, анимации)
- JavaScript для клиентской логики
- Яндекс Карты API для визуализации маршрутов
- Bootstrap 5 для адаптивного дизайна

## 🏗 Архитектура проекта

```mermaid
graph TD
    A[Клиент] -->|HTTP запросы| B[Spring Boot Backend]
    B -->|JWT Auth| C[Spring Security]
    B -->|Данные| D[PostgreSQL]
    B -->|ORM| E[Hibernate]
    A -->|Маршруты| F[Яндекс Карты API]
    B -->|Шаблоны| G[Thymeleaf]
    G -->|Рендеринг| A
```

## 📊 Диаграмма базы данных

```mermaid
erDiagram
    USER ||--o{ DRIVER : has
    USER ||--o{ MANAGER : has
    DRIVER ||--o{ ROUTE : drives
    TRUCK ||--o{ ROUTE : assigned
    ROUTE ||--o{ REPORT : generates
    MANAGER ||--o{ REPORT : creates

    USER {
        int id PK
        string username
        string password
        string role
    }
    DRIVER {
        int id PK
        string name
        string license
        date hire_date
    }
    TRUCK {
        int id PK
        string model
        string number
        string status
    }
    ROUTE {
        int id PK
        int driver_id FK
        int truck_id FK
        string start_point
        string end_point
        datetime created_at
    }
    REPORT {
        int id PK
        int route_id FK
        int manager_id FK
        datetime created_at
    }
    MANAGER {
        int id PK
        string name
        string department
    }
```

## 🔐 Безопасность
![Security](https://img.shields.io/badge/Security-JWT%20%7C%20Spring_Security-success?style=for-the-badge)

- Аутентификация на основе JWT токенов
- Шифрование паролей с использованием BCrypt
- Ролевая модель доступа (RBAC)
- Защита от CSRF атак
- Настроенная CORS политика
- Валидация всех входных данных
- Защита от SQL-инъекций
- Аудит действий пользователей

## 🎨 Особенности интерфейса

### Дизайн
- Material Design
- Адаптивная верстка
- Темная и светлая темы
- Анимированные переходы
- Интерактивные элементы

### Карты и маршруты
- Интеграция с Яндекс Картами
- Визуализация маршрутов
- Drag-and-drop точек
- Оптимизация маршрута
- Расчет времени и расстояния

## 📝 Установка и запуск

```bash
# Клонирование репозитория
git clone https://github.com/yourusername/transport-company.git

# Настройка базы данных
psql -U postgres -f database/init.sql

# Настройка переменных окружения
cp .env.example .env

# Сборка проекта
mvn clean install

# Запуск приложения
java -jar target/transport-company.jar
```

## 🔄 Процесс работы системы

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant F as Frontend
    participant B as Backend
    participant DB as База данных
    participant Y as Яндекс Карты

    U->>F: Вход в систему
    F->>B: Запрос авторизации
    B->>DB: Проверка учетных данных
    DB-->>B: Подтверждение
    B-->>F: JWT токен
    F-->>U: Доступ к системе
    U->>F: Создание маршрута
    F->>Y: Запрос карты
    Y-->>F: Отображение карты
    F->>B: Сохранение маршрута
    B->>DB: Запись данных
    DB-->>B: Подтверждение
    B-->>F: Успех
    F-->>U: Уведомление
```

---

<div align="center">
  
### 📞 Контакты для связи

[Telegram](mailto:https://t.me/butcher044)

</div>
