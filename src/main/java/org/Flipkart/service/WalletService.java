package org.Flipkart.service;

import org.Flipkart.core.Wallet;
import org.Flipkart.dao.WalletDao;
import org.Flipkart.exception.*;

import java.util.Optional;

public class WalletService {

    private final WalletDao walletDao;

    public WalletService(WalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public Wallet updateWallet(Wallet wallet) throws InvalidRequestException {
        Optional<Wallet> existingWallet = walletDao.getWalletByUserId(wallet.getUserId());
        if (!existingWallet.isPresent()) {
            throw new InvalidRequestException("Wallet not found for user ID " + wallet.getUserId());
        }
        try {
            validateWallet(wallet);
            return walletDao.updateWallet(wallet);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Error updating wallet: " + e.getMessage());
        } catch (Exception e) {
            throw new InvalidRequestException("Unexpected error while updating wallet: " + e.getMessage());
        }
    }

    public Optional<Long> getBalanceById(Long userId) throws InvalidRequestException {
        try {
            validateUserId(userId);
            return walletDao.getBalanceById(userId);
        } catch (Exception e) {
            throw new InvalidRequestException("Unexpected error while retrieving balance: " + e.getMessage());
        }
    }

    public Wallet addWallet(Wallet wallet) throws InvalidRequestException {
        try {
            validateWallet(wallet);
            return walletDao.addWallet(wallet);
        } catch (Exception e) {
            throw new InvalidRequestException("Unexpected error while adding wallet: " + e.getMessage());
        }
    }

    public Optional<Wallet> getWalletByUserId(Long userId) throws InvalidRequestException {
        try {
            validateUserId(userId);
            return walletDao.getWalletByUserId(userId);
        } catch (Exception e) {
            throw new InvalidRequestException("Unexpected error while retrieving wallet: " + e.getMessage());
        }
    }

    private void validateWallet(Wallet wallet) throws MissingDataException {
        if (wallet == null || wallet.getUserId() == null) {
            throw new MissingDataException("Invalid wallet object: Wallet or user ID is null");
        }
    }

    private void validateUserId(Long userId) throws MissingDataException {
        if (userId == null) {
            throw new MissingDataException("Invalid user ID: User ID is null");
        }
    }
}
