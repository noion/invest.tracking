package ru.noion.invest.tracking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.models.operations.OperationsList;
import ru.tinkoff.invest.openapi.models.user.AccountsList;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApiFactory;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
public class RestController {
    private final OkHttpOpenApiFactory okHttpOpenApiFactory;

    @GetMapping("/test")
    public List<OperationsList> someResponse() {
        OpenApi api = okHttpOpenApiFactory.createOpenApiClient(Executors.newFixedThreadPool(100));
        AccountsList accountsList = api.getUserContext().getAccounts().join();
        return accountsList.accounts.stream()
                .map(brokerAccount -> {
                    OffsetDateTime from = OffsetDateTime.of(2018, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
                    OffsetDateTime now = OffsetDateTime.now();
                    return api.getOperationsContext().getOperations(from, now, null, brokerAccount.brokerAccountId);
                })
                .map(CompletableFuture::join)
                .collect(Collectors.toUnmodifiableList());
    }
}
