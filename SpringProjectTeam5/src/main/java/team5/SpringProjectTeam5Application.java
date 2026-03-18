package team5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement  // 啟用 Spring 事務管理 //0204加
public class SpringProjectTeam5Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectTeam5Application.class, args);
	}

}
