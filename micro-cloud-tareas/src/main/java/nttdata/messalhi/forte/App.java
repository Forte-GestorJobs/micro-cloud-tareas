package nttdata.messalhi.forte;
import nttdata.messalhi.forte.dao.TaskInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.*;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
@ServletComponentScan
public class App extends SpringBootServletInitializer {

    @Autowired
    private static TaskInfoDAO taskInfoDAO;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(App.class);
    }
}