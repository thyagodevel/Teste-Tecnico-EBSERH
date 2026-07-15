package br.com.teste.ebserh.paciente_api_ebserh.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pacienteApiOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API de Pacientes")
                        .description("API CRUD para gerenciamento de pacientes hospitalares.")
                        .version("1.0.0")
                );
    }
}