package org.example.cayenne.persistent;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class SybContainer extends JdbcDatabaseContainer<SybContainer> {
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("jaschweder/sybase");
    public static final int DEFAULT_SYBASE_PORT = 2638;
    private String databaseName;
    private String username;
    private String password;


    public SybContainer(@lombok.NonNull String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    public SybContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        this.databaseName = "guest";
        this.username = "dba";
        this.password = "sql";
        dockerImageName.assertCompatibleWith(new DockerImageName[]{DEFAULT_IMAGE_NAME});
        this.withPrivilegedMode(true);

        this.waitStrategy = (new LogMessageWaitStrategy()).withRegEx(".*TCPIP link started successfully.*").withStartupTimeout(Duration.of(5L, ChronoUnit.MINUTES));
        this.addExposedPort(DEFAULT_SYBASE_PORT);
    }


    protected void configure() {
        this.addEnv("SYBASE_DB", this.databaseName);
        this.addEnv("SYBASE_USER", this.username);
        this.addEnv("SYBASE_PASSWORD", this.password);
    }

    public String getDriverClassName() {
        return "net.sourceforge.jtds.jdbc.Driver";
    }

    public String getJdbcUrl() {
        String additionalUrlParams = this.constructUrlParameters(":", ";", ";");
        return "jdbc:jtds:sybase://" + this.getHost() + ":" + this.getMappedPort(2638) + additionalUrlParams;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public SybContainer withUsername(String username) {
        this.username = username;
        return this;
    }

    public SybContainer withPassword(String password) {
        this.password = password;
        return this;
    }

    public SybContainer withDatabaseName(String dbName) {
        this.databaseName = dbName;
        return this;
    }

    protected void waitUntilContainerStarted() {
        this.getWaitStrategy().waitUntilReady(this);
    }

    protected String getTestQueryString() {
        return "SELECT 1 FROM SYSOBJECTS";
    }
}
