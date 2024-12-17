package com.fileManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@EnableCaching //开启缓存
@SpringBootApplication(scanBasePackages = "com.fileManager")
public class SpringbootMybatisplusApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybatisplusApplication.class, args);
	}
}
