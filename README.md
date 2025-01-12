<p align="center">
  <img src="src/main/resources/static/img/logo-white-520x520.png" alt="logo">
</p>
# MoneyTracker

A personal finance management application built with Spring Boot that helps users track their income, expenses, and manage multiple currency accounts.

## Features

- 💰 Multi-currency support
- 📊 Transaction tracking
- 💳 Multiple account management
- 📱 Responsive web interface
- 🔒 Secure user authentication

## Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Git

### Database
1. Create PostgreSQL database:
   ```sql
   CREATE DATABASE moneytracker;
   ```
2. Run database migrations (if any)

### Application Properties
1. Copy the example properties file:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
2. Edit `application.properties` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/moneytracker
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

The application will be available at `http://localhost:8080/moneytracker`

## Development
### Tech Stack
- Java 17
- Spring Boot 3.2.0
- PostgreSQL
- Maven

### Project Structure
- `src/main/java/lt/ehu/student/moneytracker/`
  - `model/` - Entity classes
  - `repository/` - Data access interfaces
  - `service/` - Business logic
  - `controller/` - Web controllers


### Database Schema
- `user` - User accounts and preferences
- `asset` - Bank accounts, cash accounts, etc.
- `transaction` - Financial transactions
- `currency` - Supported currencies
- `transaction_type` - Types of transactions

### Testing
```bash
# Run all tests
mvn test
# Run specific test class
mvn test -Dtest=UserServiceTest
```


### Code Style
- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use meaningful variable and method names
- Add comments for complex logic
- Keep methods small and focused

### Git Workflow
1. Create a feature branch:
   ```bash
   git checkout -b feature/new-feature
   ```
2. Make changes and commit:
   ```bash
   git add .
   git commit -m "Add new feature"
   ```
3. Push changes and create pull request:
   ```bash
   git push origin feature/new-feature
   ```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Acknowledgments
- Original version by Eugene Chekan (https://github.com/eugene-chekan)
- Spring Boot team for the excellent framework
- All contributors who participate in this project