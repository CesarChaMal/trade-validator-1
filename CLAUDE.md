# Trade Validator - AI Assistant Guide

## Project Overview

**Trade Validator** is a Spring Boot REST API service that validates Foreign Exchange (FX) trades including Spot, Forward, and Vanilla Options. This was originally a coding challenge implementation with a focus on business rule validation and external service integration.

### Key Technologies
- **Framework**: Spring Boot 1.5.4
- **Build Tool**: Maven
- **Java Version**: 1.8
- **Testing**: JUnit 4, Spock Framework (Groovy), REST Assured
- **Documentation**: Swagger/Springfox 2.7.0
- **Metrics**: Dropwizard Metrics
- **Utilities**: Lombok, Apache Commons

## Project Structure

```
trade-validator-1/
├── src/
│   ├── main/
│   │   ├── java/priv/rdo/trade/
│   │   │   ├── Application.java              # Main Spring Boot application
│   │   │   ├── config/                       # Configuration classes
│   │   │   │   ├── CommonExceptionHandler.java
│   │   │   │   ├── HttpClientConfig.java
│   │   │   │   └── MarshallingConfig.java
│   │   │   ├── endpoint/                     # REST controllers
│   │   │   │   └── TradeValidationEndpoint.java
│   │   │   ├── model/                        # Data models
│   │   │   │   ├── input/                    # Request models
│   │   │   │   │   ├── Trade.java            # Base trade class with polymorphic JSON
│   │   │   │   │   ├── Spot.java
│   │   │   │   │   ├── Forward.java
│   │   │   │   │   ├── Option.java
│   │   │   │   │   ├── CurrencyPair.java
│   │   │   │   │   ├── Customer.java
│   │   │   │   │   ├── TradeType.java
│   │   │   │   │   ├── TradeDirection.java
│   │   │   │   │   └── OptionStyle.java
│   │   │   │   └── output/                   # Response models
│   │   │   │       ├── ValidationResult.java
│   │   │   │       ├── TradeValidationResult.java
│   │   │   │       ├── BulkTradeValidationResult.java
│   │   │   │       └── ValidationStatus.java
│   │   │   ├── swagger/                      # Swagger configuration
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   └── SwaggerController.java
│   │   │   └── validation/                   # Validation framework
│   │   │       ├── TradeValidationService.java
│   │   │       ├── ValidationManager.java
│   │   │       ├── ValidationChain.java
│   │   │       ├── external/                 # External service integrations
│   │   │       │   └── holidays/
│   │   │       │       ├── HolidayCheckService.java
│   │   │       │       ├── HolidayCheckServiceErrorHandler.java
│   │   │       │       ├── HolidayCheckServiceNotWorkingException.java
│   │   │       │       ├── HolidayApiResponse.java
│   │   │       │       └── Holiday.java
│   │   │       └── validators/               # Validation implementations
│   │   │           ├── Validator.java        # Base validator interface
│   │   │           ├── common/               # Cross-trade-type validators
│   │   │           ├── spot/                 # Spot trade validators
│   │   │           ├── forward/              # Forward trade validators
│   │   │           └── option/               # Option trade validators
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── groovy/priv/rdo/trade/           # Spock unit tests
│       ├── java/priv/rdo/trade/             # JUnit integration tests
│       │   └── endpoint/
│       │       └── TradeValidationEndpointIT.java
│       └── resources/
│           └── requests/                     # Test JSON payloads
│               ├── spot_ok.json
│               ├── spot_bad_*.json
│               ├── option_*.json
│               └── very_bad.json
├── pom.xml                                   # Maven configuration
├── readme.md                                 # Project README
└── notes.txt                                 # API references for holidays
```

## Architecture Patterns

### 1. Chain of Responsibility Pattern
The validation system uses a flexible chain-of-responsibility pattern:

- **ValidationChain**: Holds a list of validators and executes them
- **ValidationManager**: Discovers all Spring beans implementing `Validator` interface and adds them to the chain
- **Validator Interface**: Each validator implements:
  - `shouldValidate(Trade)`: Determines if validator applies to this trade type
  - `validate(T)`: Performs the actual validation logic

### 2. Polymorphic JSON Deserialization
The `Trade` class uses Jackson's `@JsonTypeInfo` and `@JsonSubTypes` annotations to deserialize JSON into specific trade types (Spot, Forward, Option) based on the `type` field.

