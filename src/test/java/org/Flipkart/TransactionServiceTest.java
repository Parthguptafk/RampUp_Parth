package org.Flipkart;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.Flipkart.core.Transaction;
import org.Flipkart.core.Wallet;
import org.Flipkart.dao.TransactionDao;
import org.Flipkart.dao.WalletDao;
import org.Flipkart.exception.*;
import org.Flipkart.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TransactionServiceTest {

    @Mock
    private TransactionDao transactionDao;

    @Mock
    private WalletDao walletDao;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTransactionSuccess() throws InvalidRequestException {
        Transaction transaction = new Transaction();
        when(transactionDao.addTransaction(transaction)).thenReturn(transaction);

        Transaction result = transactionService.addTransaction(transaction);
        assertNotNull(result);
        assertEquals(transaction, result);
    }

    @Test
    void testAddTransactionFailure() {
        Transaction transaction = new Transaction();
        when(transactionDao.addTransaction(transaction)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            transactionService.addTransaction(transaction);
        });
        assertTrue(exception.getMessage().contains("Failed to add transaction"));
    }

    @Test
    void testCreditSuccess() throws InvalidRequestException, MissingDataException {
        Long userId = 1L;
        Long amount = 100L;
        Wallet wallet = new Wallet();
        wallet.setBalance(200L);

        when(walletDao.getWalletByUserId(userId)).thenReturn(Optional.of(wallet));
        when(walletDao.updateWallet(any(Wallet.class))).thenReturn(wallet);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setType("CREDIT");
        transaction.setTimestamp(new Date());

        when(transactionDao.addTransaction(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.credit(userId, amount);
        assertNotNull(result);
        assertEquals("CREDIT", result.getType());
        assertEquals(amount, result.getAmount());
    }

    @Test
    void testCreditFailureWalletNotFound() {
        Long userId = 1L;
        Long amount = 100L;
        when(walletDao.getWalletByUserId(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(MissingDataException.class, () -> {
            transactionService.credit(userId, amount);
        });
        assertTrue(exception.getMessage().contains("Wallet does not exist for user ID: " + userId));
    }

    @Test
    void testCreditInvalidAmount() {
        Long userId = 1L;
        Long amount = -100L;

        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            transactionService.credit(userId, amount);
        });
        assertTrue(exception.getMessage().contains("Credit amount must be greater than zero."));
    }

    @Test
    void testDebitSuccess() throws InvalidRequestException {
        Long userId = 1L;
        Long amount = 100L;
        Wallet wallet = new Wallet();
        wallet.setBalance(200L);

        when(walletDao.getWalletByUserId(userId)).thenReturn(Optional.of(wallet));
        when(walletDao.updateWallet(any(Wallet.class))).thenReturn(wallet);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setType("DEBIT");
        transaction.setTimestamp(new Date());

        when(transactionDao.addTransaction(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.debit(userId, amount);
        assertNotNull(result);
        assertEquals("DEBIT", result.getType());
        assertEquals(amount, result.getAmount());
    }

    @Test
    void testDebitFailureWalletNotFound() {
        Long userId = 1L;
        Long amount = 100L;
        when(walletDao.getWalletByUserId(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            transactionService.debit(userId, amount);
        });
        assertTrue(exception.getMessage().contains("Wallet does not exist for user ID: " + userId));
    }

    @Test
    void testDebitFailureInsufficientBalance() {
        Long userId = 1L;
        Long amount = 100L;
        Wallet wallet = new Wallet();
        wallet.setBalance(50L);

        when(walletDao.getWalletByUserId(userId)).thenReturn(Optional.of(wallet));

        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            transactionService.debit(userId, amount);
        });
        assertTrue(exception.getMessage().contains("Insufficient balance for user ID: " + userId));
    }

    @Test
    void testDebitInvalidAmount() {
        Long userId = 1L;
        Long amount = -100L;

        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            transactionService.debit(userId, amount);
        });
        assertTrue(exception.getMessage().contains("Debit amount must be greater than zero."));
    }

    @Test
    void testGetTransactionByUser() throws InvalidRequestException {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction());
        when(transactionDao.getTransactionByUser(1L)).thenReturn(mockTransactions);

        List<Transaction> transactions = transactionService.getTransactionByUser(1L);
        assertEquals(1, transactions.size());
    }

    @Test
    void testGetTransactionByUserFailure() {
        when(transactionDao.getTransactionByUser(1L)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            transactionService.getTransactionByUser(1L);
        });
        assertTrue(exception.getMessage().contains("Failed to retrieve transactions for user ID: 1"));
    }

    @Test
    void testGetTransactionsByDate() throws MissingDataException {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction());
        Date startDate = new Date();
        Date endDate = new Date();
        when(transactionDao.getTransactionsByDate(startDate, endDate, 1L)).thenReturn(mockTransactions);

        List<Transaction> transactions = transactionService.getTransactionsByDate(startDate, endDate, 1L);
        assertEquals(1, transactions.size());
    }

    @Test
    void testGetTransactionsByDateFailure() {
        when(transactionDao.getTransactionsByDate(any(Date.class), any(Date.class), any(Long.class)))
                .thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(MissingDataException.class, () -> {
            transactionService.getTransactionsByDate(new Date(), new Date(), 1L);
        });
        assertTrue(exception.getMessage().contains("Failed to retrieve transactions by date for user ID: 1"));
    }

    @Test
    void testGetTransactionsByType() throws MissingDataException {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction());
        when(transactionDao.getTransactionsByType(1L, "CREDIT")).thenReturn(mockTransactions);

        List<Transaction> transactions = transactionService.getTransactionsByType(1L, "CREDIT");
        assertEquals(1, transactions.size());
    }

    @Test
    void testGetTransactionsByTypeFailure() {
        when(transactionDao.getTransactionsByType(1L, "CREDIT")).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(MissingDataException.class, () -> {
            transactionService.getTransactionsByType(1L, "CREDIT");
        });
        assertTrue(exception.getMessage().contains("Failed to retrieve transactions for user ID: 1 with type: CREDIT"));
    }
}
