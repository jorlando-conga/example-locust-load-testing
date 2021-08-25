package example.todolist.config;

import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.logging.SessionLog;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EclipseLinkConfig extends JpaBaseConfiguration {

    protected EclipseLinkConfig(HikariDataSource dataSource, JpaProperties properties,
                                ObjectProvider<JtaTransactionManager> transactionManager) {
        super(dataSource, properties, transactionManager);
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(PersistenceUnitProperties.WEAVING, "static");
        properties.put(PersistenceUnitProperties.WEAVING_LAZY, Boolean.TRUE.toString());
        properties.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.CREATE_OR_EXTEND);
        properties.put(PersistenceUnitProperties.DDL_GENERATION_MODE, PersistenceUnitProperties.DDL_DATABASE_GENERATION);
        //properties.put(PersistenceUnitProperties.CACHE_SHARED_DEFAULT, Boolean.FALSE.toString());
        properties.put(PersistenceUnitProperties.LOGGING_LEVEL, SessionLog.OFF_LABEL);
        return properties;
    }
}
