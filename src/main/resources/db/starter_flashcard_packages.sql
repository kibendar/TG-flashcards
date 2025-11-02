-- ==============================================================================
-- TELEGRAM FLASHCARDS BOT - STARTER PACKAGES INITIALIZATION SCRIPT
-- ==============================================================================
-- Database: PostgreSQL
-- Purpose: Populate initial flashcard packages for new users
--
-- IMPORTANT: This script should be run after the application has created
-- the database schema via Hibernate's ddl-auto=update
-- ==============================================================================

-- Create a default system user (ID 0) to own starter packages
-- This user will be used as the owner of public/starter packages
INSERT INTO account (id, current_flashcard, hard_card, hardest_card, start_study_time, end_study_time)
VALUES (0, NULL, 0, 0, NULL, NULL)
ON CONFLICT (id) DO NOTHING;

-- ==============================================================================
-- PACKAGE 1: Basic English Vocabulary
-- ==============================================================================
INSERT INTO flashcard_package (title, description, user_id)
VALUES ('Basic English Vocabulary', 'Essential English words for beginners. Learn common everyday vocabulary.', 0);

-- Get the last inserted package ID for flashcards
DO $$
DECLARE
    pkg_id BIGINT;
BEGIN
    SELECT id INTO pkg_id FROM flashcard_package WHERE title = 'Basic English Vocabulary' ORDER BY id DESC LIMIT 1;

    INSERT INTO flashcard (package_id, question, answer) VALUES
    (pkg_id, 'What does "hello" mean?', 'A greeting used when meeting someone'),
    (pkg_id, 'What does "goodbye" mean?', 'A parting phrase used when leaving'),
    (pkg_id, 'What does "thank you" mean?', 'An expression of gratitude'),
    (pkg_id, 'What does "please" mean?', 'A polite word used when making a request'),
    (pkg_id, 'What does "friend" mean?', 'A person you know well and like'),
    (pkg_id, 'What does "family" mean?', 'Parents, children, and relatives'),
    (pkg_id, 'What does "house" mean?', 'A building where people live'),
    (pkg_id, 'What does "water" mean?', 'A clear liquid essential for life'),
    (pkg_id, 'What does "food" mean?', 'Something you eat to stay alive'),
    (pkg_id, 'What does "book" mean?', 'A written or printed work with pages');
END $$;

-- ==============================================================================
-- PACKAGE 2: Programming Basics - Java
-- ==============================================================================
INSERT INTO flashcard_package (title, description, user_id)
VALUES ('Java Programming Fundamentals', 'Core Java concepts for beginners. Master the basics of Java programming.', 0);

DO $$
DECLARE
    pkg_id BIGINT;
BEGIN
    SELECT id INTO pkg_id FROM flashcard_package WHERE title = 'Java Programming Fundamentals' ORDER BY id DESC LIMIT 1;

    INSERT INTO flashcard (package_id, question, answer) VALUES
    (pkg_id, 'What is a class in Java?', 'A blueprint or template for creating objects that defines fields and methods'),
    (pkg_id, 'What is an object in Java?', 'An instance of a class that has state and behavior'),
    (pkg_id, 'What is inheritance?', 'A mechanism where a new class acquires properties and methods of an existing class'),
    (pkg_id, 'What is polymorphism?', 'The ability of objects to take many forms, allowing methods to do different things based on the object'),
    (pkg_id, 'What is encapsulation?', 'Hiding internal state and requiring interaction through methods (getters/setters)'),
    (pkg_id, 'What is abstraction?', 'Hiding implementation details and showing only essential features'),
    (pkg_id, 'What is a constructor?', 'A special method called when an object is created, used to initialize the object'),
    (pkg_id, 'What is the difference between JDK and JRE?', 'JDK (Java Development Kit) includes development tools, JRE (Java Runtime Environment) only runs Java programs'),
    (pkg_id, 'What is a static method?', 'A method that belongs to the class rather than instances, callable without creating an object'),
    (pkg_id, 'What is the main method signature?', 'public static void main(String[] args) - the entry point of a Java application');
END $$;

-- ==============================================================================
-- PACKAGE 3: Spring Framework Basics
-- ==============================================================================
INSERT INTO flashcard_package (title, description, user_id)
VALUES ('Spring Framework Essentials', 'Learn the core concepts of Spring Framework and Spring Boot.', 0);

DO $$
DECLARE
    pkg_id BIGINT;
