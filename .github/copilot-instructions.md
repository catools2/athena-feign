# Athena Feign - GitHub Copilot Instructions

## Project Overview

Athena is a sophisticated software solution that systematically collects quality metrics throughout the Software Development Life Cycle (SDLC).
The system aims to proactively identify and address quality-related issues early in the development process, thereby reducing costs, improving efficiency, and enhancing overall product quality.
Athena Feign is a collection of clients and cli modules that interact with various Athena microservices to facilitate seamless communication and data exchange.

## Technology Stack

- **Java Version**: JDK 17
- **Build Tool**: Maven 3.8.6+ (using Maven Wrapper `./mvnw`)
- **Framework**: Spring Boot with Spring Data JPA, Spring Data REST, Spring Cloud OpenFeign
- **Database**: PostgreSQL (with Flyway migrations)
- **Testing**: JUnit 5, TestContainers, Gatling (performance testing)
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Code Quality**: SonarCloud integration with JaCoCo for coverage

## Architecture

Athena follows a modular microservices architecture with the following module types:

- **athena-boot-*** - Spring Boot microservices for different domains (core, git, kube, pipeline, tms, spec, metric)
- **athena-boot-*-feign** - Feign client modules for each microservice
- **athena-common** - Shared utilities and common code
- **athena-common-test** - Shared test utilities
- **athena-gateway** - API Gateway
- **athena-parent/athena-boot-parent** - Parent POMs with dependency management

### Data Sources

Athena collects metrics from:
- CI/CD pipelines
- Git repositories
- Kubernetes infrastructure
- Task management systems (Jira/Zephyr)
- OpenAPI (Swagger) documentation

## Build and Test Guidelines

### Building Locally

Requirements: Maven 3.8.6+, JDK-17, Docker

```shell
# Compile
./mvnw clean compile -U

# Install without tests
./mvnw install -DskipTests

# Build Docker images
./mvnw package docker:build -DskipTests
```

### Testing Strategy

The project uses a sophisticated testing approach with TestContainers:

1. **Unit Tests**: Standard JUnit tests with Spring Boot Test
2. **Integration Tests**: Use TestContainers to spin up PostgreSQL databases
3. **Performance Tests**: Gatling scenarios executed after functional tests

Test execution flow:
1. TestContainer starts golden database
2. Spring Boot app starts with database
3. Execute functional tests (JUnit)
4. Restart with new golden database
5. Execute performance tests (Gatling)
6. Clean up containers

### Running Tests

```shell
# Run tests
./mvnw test

# Run integration tests
./mvnw verify
```

## Coding Standards

### General Practices

- Use Lombok annotations (`@UtilityClass`, `@Data`, etc.) to reduce boilerplate
- Follow Spring Boot best practices and conventions
- Use `@RestController` for REST endpoints
- Use Spring Data JPA repositories with proper naming conventions
- Implement proper exception handling with custom exceptions in `athena-common`

### Package Structure

- `entity/` - JPA entities
- `model/` - DTOs and data models
- `mapper/` - MapStruct mappers (excluded from Sonar)
- `repository/` - Spring Data repositories
- `service/` - Business logic
- `controller/` or `rest/` - REST controllers
- `config/` - Spring configuration classes (excluded from Sonar)
- `exception/` - Custom exceptions (excluded from Sonar)

### Code Quality

- **SonarCloud** integration is enabled for code quality analysis
- **JaCoCo** is used for test coverage reporting
- Excluded from coverage: Application classes, config classes, models, entities, mappers, exceptions, test code

### Database

- Use **Flyway** for database migrations
- Default database: PostgreSQL
- Connection details configured via Spring Boot properties
- TestContainers used for integration tests

## API Design

- Use **SpringDoc OpenAPI** for API documentation
- Follow REST conventions (proper HTTP methods, status codes)
- Use Spring Data REST where appropriate for CRUD operations
- Implement validation using `@Valid` and constraint annotations

## Docker

- Docker images are built using the `docker-maven-plugin`
- Each microservice has its own Docker configuration
- Images can be built with: `./mvnw package docker:build -DskipTests`

## Dependencies

When adding or modifying dependencies:
- Update the appropriate parent POM (`athena-parent` or `athena-boot-parent`)
- Ensure version management is centralized in parent POMs
- Use Spring Boot's dependency management where possible
- Feign clients should be in separate modules (`*-feign`)

## Testing with TestContainers

- Tests use TestContainers to provide real PostgreSQL instances
- Golden database images are pulled from Docker Registry
- Database containers are automatically started and stopped during tests
- Ensure Docker is running when executing integration tests

