package pan.artem.tinkoff;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class H2Container extends GenericContainer<H2Container> {
    private static final String IMAGE_VERSION = "oscarfonts/h2";
    private static H2Container container;

    private H2Container() {
        super(IMAGE_VERSION);
    }

    public static H2Container getInstance() {
        if (container == null) {
            container = new H2Container()
                    .withExposedPorts(81, 1521)
                    .withEnv("H2_OPTIONS", "-ifNotExists")
//                    .waitingFor(new WaitStrategy() {
//                        @Override
//                        public void waitUntilReady(WaitStrategyTarget waitStrategyTarget) {
//                        }
//
//                        @Override
//                        public WaitStrategy withStartupTimeout(Duration startupTimeout) {
//                            return this;
//                        }
//                    });   // a workaround in case defaultWaitStrategy now works for you
                    .waitingFor(Wait.defaultWaitStrategy());
        }
        return container;
    }

    @Override
    public void stop() {
    }

    public void registerH2Properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> "jdbc:h2:tcp://localhost:" + getMappedPort(1521) + "/test");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
    }
}