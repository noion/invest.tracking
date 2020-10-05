package ru.noion.invest.tracking.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.models.operations.OperationsList;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AllTradedFigisGet {

    private static final OffsetDateTime START_DATE = OffsetDateTime.of(2018, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

    private final OpenApi openApi;

    public List<String> activate(String contractId) {
        OffsetDateTime now = OffsetDateTime.now();
        OperationsList operationsList = openApi.getOperationsContext().getOperations(START_DATE, now, null, contractId).join();
        return operationsList.operations.stream()
                .map(operation -> operation.figi)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