BEGIN
    SELECT id INTO pkg_id FROM flashcard_package WHERE title = 'Spring Framework Essentials' ORDER BY id DESC LIMIT 1;

    INSERT INTO flashcard (package_id, question, answer) VALUES
    (pkg_id, 'What is Spring Boot?', 'An opinionated framework that simplifies Spring application development with auto-configuration'),
    (pkg_id, 'What is Dependency Injection?', 'A design pattern where objects receive dependencies from external sources rather than creating them'),
    (pkg_id, 'What is @Autowired annotation?', 'Marks a field, setter, or constructor for automatic dependency injection by Spring'),
    (pkg_id, 'What is @Component annotation?', 'Marks a class as a Spring-managed component, making it eligible for auto-detection'),
    (pkg_id, 'What is @Service annotation?', 'A specialization of @Component used for service layer classes'),
    (pkg_id, 'What is @Repository annotation?', 'A specialization of @Component used for data access layer classes'),
    (pkg_id, 'What is @Controller annotation?', 'Marks a class as a Spring MVC controller'),
    (pkg_id, 'What is @RestController?', '@Controller + @ResponseBody, used for RESTful web services'),
    (pkg_id, 'What is application.properties?', 'Configuration file for Spring Boot application settings'),
    (pkg_id, 'What is Spring JPA?', 'Java Persistence API implementation in Spring for database operations');
END $$;

-- ==============================================================================
-- PACKAGE 4: Database Fundamentals - SQL
-- ==============================================================================
INSERT INTO flashcard_package (title, description, user_id)
VALUES ('SQL & Database Basics', 'Master fundamental SQL queries and database concepts.', 0);

DO $$
DECLARE
    pkg_id BIGINT;
BEGIN
    SELECT id INTO pkg_id FROM flashcard_package WHERE title = 'SQL & Database Basics' ORDER BY id DESC LIMIT 1;

    INSERT INTO flashcard (package_id, question, answer) VALUES
    (pkg_id, 'What is SQL?', 'Structured Query Language - a language for managing and querying relational databases'),
    (pkg_id, 'What is a Primary Key?', 'A unique identifier for each record in a database table'),
    (pkg_id, 'What is a Foreign Key?', 'A field that links to the primary key of another table, establishing relationships'),
    (pkg_id, 'What does SELECT do?', 'Retrieves data from one or more tables'),
    (pkg_id, 'What does INSERT do?', 'Adds new records to a table'),
    (pkg_id, 'What does UPDATE do?', 'Modifies existing records in a table'),
    (pkg_id, 'What does DELETE do?', 'Removes records from a table'),
    (pkg_id, 'What is a JOIN?', 'Combines rows from two or more tables based on a related column'),
    (pkg_id, 'What is an INNER JOIN?', 'Returns records that have matching values in both tables'),
    (pkg_id, 'What is an index?', 'A database structure that improves the speed of data retrieval operations');
END $$;

-- ==============================================================================
-- PACKAGE 5: Git Version Control
-- ==============================================================================
INSERT INTO flashcard_package (title, description, user_id)
VALUES ('Git Version Control Basics', 'Essential Git commands and concepts for version control.', 0);

DO $$
DECLARE
    pkg_id BIGINT;
BEGIN
    SELECT id INTO pkg_id FROM flashcard_package WHERE title = 'Git Version Control Basics' ORDER BY id DESC LIMIT 1;

    INSERT INTO flashcard (package_id, question, answer) VALUES
    (pkg_id, 'What is Git?', 'A distributed version control system for tracking code changes'),
    (pkg_id, 'What does "git init" do?', 'Initializes a new Git repository in the current directory'),
    (pkg_id, 'What does "git clone" do?', 'Creates a copy of a remote repository on your local machine'),
    (pkg_id, 'What does "git add" do?', 'Stages changes for the next commit'),
    (pkg_id, 'What does "git commit" do?', 'Records staged changes to the repository with a message'),
    (pkg_id, 'What does "git push" do?', 'Uploads local commits to a remote repository'),
    (pkg_id, 'What does "git pull" do?', 'Fetches and merges changes from a remote repository'),
    (pkg_id, 'What is a branch?', 'A separate line of development that diverges from the main codebase'),
    (pkg_id, 'What does "git merge" do?', 'Combines changes from one branch into another'),
    (pkg_id, 'What is a merge conflict?', 'Occurs when Git cannot automatically merge changes and requires manual resolution');
