package org.xmltv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    /**
     * 启动
     * mvn clean package
     * java -jar target/${appName}.jar or nohup java -jar target/${appName}.jar &
     *
     * 停止
     * curl -X POST http://localhost:8081/actuator/shutdown or kill -9 [PID]
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