## Module Dependencies

- Core modules should not depend on Feign client modules
- Feign client modules depend on their corresponding core modules
- Common functionality goes in `athena-common`
- Test utilities go in `athena-common-test`

## Performance Testing

- Use Gatling for performance scenarios
- Performance tests run as part of integration test phase
- Located in `src/it/java` directories
- Follow the population pattern for test data generation

## Important Notes

- All Spring Boot applications should extend base configuration from `athena-boot-parent`
- Use the Maven Wrapper (`./mvnw`) for consistent builds
- Docker must be available for building images and running integration tests
- Some modules skip tests/Sonar/Docker by default (configured in parent POM)

## Java/Spring Code Review Guidelines

### Quick Start
```bash
## To start a code review, use:
review [source-branch] to [target-branch]
## Example: review feature/XX-156493-dg to release/4.13.0
```

**Output:** The review will generate two documents:
1. **Code Review Report** - Detailed analysis with findings and recommendations
2. **Pull Request Description** - Pre-filled `PR_DESCRIPTION.md` file at project root, ready to copy/paste into Bitbucket

---

### Core Review Principles

#### 🎯 **Primary Focus Areas**
1. **Correctness** - Does the code solve the intended problem?
2. **Security** - Are there any security vulnerabilities?
3. **Performance** - Will this impact system performance?
4. **Maintainability** - Is the code readable and well-structured?

#### 🔍 **Review Process**
1. **Prepare Environment**
   ```bash
   git fetch origin
   git checkout [target-branch] && git pull
   git checkout [source-branch] && git pull
   ```

2. **Find Common Ancestor & Generate Diff**
   ```bash
   # Find the merge base (point where branches diverged)
   git merge-base [target-branch] [source-branch]
   
   # Get summary of changes in source branch only
   git diff $(git merge-base [target-branch] [source-branch])..[source-branch] --stat
   
   # Get detailed diff of source branch changes only
   git diff $(git merge-base [target-branch] [source-branch])..[source-branch]
   ```

   **Why merge-base?** This ensures we only review changes made in the source branch,
   not other people's commits that may have been added to the target branch after
   the source branch was created.

3. **Execute Review Checklist** (see below)

4. **Generate Pull Request Description**
    - Extract JIRA ticket from branch name
    - Summarize changes from diff
    - List modified files with change types
    - Pre-fill test coverage information
    - Include review findings summary
    - Output ready-to-paste PR description

---

### 📄 Pull Request Template Auto-Generation

When running a review, the system will automatically:

1. **Extract Information:**
    - JIRA ticket number from branch name (e.g., `feature/XX-156493-dg` → `XX-156493`)
    - Change statistics (files changed, lines added/removed)
    - Change types (new files, modified files, deleted files)

2. **Analyze Changes:**
    - Identify affected components (service, repository, controller, model, etc.)
    - Detect database changes (liquibase, flyway, SQL files)
    - Find configuration changes (properties, XML, YAML)
    - Check for breaking changes

3. **Populate Template:**
    - Fill in JIRA ticket link
    - Suggest change type based on analysis
    - Mark test-related checkboxes based on test files found
    - List breaking changes and database changes
    - Include summary of review findings

4. **Output Format:**
   The generated PR description will be written to `PR_DESCRIPTION.md` at the project root.
   Simply open the file and copy its entire contents to paste into Bitbucket's PR description field.

---

### 📋 Java/Spring Specific Checklist

#### **General Code Quality**
- [ ] Code correctness and logic
- [ ] No obvious bugs or edge case issues
- [ ] Proper error handling and exception management
- [ ] Code style follows project conventions
- [ ] Meaningful variable and method names

#### **Spring Framework Best Practices**
- [ ] **Transaction Management**: `@Transactional` only at service layer, not repository
- [ ] **Dependency Injection**: Proper use of `@Autowired`, `@Resource`, `@Qualifier`
- [ ] **Configuration**: Spring beans properly configured
- [ ] **Security**: No sensitive data in code or logs

#### **Architecture & Design**
- [ ] **Layer Separation**: Clear separation between controllers, services, repositories
- [ ] **Single Responsibility**: Classes have single, well-defined responsibilities
- [ ] **Dependency Direction**: Dependencies flow toward abstractions
- [ ] **Entity Relationships**: JPA entities properly mapped

#### **Testing Requirements**
- [ ] **Unit Tests**: New/modified service classes have corresponding tests
- [ ] **Integration Tests**: Complex flows are integration tested
- [ ] **Test Coverage**: Critical business logic is covered
- [ ] **Mock Usage**: Appropriate mocking of dependencies

