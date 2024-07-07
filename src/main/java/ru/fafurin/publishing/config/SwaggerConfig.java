package ru.fafurin.publishing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port}")
    private String port;

    @Bean
    public OpenAPI api() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:" + port + "/");
        devServer.setDescription("Локальный сервер для разработки приложения");

        Contact contact = new Contact();
        contact.setEmail("ffd58@mail.ru");
        contact.setName("Федор Фафурин");
        contact.setUrl("https://github.com/FFD58");

        return new OpenAPI()
                .servers(List.of(devServer))
                .info(new Info()
                        .title("REST API Издательства Пензенского государственного университета")
                        .version("0.1")
                        .contact(contact)
                        .description("Приложение предназначено для управления задачами сотрудников редакторского отдела " +
                                "и отдела оперативной полиграфии. В дальнейшем возможно внедрение приложения " +
                                "в работу других отделов издательства.")
                );
    }
}
