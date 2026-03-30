# StudentApp

A modern, Spring Boot-based application for managing student registrations and courses. This project provides a comprehensive dashboard for academic administrators to track student progress, manage course offerings, and handle the registration process efficiently.

## Project Description

StudentApp is designed to streamline the administrative tasks of educational institutions. It allows users to:
- **Manage Students:** Create, update, and track student profiles, including demographic and academic information.
- **Course Catalog:** Maintain a detailed list of available courses with descriptions, credits, and prerequisites.
- **Registration System:** Enroll students in courses, manage registration statuses (Applied, Confirmed, Waitlisted, Completed), and prevent duplicate enrollments.
- **Insightful Dashboard:** Get a quick overview of total students, courses, and registration activities.

### Key Features
- **CRUD Operations:** Full Create, Read, Update, and Delete functionality for Students, Courses, and Registrations.
- **Data Validation:** Robust server-side validation using Jakarta Validation (JSR 380) to ensure data integrity.
- **Relational Data Mapping:** Complex relationships between Students and Courses managed via a Registration entity with JPA/Hibernate.
- **Interactive UI:** Dynamic web pages built with Thymeleaf, styled with modern CSS, and enhanced with JavaScript.
- **Automatic Data Seeding:** Comes with a `DataSeeder` to populate the database with sample data for immediate testing and demonstration.

### Project Setup

#### Prerequisites
- Java 23 (JDK 23)
- Maven 3.9+
- MySQL (Optional, H2 used by default)

#### Installation
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-repo/StudentApp.git
    cd StudentApp/studentapp
    ```
2.  **Configure the database (Optional):**
    By default, the application uses an H2 in-memory database. To use MySQL, update `src/main/resources/application.properties` with your credentials.
3.  **Build the project:**
    ```bash
    mvn clean install
    ```
4.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
5.  **Access the app:**
    Open your browser and navigate to `http://localhost:8081`.

---

## System Architecture

The project follows a classic **Layered Architecture** (often referred to as N-Tier Architecture) implemented within the Spring Boot framework:

1.  **Presentation Layer (View):** Uses **Thymeleaf** templates to render HTML dynamically. **CSS** and **JavaScript** provide the styling and client-side logic.
2.  **Controller Layer:** Spring MVC `@Controller` classes handle incoming HTTP requests, interact with the Service layer, and return views or redirects.
3.  **Service Layer:** Contains the core business logic. It orchestrates operations between controllers and repositories, ensuring that business rules (like preventing duplicate registrations) are enforced.
4.  **Data Access Layer (Repository):** Uses **Spring Data JPA** to interact with the database. Repositories extend `JpaRepository` to provide standard CRUD operations with minimal boilerplate.
5.  **Domain Model:** Consists of **JPA Entities** (`Student`, `Course`, `Registration`) that represent the data structure and their relationships (Many-to-Many resolved via a junction entity).
6.  **Database:** **H2** (In-memory) for development/testing and **MySQL** for production-like environments.

---

## Innovation and Unique Aspects

- **Hybrid Compatibility Layer:** The `Student` entity includes a "Compatibility Shim" (getters/setters like `getName()` and `setName()`) that allows the application to remain compatible with legacy code while transitioning to a more granular `firstName`/`lastName` structure.
- **Advanced Validation Logic:** Beyond simple field checks, the system implements custom cross-field validation within the service layer to prevent illogical data states, such as a student being registered for the same course twice.
- **Zero-Config Development:** By leveraging H2 and a pre-configured `DataSeeder`, the project allows new developers to start working immediately without setting up a local database server and personal seeder data.

---

## Challenges Encountered

- **Bidirectional Relationship Management:** Managing the `@OneToMany` and `@ManyToOne` relationships between Students, Courses, and Registrations required careful handling of cascade types and collection updates to avoid `DataIntegrityViolationException`.
- **Form Data Binding with Complex Objects:** Binding Thymeleaf form inputs to nested JPA entities (like selecting a `Student` object from a dropdown in a `Registration` form) required specific configuration and the use of converters or ID-based lookups.
- **Schema Migration:** Transitioning from a single `name` field to split `firstName` and `lastName` fields while maintaining data consistency during the migration phase was a technical hurdle.
- **Port Selection:** Default port 8080 was already in use on my machine, hence the choice of 8081 was seen as most suitable and time friendly.

---

## Reflection and Lessons Learned

- **Importance of Decoupling:** Separating business logic into the Service layer instead of leaving it in Controllers made the code much more testable and maintainable.
- **Validation:** Implementing validation at both the Domain level (annotations) and the Service level (business rules) is important for a working application..
- **Spring Data JPA:** The Spring Data JPA's derived query methods and repository abstractions saved on development time.
- **Importance of Documentation:** Documentation provided a ready reference resource to understand the project.

