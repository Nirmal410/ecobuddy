package net.codejava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

@SpringBootApplication
public class CodeJavaAppApplication {
    
    public static void main(String[] args) {
        loadDotEnv();
        SpringApplication.run(CodeJavaAppApplication.class, args);
    }

    private static void loadDotEnv() {
        try {
            File envFile = new File(".env");
            if (envFile.exists()) {
                List<String> lines = Files.readAllLines(envFile.toPath());
                for (String line : lines) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#") && line.contains("=")) {
                        int idx = line.indexOf("=");
                        String key = line.substring(0, idx).trim();
                        String value = line.substring(idx + 1).trim();
                        if (System.getProperty(key) == null && System.getenv(key) == null) {
                            System.setProperty(key, value);
                        }
                    }
                }
                System.out.println("🌱 Loaded environment variables from .env file");
            }
        } catch (Exception e) {
            System.err.println("Could not load .env file: " + e.getMessage());
        }
    }
}
