package ru.noion.invest.tracking.logic;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.noion.invest.tracking.v1.model.FinRes;
import ru.noion.invest.tracking.v1.model.FinResFinResByCurrency;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.models.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FinResGet {

    private final OpenApi openApi;

//   fix calculation for opened margin position
    public FinRes activate(String contractId,
                           LocalDate startDate,
                           LocalDate endDate,
                           Boolean includeCommission) {
        var start = OffsetDateTime.of(startDate.atStartOfDay(), ZoneOffset.UTC);
        var end = OffsetDateTime.of(dateTimeAtEndOfDay(endDate), ZoneOffset.UTC);
        var operationsList = openApi.getOperationsContext().getOperations(start, end, null, contractId).join();
        var currencyOperationMap = operationsList.operations.stream()
                .collect(Collectors.groupingBy(operation -> operation.currency));
        var finResByCurrencyList = currencyOperationMap.entrySet().stream()
                .reduce(new HashMap<Currency, BigDecimal>(), (currencyPaymentMap, currencyListEntry) -> {
                    var currency = currencyListEntry.getKey();
                    var payment = currencyPaymentMap.get(currency);
                    var paymentOperationSum = currencyListEntry.getValue().stream()
                            .map(operation -> operation.payment)
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO);
                    var paymentSum = payment == null ? paymentOperationSum : payment.add(paymentOperationSum);
                    currencyPaymentMap.put(currency, paymentSum);
                    return currencyPaymentMap;
                }, (currencyBigDecimalHashMap, currencyBigDecimalHashMap2) -> {
                    currencyBigDecimalHashMap.putAll(currencyBigDecimalHashMap2);
                    return currencyBigDecimalHashMap;
                }).entrySet().stream()
                .map(currencyPaymentEntry -> {
                    var currency = currencyPaymentEntry.getKey();
                    var sum = currencyPaymentEntry.getValue();
                    return new FinResFinResByCurrency()
                            .currency(currency.name())
                            .sum(sum);
                })
                .collect(Collectors.toList());
        return new FinRes()
                .finResByCurrency(finResByCurrencyList);
    }

    @NotNull
    private LocalDateTime dateTimeAtEndOfDay(LocalDate endDate) {
        return endDate.plusDays(1).atStartOfDay().minusNanos(1);
    }
}
