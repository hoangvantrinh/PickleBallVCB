package com.vcb.master;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.FileOutputStream;

@EnableEncryptableProperties
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        try{
            FileCopyUtils.copy(new ClassPathResource("database.db").getInputStream(),
                    new FileOutputStream("/tmp/database.db"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApplicationContext context = SpringApplication.run(MainApplication.class, args);
    }

}
