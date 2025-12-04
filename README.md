# Telegram Flashcards Bot

A sophisticated Telegram bot for creating, managing, and learning with flashcards. Built with Spring Boot and designed to help users master any subject through interactive learning sessions with intelligent repetition.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Database Schema](#database-schema)
- [Usage](#usage)
- [Commands](#commands)
- [Development](#development)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

## Features

### Core Features
- **Interactive Flashcard Learning**: Browse and learn from flashcard packages with an intuitive Telegram interface
- **Intelligent Repetition System**: Cards you find difficult are automatically repeated for better retention
- **Difficulty Rating**: Rate your knowledge from 0% to 100% for each flashcard
- **Session Management**: Start, pause, and stop learning sessions at any time
- **Package Browsing**: View all available flashcard packages with descriptions and previews
- **Progress Tracking**: Track hard cards and hardest cards during learning sessions

### Learning Algorithm
- **0-25% (Hardest)**: Card duplicated twice in the learning queue
- **25-50% (Hard)**: Card duplicated once in the learning queue
- **75-100% (Easy)**: Card moved to repetition list for final review

### User Experience
- Markdown-formatted messages for better readability
- Inline keyboard buttons for seamless interaction
- Comprehensive help and guide messages
- Error handling with user-friendly messages
- **Robust session validation**: Prevents errors when users aren't in active learning sessions
- Graceful error messages guide users back to valid states

## Technology Stack

- **Java 25**: Latest Java features and performance improvements
- **Spring Boot 3.2.2**: Enterprise-grade application framework
- **Spring Data JPA**: Object-relational mapping and database interaction
- **PostgreSQL**: Robust relational database for data persistence
- **TelegramBots API 6.9.7.0**: Official Telegram Bot API client
- **Lombok**: Reduces boilerplate code
- **SLF4J + Logback**: Comprehensive logging framework
- **Maven**: Dependency management and build automation
- **JUnit 5**: Modern testing framework
- **Mockito**: Mocking framework for unit tests
- **AssertJ**: Fluent assertions library

## Architecture

### Design Pattern
The application follows a **layered architecture** pattern with **SOLID principles**:

```
┌─────────────────────────────────────┐
│     Telegram Bot API (External)     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Controller Layer                │
│  - MainController                    │
│  - StartController                   │
│  - HelpController                    │
│  - EducationController               │
│  - ShowAllPackagesController         │
│  - StopController                    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│    Service Interface Layer           │
│  - IUserService                      │
│  - IEducationService                 │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  Service Implementation Layer        │
│  - StartService                      │
│  - HelpService                       │
│  - EducationService                  │
│  - ShowAllPackagesService            │
│  - StopService                       │
│  - UserService                       │
│  - FlashcardService                  │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     Repository Layer                 │
│  - UserRepository                    │
│  - FlashcardRepository               │
│  - FlashcardPackageRepository        │
│  - FlashcardEducationListRepository  │
│  - FlashcardRepetitionListRepository │
│  - FlashcardStatusRepository         │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      PostgreSQL Database             │
└──────────────────────────────────────┘
```

![Diagram of classes](~/JavaProject/TG-flashcards/class-diagram.pdf)

### SOLID Principles Implementation

The project follows **SOLID principles** for better maintainability and testability:

- **Single Responsibility Principle (SRP)**: Each service class has a single, well-defined responsibility
  - `UserService`: Manages user data operations
  - `EducationService`: Handles learning session logic
  - `StopService`: Manages session termination

- **Open/Closed Principle (OCP)**: Classes are open for extension but closed for modification through interface-based design

- **Liskov Substitution Principle (LSP)**: Service implementations can be substituted with their interface contracts

- **Interface Segregation Principle (ISP)**: Focused interfaces (`IUserService`, `IEducationService`) contain only relevant methods

- **Dependency Inversion Principle (DIP)**: High-level modules (controllers) depend on abstractions (interfaces) rather than concrete implementations

### Key Components

**Controllers**: Handle incoming Telegram updates and route to appropriate services. Controllers depend on service interfaces for loose coupling.

**Service Interfaces**: Define contracts for business operations, enabling dependency inversion and easier testing.

**Service Implementations**: Contain business logic and coordinate between repositories. Each service follows SRP with focused responsibilities.

**Repositories**: Data access layer using Spring Data JPA for database operations.

**Models**: Entity classes representing database tables with JPA annotations.

## Prerequisites

- **Java 25** or higher
- **PostgreSQL 12** or higher
- **Maven 3.6** or higher
- **Telegram Bot Token** (obtain from [@BotFather](https://t.me/BotFather))

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/telegram-flashcards-bot.git
cd telegram-flashcards-bot
```

### 2. Set Up PostgreSQL Database

```bash
# Create database and user
sudo -u postgres psql
CREATE DATABASE telegram_flashcards_bot;
CREATE USER telegram_flashcards_bot WITH PASSWORD 'telegram_flashcards_bot';
GRANT ALL PRIVILEGES ON DATABASE telegram_flashcards_bot TO telegram_flashcards_bot;
\q
```

### 3. Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Bot Configuration
bot.name=YourBotName
bot.token=YOUR_BOT_TOKEN_HERE

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/telegram_flashcards_bot
spring.datasource.username=telegram_flashcards_bot
spring.datasource.password=your_secure_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server Configuration
server.port=8080
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR file:

```bash
java -jar target/telegram-flashcards-bot-0.0.1-SNAPSHOT.jar
```

## Configuration

### Environment Variables (Alternative to application.properties)

You can also configure the bot using environment variables:

```bash
export BOT_NAME="YourBotName"
export BOT_TOKEN="YOUR_BOT_TOKEN"
export DB_URL="jdbc:postgresql://localhost:5432/telegram_flashcards_bot"
export DB_USERNAME="telegram_flashcards_bot"
export DB_PASSWORD="your_secure_password"
```

### Logging Configuration

Logging is configured in `src/main/resources/logback.xml`:

- **Console Logging**: INFO level for development
- **File Logging**: WARN level in `logs/flashcards.log`
- **Rolling Policy**: Daily rotation with 30-day retention

## Database Schema

### Core Tables

#### `account` (User)
- `id` (BIGINT, PK): Telegram chat ID
- `current_flashcard` (BIGINT): Current position in learning session
- `start_study_time` (TIMESTAMP): Session start time
- `end_study_time` (TIMESTAMP): Session end time
- `hard_card` (BIGINT): Count of hard cards in current session
- `hardest_card` (BIGINT): Count of hardest cards in current session

#### `flashcard_package`
- `id` (BIGINT, PK): Auto-generated ID
- `title` (VARCHAR): Package name
- `description` (TEXT): Package description
- `user_id` (BIGINT, FK): Owner user

#### `flashcard`
- `id` (BIGINT, PK): Auto-generated ID
- `question` (TEXT): Flashcard question
- `answer` (TEXT): Flashcard answer
- `package_id` (BIGINT, FK): Parent package

### Session Tables (Temporary)

#### `flashcard_education_list`
- Composite PK: `(id, user_id)`
- `flashcard_id` (BIGINT, FK): Referenced flashcard
- Cleared after learning session

#### `flashcard_repetition_list`
- Composite PK: `(id, user_id)`
- `flashcard_id` (BIGINT, FK): Referenced flashcard
- Cleared after learning session

#### `flashcard_status`
- Composite PK: `(user_id, flashcard_id)`
- `number_of_duplicated_cards` (INTEGER): Duplication count
- Cleared after learning session

### Entity Relationships

```
User (1) ────────────── (*) FlashcardPackage
                              │
                              │ (1)
                              │
                              ▼ (*)
                         Flashcard
                              │
                              ├── (*) FlashcardEducationList
                              ├── (*) FlashcardRepetitionList
                              └── (*) FlashcardStatus
```

## Usage

### Getting Started

1. **Start the bot**: Send `/start` to your bot in Telegram
2. **Read the guide**: Click "Get Guide" button or use `/help`
3. **Browse packages**: Use `/showallpackages` to see available flashcard packages
4. **Start learning**: Click on a package, then click "Start education"
5. **Answer cards**: Rate each flashcard from 0% to 100%
6. **Stop anytime**: Use `/stop` to end your learning session

### Learning Workflow

```
/showallpackages
      │
      ▼
Select Package
      │
      ▼
View Description
      │
      ▼
Click "Start education"
      │
      ▼
View Question ──► Click "Show answer"
      │                   │
      │                   ▼
      │          Rate Difficulty (0-100%)
      │                   │
      │                   ▼
      └──────────► Next Question
                          │
                          ▼
                   Repetition Phase
                          │
                          ▼
                  Completion Message
```

## Commands

| Command | Description |
|---------|-------------|
| `/start` | Get a welcome message and introduction to the bot |
| `/showallpackages` | Browse all available flashcard packages |
| `/stop` | Stop your current learning session (progress not saved) |
| `/help` | Display comprehensive help message with usage instructions |

## Development

### Project Structure

```
telegram-flashcards-bot/
├── src/
│   ├── main/
│   │   ├── java/bot/telegram/flashcards/
│   │   │   ├── config/
│   │   │   │   ├── BotConfig.java
│   │   │   │   └── BotInitializer.java
│   │   │   ├── controller/
│   │   │   │   ├── MainController.java
│   │   │   │   ├── StartController.java
│   │   │   │   ├── HelpController.java
│   │   │   │   ├── EducationController.java
│   │   │   │   ├── ShowAllPackagesController.java
│   │   │   │   └── StopController.java
│   │   │   ├── service/
│   │   │   │   ├── interfaces/
│   │   │   │   │   ├── IUserService.java
│   │   │   │   │   └── IEducationService.java
│   │   │   │   ├── StartService.java
│   │   │   │   ├── HelpService.java
│   │   │   │   ├── EducationService.java
│   │   │   │   ├── ShowAllPackagesService.java
│   │   │   │   ├── StopService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── FlashcardService.java
│   │   │   ├── models/
│   │   │   │   ├── User.java
│   │   │   │   ├── Flashcard.java
│   │   │   │   ├── FlashcardPackage.java
│   │   │   │   └── temporary/
│   │   │   │       ├── FlashcardEducationList.java
│   │   │   │       ├── FlashcardRepetitionList.java
│   │   │   │       └── FlashcardStatus.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── FlashcardRepository.java
│   │   │   │   ├── FlashcardPackageRepository.java
│   │   │   │   ├── FlashcardEducationListRepository.java
│   │   │   │   ├── FlashcardRepetitionListRepository.java
│   │   │   │   └── FlashcardStatusRepository.java
│   │   │   ├── misc/
│   │   │   │   └── FlashcardAnswerStatus.java
│   │   │   └── TgFlashcardsBotApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application.uat.properties
│   │       └── logback.xml
│   └── test/
│       └── java/bot/telegram/flashcards/
│           └── service/
│               ├── StopServiceTest.java
│               ├── UserServiceTest.java
│               ├── StartServiceTest.java
│               └── HelpServiceTest.java
├── logs/
│   └── flashcards.log
├── pom.xml
├── README.md
├── LICENSE
└── .gitignore
```

### Adding New Features

1. **Create a new controller** for handling bot commands
2. **Create a corresponding service** for business logic
3. **Add repository methods** if database access is needed
4. **Register the controller** in MainController
5. **Add bot command** to the command list in MainController constructor
6. **Write tests** for your new feature

### Code Style Guidelines

- Use **Lombok** annotations to reduce boilerplate
- Follow **Spring Boot best practices**
- Write **comprehensive JavaDoc** for public methods
- Use **meaningful variable names**
- Keep methods **focused and single-purpose**
- Add **null checks** and validation
- Log **errors and warnings** appropriately

## Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run tests with coverage
mvn test jacoco:report
```

### Test Structure

- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions
- **Repository Tests**: Test database operations

### Test Coverage

The project uses **Test-Driven Development (TDD)** practices and aims for:
- **80%+ code coverage** for service layer
- **70%+ code coverage** for controller layer
- **100% coverage** for critical business logic

Current test suite includes:
- `StopServiceTest`: Tests for session termination functionality
- `UserServiceTest`: Tests for user data operations
- `StartServiceTest`: Tests for welcome and onboarding features
- `HelpServiceTest`: Tests for help command functionality

### Example Test

```java
@Test
@DisplayName("Should return error message when user is not in a learning session")
void testStopLearningSession_WhenUserNotInSession_ReturnsErrorMessage() {
    // Given
    user.setCurrentFlashcard(null);
    when(userService.getUser(CHAT_ID)).thenReturn(user);

    // When
    SendMessage result = stopService.stopLearningSession(CHAT_ID);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getChatId()).isEqualTo(String.valueOf(CHAT_ID));
    assertThat(result.getText()).contains("You are not currently in a learning session");

    // Verify that clearTemporaryResources was never called
    verify(educationService, never()).clearTemporaryResourcesAfterEducation(anyLong());
}
```

## API Documentation

### Main Controller Endpoints

#### Message Handlers

- **`/start`**: Initializes user and displays welcome message
- **`/help`**: Shows comprehensive help documentation
- **`/showallpackages`**: Lists all flashcard packages
- **`/stop`**: Stops current learning session

#### Callback Query Handlers

- **`GET_GUIDE_BUTTON_CLICKED`**: Displays quick start guide
- **`SHOW_ANSWER_CLICKED`**: Reveals flashcard answer
- **`0%_BUTTON_CLICKED`**: Rates card as "Don't know" (hardest)
- **`25%_BUTTON_CLICKED`**: Rates card as hard
- **`50%_BUTTON_CLICKED`**: Rates card as moderate
- **`75%_BUTTON_CLICKED`**: Rates card as easy
- **`100%_BUTTON_CLICKED`**: Rates card as mastered
- **`FLASHCARD_PACKAGE_{id}_SELECTED`**: Starts learning session for package
- **`SHOW_ALL_PACKAGES_{id}_SELECTED`**: Shows package description
- **`FIRST|PREVIOUS|NEXT_CARD_{id}_OF_PACKAGE_{id}_CLICKED`**: Navigates package preview

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Contribution Guidelines

- Write tests for new features
- Update documentation
- Follow existing code style
- Add JavaDoc comments
- Ensure all tests pass

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [TelegramBots Java Library](https://github.com/rubenlagus/TelegramBots)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [PostgreSQL](https://www.postgresql.org/)

## Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Contact the maintainer

## Roadmap

### Planned Features

- [ ] User-created flashcard packages
- [ ] Spaced repetition algorithm (SM-2)
- [ ] Export/import flashcards
- [ ] Multi-language support
- [ ] Collaborative flashcard packages
- [ ] Mobile-responsive web interface
- [ ] AI-generated flashcards
- [ ] Voice input for answers
- [ ] Image support in flashcards

## Changelog

### Version 1.0.0 (Current)
- Initial release
- Basic flashcard learning functionality
- Package browsing
- Intelligent repetition system
- Session management
- Comprehensive error handling

---

**Made with ❤️ using Java and Spring Boot**
