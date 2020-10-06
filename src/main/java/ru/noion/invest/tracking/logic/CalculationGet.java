package ru.noion.invest.tracking.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.noion.invest.tracking.builder.CalculationBuilder;
import ru.noion.invest.tracking.v1.model.Calculation;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.models.operations.OperationsList;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class CalculationGet {
    private static final OffsetDateTime START_DATE = OffsetDateTime.of(2018, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

    private final OpenApi openApi;
    private final CalculationBuilder calculationBuilder;

    public Calculation activate(String contractId, String figi) {
        OffsetDateTime now = OffsetDateTime.now();
        OperationsList operationsList = openApi.getOperationsContext().getOperations(START_DATE, now, figi, contractId).join();
        return calculationBuilder.build(operationsList, figi);
    }
}