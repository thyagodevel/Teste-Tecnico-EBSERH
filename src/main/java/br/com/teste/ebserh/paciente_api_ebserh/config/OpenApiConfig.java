package br.com.teste.ebserh.paciente_api_ebserh.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API de Pacientes")
                        .description("API REST para gerenciamento de pacientes hospitalares.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Thyago Cabral")
                                .email("thyagoc.desousa@gmail.com"))
                );
    }

}