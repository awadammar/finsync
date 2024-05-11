package com.project.finsync.service;

import com.project.finsync.model.Account;
import com.project.finsync.model.Sms;
import com.project.finsync.model.Transaction;
import com.project.finsync.model.User;
import com.project.finsync.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsProcessorServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private SmsProcessorService smsProcessorService;

    @Test
    void testProcessSmsMessages() {
        User user = TestUtils.createSimpleUser();
        Account account = new Account();
        account.setId(1L);
        List<Sms> smsList = new ArrayList<>();

        smsList.add(createSms("Transaction of EGP 4779.76 from AMZN Mktp DE using HSBC card ending with *** 2029 was declined. Please call HSBC Contact Center for more details."));
        smsList.add(createSms("Your Credit Card ending with *** 1234 has been used for EGP 4568.89 on 06/03/2024 at AMZN Mktp DE. Your available limit is EGP 135453.26"));
        smsList.add(createSms("Transaction EGP 2600.00 on Card ending *** 1234 was declined due to invalid PIN. Enter the correct PIN or call the number at the back of your Card to reset it."));
        smsList.add(createSms("06MAR24 Transfer from 011-123***-024 EGP 13,965.46- to your Credit Card ending with 1234 as per your instruction. Your available balance is EGP 2,121.38"));
        smsList.add(createSms("Your HSBC Account ********2024 was debited with IPN outward transfer for EGP 6,000.00 on 08-03-2024 17:06 to OMAR IBRAHIM MOHAMED SANAD with reference 915d4583. For further details, please contact HSBC call centre"));
        smsList.add(createSms("Your HSBC Account ********2024 was credited with IPN inward transfer for EGP 5,500.00 on 08-03-2024 17:02 from abc@instapay with reference b1946649. For further details, please contact HSBC call centre"));

        when(accountService.findAccountsByUser(user.getId())).thenReturn(new ArrayList<>()); // Mock empty account list
        when(accountService.createAccount(eq(user.getId()), any(Account.class))).thenReturn(Optional.of(account)); // Mock account creation

        smsProcessorService.convertSmsListForUser(smsList, user);

        verify(transactionService, times(4)).createTransaction(anyLong(), any(Transaction.class)); // Verify transaction creation
    }

    private Sms createSms(String body) {
        return new Sms(body, LocalDate.now());
    }
}