### 3. Validation Strategy
- **Syntax validation**: Handled by Jackson deserialization and javax.validation annotations
- **Business validation**: Handled by custom validator chain
- **All errors returned**: Multiple validation errors are collected and returned together

## Key Conventions

### Code Style

1. **Lombok Usage**: Extensive use of `@Getter`, `@Setter`, `@ToString` for data classes
2. **Logging**: Uses SLF4J XLogger with entry/exit logging for trace debugging
3. **Formatter Comments**: Code uses `//@formatter:off` and `//@formatter:on` to control IDE formatting, especially in test fluent assertions

### Naming Conventions

- **Packages**: lowercase, following domain structure (e.g., `priv.rdo.trade.validation.validators.common`)
- **Classes**: PascalCase
- **Methods**: camelCase
- **Constants**: UPPER_SNAKE_CASE
- **Test Classes**:
  - Unit tests (Groovy/Spock): `*Test.groovy`
  - Integration tests (JUnit): `*IT.java`

### Dependency Injection

- **Constructor injection** is preferred (see `TradeValidationEndpoint`, `ValidationManager`)
- Spring auto-discovers components via `@Component`, `@Service`, `@RestController` annotations
- No XML configuration - pure Java/annotation-based configuration

## Development Workflow

### Running the Application

```bash
# Method 1: Run main class
# Execute Application.main() in your IDE

# Method 2: Maven
mvn spring-boot:run

# Method 3: Build and run JAR
mvn clean package
java -jar target/trade-validator-1.0-SNAPSHOT.jar
```

**Default Port**: 8080

### Endpoints

- `POST /trades` - Validate a single trade
- `POST /bulkTrades` - Validate multiple trades
- `GET /documentation` - Swagger UI
- `GET /metrics/*` - Dropwizard metrics

### Running Tests

```bash
# Run all tests (unit + integration)
mvn clean verify

# Run only unit tests
mvn test

# Run only integration tests
mvn integration-test
```

**Test Execution Notes**:
- Integration tests use `@SpringBootTest(webEnvironment = RANDOM_PORT)`
- REST Assured is used for API testing
- Spock tests use Groovy with expressive `where:` clauses for parameterized tests

### Building

```bash
# Clean build
mvn clean package

# Skip tests
mvn clean package -DskipTests

# With integration tests
mvn clean verify
```

## Testing Conventions

### Unit Tests (Spock/Groovy)

Located in `src/test/groovy/`, these tests:
- Use Spock Framework syntax (`given:`, `when:`, `then:`, `where:`)
- Test individual classes in isolation
- Use `@Unroll` for parameterized test display
- Example: `CurrencyPairTest.groovy`

```groovy
@Unroll
def "should parse input(#input) to objects"() {
    when:
        CurrencyPair result = new CurrencyPair(input)
    then:
        result.getLeftCurrency() == expectedLeftCurrency
    where:
        input      || expectedLeftCurrency | expectedRightCurrency
        "EURUSD"   || EURO                 | DOLLAR
}
```

### Integration Tests (JUnit)

Located in `src/test/java/`, these tests:
- Use `@SpringBootTest` with `RANDOM_PORT`
- Test full HTTP request/response cycle
- Use REST Assured for fluent API testing
- Load test JSON from `src/test/resources/requests/`
- Example: `TradeValidationEndpointIT.java`

```java
@Test
public void tradesOk() throws Exception {
    given()
        .port(port)
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .body(fileToString("spot_ok.json"))
    .when()
        .post("/trades")
    .then()
        .statusCode(OK.value())
        .body("validationStatus", is("SUCCESS"));
}
```

### Test Data

- Test JSON files are stored in `src/test/resources/requests/`
- `TestFileUtils.fileToString()` helper loads test data
- Naming pattern: `<trade_type>_<scenario>.json` (e.g., `spot_ok.json`, `option_bad_dates.json`)

## External Dependencies

