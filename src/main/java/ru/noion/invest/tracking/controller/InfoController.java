package ru.noion.invest.tracking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.noion.invest.tracking.logic.AllTradedFigisGet;
import ru.noion.invest.tracking.logic.CalculationGet;
import ru.noion.invest.tracking.logic.GetContract;
import ru.noion.invest.tracking.v1.api.InfoApiDelegate;
import ru.noion.invest.tracking.v1.model.AccountList;
import ru.noion.invest.tracking.v1.model.Calculation;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InfoController implements InfoApiDelegate {

    private final GetContract getContract;
    private final AllTradedFigisGet allTradedFigisGet;
    private final CalculationGet calculateGet;

    @Override
    public ResponseEntity<AccountList> getContracts() {
        return logAndProcessOperation("getContracts", () -> {
            AccountList accountList = getContract.activate();
            return ResponseEntity.ok(accountList);
        });
    }

    @Override
    public ResponseEntity<List<String>> allTradedFigisGet(String contractId) {
        return logAndProcessOperation("allTradedFigisGet", () -> {
            List<String> isins = allTradedFigisGet.activate(contractId);
            return ResponseEntity.ok(isins);
        });
    }

    @Override
    public ResponseEntity<Calculation> calculateGet(String contractId, String figi) {
        return logAndProcessOperation("allTradedFigisGet", () -> {
            Calculation calculation = calculateGet.activate(contractId, figi);
            return ResponseEntity.ok(calculation);
        });
    }

    private <T> T logAndProcessOperation(String operationName, Supplier<T> supplier) {
        try {
            log.debug("in {}", operationName);
            return supplier.get();
        } finally {
            log.debug("out {}}", operationName);
        }
    }
}