END $$;

-- ==============================================================================
-- PACKAGE 6: Data Structures & Algorithms
-- ==============================================================================
INSERT INTO flashcard_package (title, description, user_id)
VALUES ('Data Structures & Algorithms', 'Fundamental data structures and algorithm concepts.', 0);

DO $$
DECLARE
    pkg_id BIGINT;
BEGIN
    SELECT id INTO pkg_id FROM flashcard_package WHERE title = 'Data Structures & Algorithms' ORDER BY id DESC LIMIT 1;

    INSERT INTO flashcard (package_id, question, answer) VALUES
    (pkg_id, 'What is an Array?', 'A fixed-size sequential collection of elements of the same type'),
    (pkg_id, 'What is a Linked List?', 'A linear data structure where elements are linked using pointers'),
    (pkg_id, 'What is a Stack?', 'A LIFO (Last In First Out) data structure with push and pop operations'),
    (pkg_id, 'What is a Queue?', 'A FIFO (First In First Out) data structure with enqueue and dequeue operations'),
    (pkg_id, 'What is a Binary Tree?', 'A tree structure where each node has at most two children'),
    (pkg_id, 'What is a Hash Table?', 'A data structure that maps keys to values using a hash function'),
    (pkg_id, 'What is Big O notation?', 'A mathematical notation describing the performance or complexity of an algorithm'),
    (pkg_id, 'What is O(n) complexity?', 'Linear time - execution time grows proportionally with input size'),
    (pkg_id, 'What is O(1) complexity?', 'Constant time - execution time remains the same regardless of input size'),
    (pkg_id, 'What is recursion?', 'A function that calls itself to solve smaller instances of the same problem');
END $$;

-- ==============================================================================
-- PACKAGE 7: PostgreSQL Specific
-- ==============================================================================
INSERT INTO flashcard_package (title, description, user_id)
VALUES ('PostgreSQL Essentials', 'PostgreSQL-specific features and commands.', 0);

DO $$
DECLARE
    pkg_id BIGINT;
BEGIN
    SELECT id INTO pkg_id FROM flashcard_package WHERE title = 'PostgreSQL Essentials' ORDER BY id DESC LIMIT 1;

    INSERT INTO flashcard (package_id, question, answer) VALUES
    (pkg_id, 'What is PostgreSQL?', 'An open-source, object-relational database management system (ORDBMS)'),
    (pkg_id, 'What is SERIAL in PostgreSQL?', 'An auto-incrementing integer type, shorthand for creating sequences'),
    (pkg_id, 'What is a sequence?', 'A database object that generates unique numeric identifiers'),
    (pkg_id, 'What is JSONB?', 'Binary JSON storage format in PostgreSQL, faster than regular JSON'),
    (pkg_id, 'What does VACUUM do?', 'Reclaims storage and updates statistics for query optimization'),
    (pkg_id, 'What is a schema in PostgreSQL?', 'A namespace that contains database objects like tables and views'),
    (pkg_id, 'What is the psql command?', 'PostgreSQL interactive terminal for executing queries'),
    (pkg_id, 'What does \dt do in psql?', 'Lists all tables in the current database'),
    (pkg_id, 'What is a materialized view?', 'A view that stores query results physically for faster access'),
    (pkg_id, 'What is EXPLAIN ANALYZE?', 'Shows the execution plan and actual runtime statistics of a query');
END $$;

-- ==============================================================================
-- PACKAGE 8: Mathematics - Basic Algebra
-- ==============================================================================
INSERT INTO flashcard_package (title, description, user_id)
VALUES ('Basic Algebra Concepts', 'Fundamental algebraic concepts and formulas.', 0);

DO $$
DECLARE
    pkg_id BIGINT;
