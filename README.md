</div>
<div align="center">
  <h3>🔍 Learn, Review, Discuss! 🔍</h3>
  <p><b>Dive into the code, share your insights, and join the conversation!</b></p>
  <p>⭐ Star if you find this helpful ⭐</p>
  <p>
    <a href="https://github.com/your-username/jooq-workshop/discussions">💬 Discuss Best Practices</a> •
    <a href="https://github.com/your-username/jooq-workshop/issues">🐞 Report Issues</a> •
    <a href="#contribution-guide">👥 Contribute</a>
  </p>
</div>

# Spring Boot jOOQ Research
A demonstration project showcasing how to use jOOQ with Spring Boot for building efficient database operations in a real estate management application.

## Project Overview
This research demonstrates the integration of jOOQ (Java Object Oriented Querying) with Spring Boot to create a real estate management system. The application manages offices, brokers, properties, and related data with a focus on efficient SQL generation and type-safe queries.

### Key Features
- **Type-safe SQL queries** using jOOQ's DSL
- **Advanced querying techniques** including nested queries, multisets, and record mapping
- **Real estate domain model** with complex relationships
- **REST API** for data import and export operations
- **PostgreSQL database** integration

## Technology Stack
- Java 21
- Spring Boot 3.4.4
- jOOQ
- PostgreSQL
- Lombok
- Spring Data JPA
- Spring Validation
- SpringDoc OpenAPI

## Database Structure
The application uses a PostgreSQL database with the following main entities:
- Offices
- Brokers
- Properties
- Addresses
- Images
- Emails
- Phone numbers

The schema includes various relationships including one-to-many, many-to-many, and complex hierarchical data structures.

## API Endpoints
### Export API
- `GET /api/v1/export/offices/{officeId}/properties/{propertyId}` - Get property details
- `GET /api/v1/export/offices/{officeId}/brokers/{brokerId}/properties` - Get properties for a broker
- `GET /api/v1/export/offices` - Get all offices

### Import API
- `POST /api/v1/export/brokers` - Create a new broker
- `PUT /api/v1/export/brokers/{brokerId}` - Update an existing broker
- `DELETE /api/v1/export/brokers/{brokerId}` - Delete a broker

## Getting Started
### Prerequisites
- Java 21 or higher
- PostgreSQL database
- Maven

### Configuration
1. Clone the repository
2. Create a `.env` file in the project root with the following properties:
   ```
   POSTGRES_USERNAME=your_username
   POSTGRES_PASSWORD=your_password
   POSTGRES_URL=jdbc:postgresql://localhost:5432/your_database
   ```
3. Run the SQL scripts in the following order:
   - `init.sql` to create the schema
   - `data.sql` to populate it with sample data

### Running the Application
```bash
mvn clean install
mvn spring-boot:run
```

Access the OpenAPI documentation at: http://localhost:8080/swagger-ui.html

## jOOQ Code Generation
The project uses jOOQ's code generation to create Java classes based on the database schema. The configuration is in the `pom.xml` file.

To generate jOOQ classes:
```bash
mvn clean generate-sources
```

## Research practices
- Type-safe SQL queries with jOOQ
- Structured DTO mapping 
- Efficient nested query handling
- Complex data relationships
- Transactional operations
- Error handling strategies
- API documentation with OpenAPI

## Research Learning Objectives
- Understanding jOOQ integration with Spring Boot
- Mastering type-safe SQL query construction
- Handling complex database operations
- Building efficient data mapping strategies
- Implementing clean repository patterns
