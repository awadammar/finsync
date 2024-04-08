package com.project.finsync.service;

import com.project.finsync.enums.TransactionType;
import com.project.finsync.model.Account;
import com.project.finsync.model.Sms;
import com.project.finsync.model.Transaction;
import com.project.finsync.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SmsProcessorService {
    private static final String CURRENCIES = Currency.getAvailableCurrencies().stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.joining("|"));
    private static final Pattern amountPattern = Pattern.compile("[" + CURRENCIES + "]\\.?\\s([,\\d]+\\.?\\d{0,2})");
    private static final Pattern accountNopattern = Pattern.compile("\\d*[Xx*]*\\d*[Xx*]+[\\s|-]?\\d{3,}");
    private static final Pattern creditPattern = Pattern.compile("(?i)(credited|deposited)");
    private static final Pattern debitPattern = Pattern.compile("(?i)(debited|deducted)");
    private static final Pattern transferPattern = Pattern.compile("(?i)(transfer|transfered)");
    private static final Pattern declinedPattern = Pattern.compile("(?i)(declined)");

    public void convertSmsForUser(List<Sms> smsList, User user) {
        List<Transaction> transactions = filterDeclined(smsList).stream()
                .map(sms -> convertSmsToTransaction(sms, user))
                .toList();
    }

    private List<Sms> filterDeclined(List<Sms> smsList) {
        return smsList.stream()
                .filter(sms -> !declinedPattern.matcher(sms.getBody()).find())
                .toList();
    }

    public Transaction convertSmsToTransaction(Sms sms, User user) {
        Double amount = getTransactionAmount(sms.getBody());
        String accountNo = getTransactionAccountNo(sms.getBody());
        TransactionType transactionType = getTransactionType(sms.getBody());
        LocalDate date = sms.getDate();
        return new Transaction(new Account(user, accountNo), amount, date, transactionType);
    }

    private Double getTransactionAmount(String smsBody) {
        Matcher matcher = amountPattern.matcher(smsBody);
        if (matcher.find()) {
            //replace all commas if present with no comma
            String trimmedAmount = matcher.group(1).replace(",", "").trim();
            return Double.parseDouble(trimmedAmount);
        }
        return 0.0;
    }

    private String getTransactionAccountNo(String smsBody) {
        Matcher matcher = accountNopattern.matcher(smsBody);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private TransactionType getTransactionType(String smsBody) {
        Matcher creditMatcher = creditPattern.matcher(smsBody);
        if (creditMatcher.find()) {
            return TransactionType.CREDITED;
        }

        Matcher debitMatcher = debitPattern.matcher(smsBody);
        if (debitMatcher.find()) {
            return TransactionType.DEBITED;
        }

        Matcher transferMatcher = transferPattern.matcher(smsBody);
        if (transferMatcher.find()) {
            return TransactionType.TRANSFER;
        }

        return TransactionType.OTHER;
    }

}
