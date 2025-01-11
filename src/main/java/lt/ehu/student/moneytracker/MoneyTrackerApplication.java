package lt.ehu.student.moneytracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) // Temporarily disable security
public class MoneyTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoneyTrackerApplication.class, args);
    }
}