package binar.box.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariDataSource;

import binar.box.util.Constants;

@Configuration
public class DatabaseConfiguration {

	@Autowired
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
