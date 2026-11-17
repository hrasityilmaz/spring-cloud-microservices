package art.timestop.eurekadiscovert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekadiscovertApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekadiscovertApplication.class, args);
	}

}
