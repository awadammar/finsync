package com.project.finsync.service;

import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SMSProcessorService {
    private static final String CURRENCIES = Currency.getAvailableCurrencies().stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.joining("|"));

    public void convertSMSToTransaction(String sms) {
        Double amount = getTransactionAmount(sms);
        String accountNo = getTransactionAccountNo(sms);
    }

    private Double getTransactionAmount(String sms) {
        String regex = "[" + CURRENCIES + "]\\.?\\s([,\\d]+\\.?\\d{0,2})|[" + CURRENCIES + "]]\\.?\\s*([,\\d]+\\.?\\d{0,2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sms);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return 0.0;
    }

    private String getTransactionAccountNo(String sms) {
        String regex = "\\d*[Xx*]*\\d*[Xx*]+[\\s|-]?\\d{3,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sms);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

}
