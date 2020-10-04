package ru.noion.invest.tracking.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApiFactory;

import java.util.logging.Logger;

@Slf4j
@Configuration
public class InvestApiConfiguration {

    @Bean
    public OkHttpOpenApiFactory okHttpOpenApiFactory(@Value("${invest.token}") String token) {
        return new OkHttpOpenApiFactory(token, Logger.getLogger("apiLog"));
    }
}
