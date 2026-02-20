# Campus Closet 
## A Student Thrifting Mobile Platform

Campus Closet is a mobile application designed exclusively for university students to buy, sell, and exchange clothing within their campus community. The platform promotes affordability, sustainability, and safe peer-to-peer transactions within a verified student network.

This project is being developed as part of a Software Engineering course and follows an iterative sprint-based development process.

### Project Goals
- Create a secure, student-only marketplace
- Ensure authenticated access using university email verification
- Provide a structured system for listing and managing items
- Maintain reliable data storage and user management
- Apply software engineering principles including UML design, database modeling, and system implementation

### Tech Stack
- Platform: Native Android
- Language: Kotlin
- Database: SQLite (via Room ORM) [internal]  AND PostgreSQL (Hosted vis SUpabase [external])
- Backend Communication: Retrofit + OkHttp
- Session Management: SharedPreferences
- Architecture: Activity-based Android structure
- Version Control: GitHub

## Core Features (Current Implementation)
- User Management
    - User Registration (restricted to @student.gsu.edu emails)
    - Secure Login Authentication
    - Persistent Session Handling
    - Logout Functionality
    - Role support (User / Admin – extendable)
- Database System (PostgreSQL Hosted via Supabase)
- Room-based SQLite database
- Structured User entity
- DAO queries for insert, validation, and retrieval
- Verified persistent storage

### Database Overview

Table: Users

| Attribute      | Type              | Description               |
| ---------      | ----------------- | ------------------------- |
| userId         | Int (Primary Key) | Auto-generated unique ID  |
| name           | String            | Student name              |
| email          | String            | University email (unique) |
| password_hash  | String            | Stored password           |
| role           | String            | User role                 |

The application communicates with Supabase through REST API endpoints.
No local database installation or configuration required.


### System Design

The project includes:

Context Diagram
Activity Diagrams
Use Case Diagrams (4 Major Use Cases)
Database Schema Design
Sprint Planning & Scheduling Documentation

All diagrams follow standard UML conventions.

### Running the System
Prerequisites

- Java 11 or higher
- Gradle (wrapper included in project)
- Android SDK installed

### Build Instructions
This project uses the Gradle build system.

To build the project from the root directory:
./gradlew build

### On Windows:
gradlew.bat build

### To generate a debug APK:
./gradlew assembleDebug

### The generated APK will be located in:
app/build/outputs/apk/debug/

Database Configuration
- The system connects to a hosted Supabase PostgreSQL database.
- Backend is cloud-hosted and requires no local database installation.

Supabase Project URL: https://mqkevnprofrsrysciwxa.supabase.co

### Authentication:
- Public anon API key is embedded in the application build configuration.
- No additional database setup is required.

Running the Application
- After building the project, install the generated APK on an Android device or emulator.
- The application will automatically connect to the hosted Supabase database.
- Users may create an account using a valid @student.gsu.edu email address.

## Development Approach

The project follows a sprint-based model:

- Sprint 1: System planning and initial UML design

- Sprint 2: Database and user management implementation

Future Sprints: Marketplace features, item listings, transaction flows

👥 Team

Software Engineering Project
Group: 2
