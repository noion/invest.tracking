package ru.noion.invest.tracking.converter;

import org.springframework.stereotype.Component;
import ru.noion.invest.tracking.v1.model.AccountList;
import ru.noion.invest.tracking.v1.model.AccountListBrokerAccount;
import ru.tinkoff.invest.openapi.models.user.AccountsList;
import ru.tinkoff.invest.openapi.models.user.BrokerAccountType;

import java.util.stream.Collectors;

@Component
public class AccountListConverter {
    public AccountList convert(AccountsList accountsList) {
        AccountList accountList = new AccountList();
        accountList.brokerAccount(accountsList.accounts.stream()
                .map(brokerAccount -> new AccountListBrokerAccount()
                        .brokerAccountId(brokerAccount.brokerAccountId)
                        .brokerAccountType(convertType(brokerAccount.brokerAccountType)))
                .collect(Collectors.toUnmodifiableList()));
        return accountList;
    }

    private AccountListBrokerAccount.BrokerAccountTypeEnum convertType(BrokerAccountType brokerAccountType) {
        switch (brokerAccountType) {
            case Tinkoff:
                return AccountListBrokerAccount.BrokerAccountTypeEnum.BROCKER;
            case TinkoffIis:
                return AccountListBrokerAccount.BrokerAccountTypeEnum.IIS;
            default:
                return null;
        }
    }
}