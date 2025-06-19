package xyz.sonyp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("xyz.sonyp.mapper")
@SpringBootApplication
public class BlackHorseShoppingMallApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlackHorseShoppingMallApplication.class, args);
	}

}
