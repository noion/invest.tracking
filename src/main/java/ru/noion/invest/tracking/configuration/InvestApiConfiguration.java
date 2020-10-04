package ru.noion.invest.tracking.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApiFactory;

import java.util.logging.Logger;

@Configuration
public class InvestApiConfiguration {

    @Bean
    public OkHttpOpenApiFactory okHttpOpenApiFactory(@Value("${invest.token}") String token) {
        return new OkHttpOpenApiFactory(token, Logger.getLogger("apiLog"));
    }

    @Bean
    public OpenApi openApi(OkHttpOpenApiFactory okHttpOpenApiFactory,
                           ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return okHttpOpenApiFactory.createOpenApiClient(threadPoolTaskExecutor);
    }
}
