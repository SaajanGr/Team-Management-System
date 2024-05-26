package rei.java.springboot.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractContainerBaseTest {

    @Container
    static final MySQLContainer MY_SQL_CONTAINER;

    // Initialize the MySQLContainer with the latest MySQL image
    static {
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest")
                .withUsername("username")
                .withPassword("password")
                .withDatabaseName("tms");
        
        MY_SQL_CONTAINER.start();
    }

    // Set the dynamic properties for the MySQLContainer
    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }
}
