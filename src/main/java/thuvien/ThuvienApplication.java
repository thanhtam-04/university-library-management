package thuvien; // Phải khai báo package này để làm "gốc"

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ThuvienApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThuvienApplication.class, args);
    }
}