package example.todolist.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Properties;

@Configuration
@EntityScan(basePackages = {"example.todolist.data.persistence.model", "example.todolist.data.persistence.converter"})
@EnableJpaRepositories("example.todolist.data.persistence.repository")
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String postgresURL;

    @Value("${spring.datasource.username}")
    private String postgresUserName;

    @Value("${spring.datasource.password}")
    private String postgresPassword;

    @Bean
    public HikariConfig hikariConfig() {
        Properties hikariProperties = new Properties();
        hikariProperties.setProperty("url", postgresURL);
        hikariProperties.setProperty("user", postgresUserName);
        hikariProperties.setProperty("password", postgresPassword);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceProperties(hikariProperties);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        hikariConfig.setConnectionTimeout(30_000L);
        hikariConfig.setInitializationFailTimeout(15_000L);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(5);
        return hikariConfig;
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }
}
