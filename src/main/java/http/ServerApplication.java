package http;

/**
 * @author ke.xiong@oracle.com
 * @version 1.0
 * @date 12/2/20 11:08 AM
 */

 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = {"ctl.**"})
@ServletComponentScan(basePackages = {"ctl.**"})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}