package com.fileManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

@EnableAspectJAutoProxy
@EnableCaching // 开启缓存
@SpringBootApplication(scanBasePackages = "com.fileManager")
public class SpringbootMybatisplusApplication implements CommandLineRunner {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybatisplusApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Database URL: " + dbUrl);
		System.out.println("Database Username: " + dbUsername);
		// 出于安全考虑，避免输出密码，或者以其他方式处理
		System.out.println("Database Password: [HIDDEN]");

		checkDatabaseConnection();
	}

	private void checkDatabaseConnection() {
		try (Connection connection = new DriverManagerDataSource(dbUrl, dbUsername, dbPassword).getConnection()) {
			System.out.println("Database connection is successful.");
		} catch (SQLException e) {
			System.err.println("Failed to connect to the database: " + e.getMessage());
		}
	}
}
