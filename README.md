# Recipe API (Spring Boot + MySQL) — JSON Import + Search APIs

This project is a Spring Boot REST API that imports recipe data from a given JSON file, cleans invalid values like `NaN` → `null`, stores the data in a MySQL database, and exposes **2 endpoints**:
1) **Get all recipes (pagination + sort by rating desc)**
2) **Search recipes (filters + pagination + sort by rating desc)**

---

## 1) Problem Statement (What this project solves)

A JSON dataset of recipes is provided. The task is to:

- Read the JSON file from the backend
- Store the recipe records into **MySQL**
- Handle invalid values like `"NaN"` by storing them as **NULL** in DB
- Store nested nutrition object (`nutrients`) inside DB as **MySQL JSON**
- Create REST APIs to **retrieve and search** recipes with filters

---

## 2) Tech Stack

- Java
- Spring Boot (Web + Data JPA)
- MySQL
- Jackson (ObjectMapper for JSON parsing)
- Maven

---

## 3) Spring Initializr Setup

Project created using https://start.spring.io

Selected:
- Maven Project
- Java
- Dependencies:
  - Spring Web
  - Spring Data JPA
  - MySQL Driver

---

## 4) Database Setup

### Create database
```sql
CREATE DATABASE recipes_db;
```

### application.properties
```properties
server.port=3001
spring.datasource.url=jdbc:mysql://localhost:3306/recipes_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

---

## 5) Project Structure

```
com.recipe.recipe_api
 ├── RecipeApiApplication.java
 ├── entity/Recipe.java
 ├── repository/RecipeRepository.java
 ├── service/RecipeImportService.java
 ├── controller/RecipeController.java
 ├── dto/RecipeResponse.java
 └── config/DataLoader.java
```

---

## 6) API Endpoints

### Get all recipes
GET /api/recipes?page=1&limit=10

### Search recipes
GET /api/recipes/search?cuisine=Indian&ratingMin=4

---

## 7) SQL Queries used internally

- SELECT with pagination
- SELECT with filtering
- INSERT (bulk import)
- COUNT for checking duplicates

Example manual queries:

```sql
SELECT COUNT(*) FROM recipes;
SELECT * FROM recipes LIMIT 10;
SELECT * FROM recipes WHERE cuisine='Indian' AND rating>=4.2;
```

---

## 8) Execution Steps

1. Put JSON file in:
   src/main/resources/data/recipes.json

2. Configure MySQL credentials

3. Run:
```
mvn spring-boot:run
```

4. Test APIs:
```
http://localhost:3001/api/recipes
http://localhost:3001/api/recipes/search
```

---

## 9)  Summary

This project is a Spring Boot REST API that imports recipe data from JSON into MySQL, handles NaN values, stores nested JSON data, and provides search APIs with pagination and filters using JPA Specifications.

---

## Author
Praveen K — Java Developer
