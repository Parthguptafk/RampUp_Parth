package org.Flipkart;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.Flipkart.core.Wallet;
import org.Flipkart.dao.WalletDao;
import org.Flipkart.exception.*;
import org.Flipkart.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class WalletServiceTest {

    @Mock
    private WalletDao walletDao;

    @InjectMocks
    private WalletService walletService;

    public WalletServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateWalletSuccess() throws InvalidRequestException {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(100L);

        when(walletDao.getWalletByUserId(wallet.getUserId())).thenReturn(Optional.of(wallet));
        when(walletDao.updateWallet(wallet)).thenReturn(wallet);

        Wallet result = walletService.updateWallet(wallet);
        assertNotNull(result);
        assertEquals(wallet, result);
    }

    @Test
    void testUpdateWalletFailure() {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        when(walletDao.getWalletByUserId(wallet.getUserId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            walletService.updateWallet(wallet);
        });
        assertTrue(exception.getMessage().contains("Wallet not found for user ID"));
    }

    @Test
    void testGetBalanceByIdSuccess() throws InvalidRequestException {
        Long userId = 1L;
        Long balance = 100L;

        when(walletDao.getBalanceById(userId)).thenReturn(Optional.of(balance));

        Optional<Long> result = walletService.getBalanceById(userId);
        assertTrue(result.isPresent());
        assertEquals(balance, result.get());
    }

    @Test
    void testGetBalanceByIdFailure() throws InvalidRequestException {
        Long userId = 1L;

        when(walletDao.getBalanceById(userId)).thenReturn(Optional.empty());

        Optional<Long> result = walletService.getBalanceById(userId);
        assertFalse(result.isPresent());
    }

    @Test
    void testAddWalletSuccess() throws InvalidRequestException {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(100L);
        when(walletDao.addWallet(wallet)).thenReturn(wallet);

        Wallet result = walletService.addWallet(wallet);
        assertNotNull(result);
        assertEquals(wallet, result);
    }

    @Test
    void testAddWalletFailure() {
        Wallet wallet = new Wallet();
        when(walletDao.addWallet(wallet)).thenThrow(new RuntimeException("Database error"));

     assertThrows(InvalidRequestException.class, () -> {
            walletService.addWallet(wallet);
        });
    }

    @Test
    void testGetWalletByUserIdSuccess() throws InvalidRequestException {
        Long userId = 1L;
        Wallet wallet = new Wallet();
        when(walletDao.getWalletByUserId(userId)).thenReturn(Optional.of(wallet));

        Optional<Wallet> result = walletService.getWalletByUserId(userId);
        assertTrue(result.isPresent());
        assertEquals(wallet, result.get());
    }

    @Test
    void testGetWalletByUserIdFailure() throws InvalidRequestException {
        Long userId = 1L;
        when(walletDao.getWalletByUserId(userId)).thenReturn(Optional.empty());

        Optional<Wallet> result = walletService.getWalletByUserId(userId);
        assertFalse(result.isPresent());
    }
}
