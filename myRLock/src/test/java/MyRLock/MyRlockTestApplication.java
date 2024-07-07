package MyRLock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy
public class MyRlockTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyRlockTestApplication.class, args);
    }

}