### Holiday API
The service integrates with HolidayAPI (https://holidayapi.com/) to validate working days:
- API key stored in `application.properties` as `holidays.key`
- Free tier: 500 calls/month (can be renewed)
- Used by validators to check if dates fall on holidays/weekends
- See `notes.txt` for alternative API options

### Configuration
Key application properties in `application.properties`:
- `management.security.enabled = false` - Disables actuator security
- `logging.level.priv.rdo = TRACE` - Debug logging
- `holidays.key` - HolidayAPI key (must be valid)

## Common Development Tasks

### Adding a New Validator

1. **Create validator class** in appropriate package:
   ```java
   @Component
   public class MyValidator implements Validator<Trade> {
       @Override
       public boolean shouldValidate(Trade input) {
           // Return true if this validator applies
           return true;
       }

       @Override
       public ValidationResult validate(Trade input) {
           // Perform validation
           if (isValid) {
               return ValidationResult.success();
           }
           return ValidationResult.failure("Error message");
       }
   }
   ```

2. **Add `@Component` annotation** - Spring will auto-discover it
3. **ValidationManager will automatically add it** to the validation chain
4. **Write tests** in corresponding test package

### Adding a New Trade Type

1. **Create model class** extending `Trade` in `model/input/`
2. **Add `@JsonSubTypes.Type`** annotation to `Trade` class
3. **Add constant** to `TradeType` class
4. **Create specific validators** in new package under `validators/`
5. **Write integration tests** with test JSON files

### Modifying Existing Validation

1. **Locate validator** in `validation/validators/` packages
2. **Modify validation logic** in `validate()` method
3. **Update or add tests** to cover new scenarios
4. **Add test JSON** if needed for integration tests
5. **Verify all tests pass** with `mvn verify`

## Important Notes for AI Assistants

### Code Quality Considerations

1. **The author's notes in Trade.java (lines 81-98)** discuss a key architectural decision about validation approach - read this for context on validation philosophy

2. **This is a time-constrained coding challenge** - some shortcuts were taken intentionally (mentioned in readme.md line 3)

3. **Strong typing is preferred** - Don't convert everything to Strings; use Java's type system

4. **External service resilience** - The holiday service can fail; consider caching strategies

### When Making Changes

1. **Maintain the Chain of Responsibility pattern** - Don't break the validator discovery mechanism
2. **Keep validators focused** - Each validator should have a single responsibility
3. **Return all errors** - Don't fail fast; collect all validation errors
4. **Test with real HTTP calls** - Integration tests are critical
5. **Update Swagger annotations** - Keep API documentation current

### Testing Requirements

- **Always write both unit and integration tests** for new features
- **Integration tests must use test JSON files** from resources
- **Verify external service integration** doesn't break under failure
- **Check all HTTP status codes** (OK, BAD_REQUEST, UNPROCESSABLE_ENTITY)

### Maven Plugin Notes

- **spring-boot-maven-plugin**: Provides `spring-boot:run` and executable JAR packaging
- **gmavenplus-plugin**: Compiles Groovy/Spock tests
- **maven-failsafe-plugin**: Runs integration tests (*IT.java) separately from unit tests

## Scalability & High Availability Notes

From readme.md, the service is designed to be:
- **Stateless** - No database, easy to scale horizontally
- **Cloud-ready** - Can be deployed in AWS with auto-scaling groups
- **Multi-AZ capable** - For high availability
- **Cacheable** - External holiday API calls can be cached with `@Cacheable`

## Troubleshooting

### Common Issues

1. **Holiday API limit exceeded**: Update `holidays.key` in `application.properties`
2. **Tests failing on dates**: Some validators check real holidays; dates matter
3. **Integration tests port conflicts**: Uses random port by default
4. **Groovy tests not running**: Ensure gmavenplus-plugin is configured in pom.xml

### Debugging

- Set `logging.level.priv.rdo = TRACE` for detailed logs
- XLogger provides method entry/exit tracing
- Swagger UI at `/documentation` for manual API testing
- Metrics available at `/metrics/priv.rdo.trade.endpoint.TradeValidationEndpoint.trades.*`

## Git Workflow

This repository uses feature branches:
- **Main branch**: Not specified in git status
- **Current branch**: `claude/claude-md-mhy1ttvvieg715nj-01QLVzpwKdtwMS78rP5ZFmgX`
- **Recent commits** focus on test fixes and API key updates
- Use `mvn verify` before committing to ensure tests pass

## Additional Resources

- **Swagger UI**: http://localhost:8080/documentation
- **Metrics**: http://localhost:8080/metrics
- **Holiday API Docs**: See notes.txt for API references
- **FX Trading References**: See readme.md lines 43-47 for Investopedia links used for business rules

---

**Last Updated**: 2025-11-13
**Project Version**: 1.0-SNAPSHOT
