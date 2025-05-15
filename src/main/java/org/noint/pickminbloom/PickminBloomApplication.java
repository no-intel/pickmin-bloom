package org.noint.pickminbloom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class PickminBloomApplication {

    public static void main(String[] args) {
        SpringApplication.run(PickminBloomApplication.class, args);
    }

}