BEGIN
    SELECT id INTO pkg_id FROM flashcard_package WHERE title = 'Basic Algebra Concepts' ORDER BY id DESC LIMIT 1;

    INSERT INTO flashcard (package_id, question, answer) VALUES
    (pkg_id, 'What is a variable?', 'A symbol (usually a letter) that represents an unknown or changing value'),
    (pkg_id, 'What is an equation?', 'A mathematical statement that two expressions are equal'),
    (pkg_id, 'What is the distributive property?', 'a(b + c) = ab + ac'),
    (pkg_id, 'What is the commutative property of addition?', 'a + b = b + a - order does not matter'),
    (pkg_id, 'What is the associative property?', '(a + b) + c = a + (b + c) - grouping does not matter'),
    (pkg_id, 'What is a quadratic equation?', 'An equation of the form ax² + bx + c = 0'),
    (pkg_id, 'What is the quadratic formula?', 'x = (-b ± √(b² - 4ac)) / 2a'),
    (pkg_id, 'What is a linear equation?', 'An equation where the highest power of the variable is 1'),
    (pkg_id, 'What is slope?', 'The measure of steepness of a line, calculated as rise/run or (y₂-y₁)/(x₂-x₁)'),
    (pkg_id, 'What is slope-intercept form?', 'y = mx + b, where m is slope and b is y-intercept');
END $$;

-- ==============================================================================
-- PACKAGE 9: Design Patterns
-- ==============================================================================
INSERT INTO flashcard_package (title, description, user_id)
VALUES ('Software Design Patterns', 'Common design patterns in software development.', 0);

DO $$
DECLARE
    pkg_id BIGINT;
BEGIN
    SELECT id INTO pkg_id FROM flashcard_package WHERE title = 'Software Design Patterns' ORDER BY id DESC LIMIT 1;

    INSERT INTO flashcard (package_id, question, answer) VALUES
    (pkg_id, 'What is the Singleton pattern?', 'Ensures a class has only one instance and provides global access to it'),
    (pkg_id, 'What is the Factory pattern?', 'Creates objects without specifying the exact class to create'),
    (pkg_id, 'What is the Observer pattern?', 'Defines a one-to-many dependency so when one object changes, dependents are notified'),
    (pkg_id, 'What is the Strategy pattern?', 'Defines a family of algorithms and makes them interchangeable'),
    (pkg_id, 'What is the Decorator pattern?', 'Adds new functionality to objects dynamically without altering their structure'),
    (pkg_id, 'What is the MVC pattern?', 'Model-View-Controller: separates data, presentation, and control logic'),
    (pkg_id, 'What is the Repository pattern?', 'Mediates between domain and data mapping layers using a collection-like interface'),
    (pkg_id, 'What is the Builder pattern?', 'Constructs complex objects step by step, separating construction from representation'),
    (pkg_id, 'What is the Adapter pattern?', 'Converts the interface of a class into another interface clients expect'),
    (pkg_id, 'What is the Facade pattern?', 'Provides a simplified interface to a complex subsystem');
END $$;

-- ==============================================================================
-- PACKAGE 10: Web Development Basics
-- ==============================================================================
INSERT INTO flashcard_package (title, description, user_id)
VALUES ('Web Development Fundamentals', 'Essential concepts for web development.', 0);

DO $$
DECLARE
    pkg_id BIGINT;
BEGIN
    SELECT id INTO pkg_id FROM flashcard_package WHERE title = 'Web Development Fundamentals' ORDER BY id DESC LIMIT 1;

    INSERT INTO flashcard (package_id, question, answer) VALUES
    (pkg_id, 'What is HTML?', 'HyperText Markup Language - the standard markup language for web pages'),
    (pkg_id, 'What is CSS?', 'Cascading Style Sheets - describes how HTML elements are displayed'),
    (pkg_id, 'What is JavaScript?', 'A programming language that enables interactive web pages'),
    (pkg_id, 'What is HTTP?', 'HyperText Transfer Protocol - protocol for transferring web data'),
    (pkg_id, 'What is HTTPS?', 'HTTP Secure - encrypted version of HTTP using SSL/TLS'),
    (pkg_id, 'What is a REST API?', 'Representational State Transfer - an architectural style for web services'),
    (pkg_id, 'What is GET request?', 'HTTP method for retrieving data from a server'),
    (pkg_id, 'What is POST request?', 'HTTP method for sending data to create/update resources'),
    (pkg_id, 'What is JSON?', 'JavaScript Object Notation - lightweight data interchange format'),
    (pkg_id, 'What is the DOM?', 'Document Object Model - programming interface for HTML documents');
END $$;

-- ==============================================================================
-- END OF SCRIPT
-- ==============================================================================

-- Verification query to check installed packages
SELECT
    fp.id,
    fp.title,
    fp.description,
    COUNT(f.id) as flashcard_count
FROM flashcard_package fp
LEFT JOIN flashcard f ON f.package_id = fp.id
WHERE fp.user_id = 0
GROUP BY fp.id, fp.title, fp.description
ORDER BY fp.id;
