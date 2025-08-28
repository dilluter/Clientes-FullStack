package io.github.dilluter;

import io.github.dilluter.model.entity.Cliente;
import io.github.dilluter.model.repository.ClienteRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class ClientesApplication {


    public static void main(String[] args) {
        SpringApplication.run(ClientesApplication.class, args);
    }
}

