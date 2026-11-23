package kr.co.private_box;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

//@SpringBootApplication(exclude = {
//		DataSourceAutoConfiguration.class
//})
@SpringBootApplication
public class PrivateBoxApplication {
	public static void main(String[] args) {
		SpringApplication.run(PrivateBoxApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(DataSource dataSource){
		return args -> {
			Connection connection = dataSource.getConnection();
		};
	}
}
