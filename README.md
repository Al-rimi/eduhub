# EduHub: University Application Management Platform

## Project Summary

EduHub is a robust Java EE web application designed to simplify and manage the university application process. It supports various user roles, including **Platform Admins**, **Facility Admins**, **Staff**, and **Students**, each with specific functionalities. The platform provides a centralized system for managing universities, majors, and applications, ensuring a streamlined and efficient workflow from application submission to status tracking.


## Architecture and Technologies

EduHub is built on a layered architecture using the following key technologies:

* **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA
* **Frontend:** JSP, JSTL, Tailwind CSS
* **Database:** MySQL
* **Build Tool:** Maven
* **Diagramming:** PlantUML


## Key Features

* **User Authentication and Authorization:** Secure login and role-based access control.
* **University and Major Management:** Comprehensive tools for creating, updating, and managing universities and their associated majors.
* **Application Submission and Tracking:** Students can apply to universities and monitor their application status in real time.
* **Facility Admin Self-Registration:** Facility Admins can register and manage their specific university.
* **Admin Dashboards:** Provides detailed statistics and management capabilities for Platform and Facility Admins.


## User Roles

EduHub defines distinct roles to manage permissions and workflows:

* **Platform Admin:** Oversees all users, universities, and system configurations.
* **Facility Admin:** Manages a single university and its applications.
* **Staff:** Can view and process applications for assigned universities.
* **Student:** Submits applications and tracks their status.


## Database Design

The system's data is structured around key entities:

* **User:** Stores user details, including role and credentials.
* **University:** Contains information about each university.
* **Major:** Defines academic programs offered by universities.
* **Application:** Records student applications, their data, and status.
* **StaffUniversityAssignment:** A join table to manage staff assignments to universities.
* **UniversityRequirement:** Stores specific requirements for each university.

An Entity-Relationship (ER) diagram, along with a detailed SQL Schema, is provided in the full project documentation.


## Cloning the Project

To get a local copy of the project up and running, follow these steps:

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/Al-rimi/eduhub.git
    cd eduhub
    ```


## Installing Dependencies

Ensure you have Maven and Java 21 installed on your system.

1.  **Install Maven dependencies:**

    ```bash
    mvn clean install
    ```


## Running the Application

After installing the dependencies, you can run the Spring Boot application:

1.  **Start the application:**

    ```bash
    mvn spring-boot:run
    ```

    The application will typically be accessible at `http://localhost:8080` in your web browser.
