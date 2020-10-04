package ru.noion.invest.tracking.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.noion.invest.tracking.converter.AccountListConverter;
import ru.noion.invest.tracking.v1.model.AccountList;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.models.user.AccountsList;

@Component
@RequiredArgsConstructor
public class GetContract {

    private final OpenApi openApi;
    private final AccountListConverter accountListConverter;

    public AccountList activate() {
        AccountsList accountsList = openApi.getUserContext().getAccounts().join();
        return accountListConverter.convert(accountsList);
    }
}