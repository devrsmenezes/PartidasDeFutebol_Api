package com.expoo.partidasdefutebol_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; 

@SpringBootApplication
@EnableScheduling 
@OpenAPIDefinition(
    info = @Info(
        title = "API de Partidas de Futebol",
        version = "1.0",
        description = "API para gerenciamento de partidas, clubes e estádios de futebol"
    )
)
public class PartidasDeFutebolApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartidasDeFutebolApiApplication.class, args);
    }
}