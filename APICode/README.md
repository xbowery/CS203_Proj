# APICode

## Project Requirements

- Java Development Kit (version 11.0 and above)
- Maven (version 3.3 and above)

### Project Setup

To run the backend application on your machine locally, run the following command:

```bash
mvn spring-boot:run
```

### Testing Results

To see the code coverage results from the JaCoCo plugin in the application, run the following command:

```
mvn test
```

The generated JaCoCo test results should be accessible at the `target\site\jacoco` directory

### Documentation

The application also has a plugin to automatically generate the Javadoc for all source files. Run the following command:

```
mvn javadoc:javadoc
```

The generated Javadoc should be accessible at the `target\site\apidocs` directory
