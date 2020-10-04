package ru.noion.invest.tracking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noion.invest.tracking.logic.GetContract;
import ru.noion.invest.tracking.v1.api.InfoApiDelegate;
import ru.noion.invest.tracking.v1.model.AccountList;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.models.operations.OperationsList;
import ru.tinkoff.invest.openapi.models.user.AccountsList;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InfoController implements InfoApiDelegate {

    private final OpenApi api;
    private final GetContract getContract;

    @Override
    public ResponseEntity<AccountList> getContracts() {
        try {
            log.debug("in getContracts");
            AccountList accountList = getContract.activate();
            return ResponseEntity.ok(accountList);
        } finally {
            log.debug("out getContracts");
        }
    }

    @GetMapping("/test")
    public List<OperationsList> someResponse() {
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
