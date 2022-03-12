package dk.fantastiskefroe.spir.ingest;

import dk.fantastiskefroe.spir.ingest.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class SpirIngestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpirIngestApplication.class, args);
	}

}
