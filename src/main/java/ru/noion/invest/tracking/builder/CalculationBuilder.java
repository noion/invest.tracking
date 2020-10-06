package ru.noion.invest.tracking.builder;

import org.springframework.stereotype.Component;
import ru.noion.invest.tracking.v1.model.Calculation;
import ru.tinkoff.invest.openapi.models.operations.OperationStatus;
import ru.tinkoff.invest.openapi.models.operations.OperationType;
import ru.tinkoff.invest.openapi.models.operations.OperationsList;

import java.math.BigDecimal;

@Component
public class CalculationBuilder {

    public Calculation build(OperationsList operationsList, String figi) {
        Calculation calculation = new Calculation();
        String currency = operationsList.operations.isEmpty() ? null : operationsList.operations.get(0).currency.name();
        calculation.setCurrency(currency);
        calculation.setFigi(figi);
        BigDecimal payment = operationsList.operations.stream()
                //TODO add Progress?
                .filter(operation -> operation.status == OperationStatus.Done)
                .filter(operation -> operation.operationType == OperationType.Buy ||
                        operation.operationType == OperationType.BuyCard ||
                        operation.operationType == OperationType.Sell)
                .reduce(BigDecimal.ZERO, (partialResult, operation) -> partialResult.add(operation.payment), BigDecimal::add);
        BigDecimal brockerFee = operationsList.operations.stream()
                //TODO add Progress?
                .filter(operation -> operation.status == OperationStatus.Done)
                .filter(operation -> operation.operationType == OperationType.BrokerCommission)
                .reduce(BigDecimal.ZERO, (partialResult, operation) -> partialResult.add(operation.payment), BigDecimal::add);
        calculation.setBrockerFee(brockerFee);
        calculation.setFinanshialResult(payment.add(brockerFee));
        return calculation;
    }
}