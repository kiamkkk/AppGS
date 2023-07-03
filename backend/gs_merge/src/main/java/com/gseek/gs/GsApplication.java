package com.gseek.gs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
@Slf4j
public class GsApplication {

    public static void main(String[] args) throws AWTException {
        ConfigurableApplicationContext context = SpringApplication.run(GsApplication.class, args);

        // 只在调试时使用
        if (log.isDebugEnabled() && System.getProperty("os.name").contains("Windows")) {
            StringBuilder beginPage = new StringBuilder()
                    .append("------------------------------------------------------\n")
                    .append("|                                                    |\n")
                    .append("|                                                    |\n")
                    .append("|                   Gseek Begin                      |\n")
                    .append("|                                                    |\n")
                    .append("|                                                    |\n")
                    .append("------------------------------------------------------\n");
            System.out.println(beginPage);
        }
    }
}
