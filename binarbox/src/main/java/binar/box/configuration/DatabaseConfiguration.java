package binar.box.configuration;

import binar.box.util.Constants;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
@Configuration
public class DatabaseConfiguration {

    @Resource
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        final String driverClassName = environment.getProperty("database.driverClassName");
        final String jdbcUrl = environment.getProperty("database.jdbcUrl");
        final String username = environment.getProperty("database.username");
        final String password = environment.getProperty("database.password");
        int maxPoolSize = Integer.valueOf(environment.getProperty("database.poolSize"));
        boolean isAutoCommit = Boolean.valueOf(environment.getProperty("database.autoCommit"));

        final HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setMaximumPoolSize(maxPoolSize);
        hikariDataSource.setDriverClassName(driverClassName);
        hikariDataSource.setJdbcUrl(jdbcUrl);
        hikariDataSource.addDataSourceProperty(Constants.DATABASE_USERNAME_PROPERTY, username);
        hikariDataSource.addDataSourceProperty(Constants.DATABASE_PASSWORD_PROPERTY, password);
        hikariDataSource.setAutoCommit(isAutoCommit);
        return hikariDataSource;
    }
}
