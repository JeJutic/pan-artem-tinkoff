package pan.artem.tinkoff;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
public class TestContainerH2Test {

    @Container
    static private H2Container h2 = H2Container.getInstance();
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void registerH2Properties(DynamicPropertyRegistry registry) {
        h2.registerH2Properties(registry);
    }

    @Test
    void databaseWorks() {
        jdbcTemplate.execute(
                "CREATE TABLE tinkoff (" +
                        "    ID INT PRIMARY KEY," +
                        "    VAL VARCHAR(255)" +
                        ")");
        jdbcTemplate.update(
                "INSERT INTO tinkoff (ID, VAL) VALUES (?, ?)",
                1, "Test Val");
        String testName = jdbcTemplate.queryForObject("SELECT VAL FROM tinkoff WHERE id = 1", String.class);

        assertThat(testName).isEqualTo("Test Val");
    }
}