#### **Code Style & Patterns**
- [ ] **Early Returns**: No unnecessary `else` blocks after `return` statements
- [ ] **Logging**: Include contextual information (IDs, parameters, state)
- [ ] **Exception Handling**: Specific exceptions with meaningful messages
- [ ] **Resource Management**: Proper cleanup of resources

#### **Performance & Scalability**
- [ ] **Database Operations**: Efficient queries, proper indexing considerations
- [ ] **Memory Usage**: No obvious memory leaks or excessive object creation
- [ ] **Caching**: Appropriate use of caching where beneficial
- [ ] **Async Processing**: Consider async operations for long-running tasks

#### **Security Considerations**
- [ ] **Input Validation**: All inputs properly validated
- [ ] **Authorization**: Proper access controls in place
- [ ] **Data Sanitization**: Sensitive data properly handled in logs
- [ ] **SQL Injection**: Parameterized queries used

---

### 🚨 Common Issues to Flag

#### **Critical Issues**
- Missing unit tests for new service classes
- `@Transactional` annotations in repository interfaces/classes
- Hardcoded credentials or sensitive information
- SQL injection vulnerabilities

#### **Code Quality Issues**
- Unnecessary `else` blocks after `return` statements
- Logging without sufficient context (missing IDs, parameters)
- Large methods that should be decomposed
- Inconsistent exception handling patterns

#### **Spring-Specific Issues**
- Improper transaction boundaries
- Circular dependencies
- Missing `@Component` or service annotations
- Incorrect scope usage (`@Singleton`, `@Prototype`)

---

### 💡 Review Tips

#### **Understanding Branch Comparisons**

**❌ Wrong Approach:**
```bash
git diff target-branch..source-branch
```
This compares ALL differences between branches, including changes others made to the target branch after your source branch was created.

**✅ Correct Approach:**
```bash
git diff $(git merge-base target-branch source-branch)..source-branch
```
This shows only changes made in your source branch since it diverged from the target branch.

**Alternative (Three-dot syntax):**
```bash
git diff target-branch...source-branch
```
The three-dot syntax automatically uses merge-base, showing only source branch changes.

#### **For Reviewers**
- Focus on business logic correctness first
- Check for proper Spring patterns and conventions
- Verify test coverage for critical paths
- Look for potential performance bottlenecks
- Ensure proper error handling and logging

#### **For Authors**
- Include test evidence in PR description
- Document any architectural decisions
- Highlight breaking changes or migration needs
- Provide context for complex business logic

### Troubleshooting: Mockito & Mocking Lambdas

If you see errors like:

```
org.mockito.exceptions.base.MockitoException: Cannot mock/spy class ... Lambda ...
Mockito cannot mock/spy because :  - VM does not support modification of given type
```

Use one of the following fixes:

1) Add the inline mock maker dependency (preferred)

```xml
<!-- Add to the test scope of the module's `pom.xml` -->
<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-inline</artifactId>
  <version>5.5.0</version> <!-- align with project Mockito version -->
  <scope>test</scope>
</dependency>
```

2) Or enable the inline mock maker via test resources (no dependency change)

- Create file: `src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker`
- File content:

```
mock-maker-inline
```

Either approach allows Mockito to mock final classes, lambdas and classes when the JVM otherwise blocks modification.

### Running tests (IntelliJ on macOS) / Common pitfalls

- Use the Maven wrapper in terminal:

```bash
./mvnw test
./mvnw -DskipTests install
./mvnw verify      # runs integration profiles
```

- In IntelliJ IDEA (use the project Maven runner to match CI behavior):
  - Run Maven goals from the Maven tool window (`test`, `verify`) instead of the IDE JUnit run configuration when you want the same classpath and resource loading as CI.
  - Ensure `src/test/resources` is on the test classpath (Project Structure -> Modules -> Sources).
  - If you experience Mockito lambda/mock failures in the IDE runner but not in Maven, enable the inline mock maker (see above) or run via `./mvnw test` to match CI.

- macOS-specific: ensure Java versions are consistent between IntelliJ and the `./mvnw` environment (use JDK 17 as required by the project).

### Quick Git / Repo note

- Recommended workflow when reviewing/fixing tests:

```bash
git fetch origin
git checkout v2 && git pull
# create a feature/fix branch from v2, implement fixes, run tests, push
```

- When generating PR descriptions or running code reviews, prefer using the `merge-base` approach to limit diffs to the source branch changes:

```bash
git diff $(git merge-base target-branch source-branch)..source-branch
```
