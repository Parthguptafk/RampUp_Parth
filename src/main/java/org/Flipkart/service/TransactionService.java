package org.Flipkart.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.Flipkart.core.Transaction;
import org.Flipkart.core.Wallet;
import org.Flipkart.dao.TransactionDao;
import org.Flipkart.dao.WalletDao;
import org.Flipkart.exception.*;

public class TransactionService {

    private final TransactionDao transactionDao;
    private final WalletDao walletDao;

    public TransactionService(TransactionDao transactionDao, WalletDao walletDao) {
        this.transactionDao = transactionDao;
        this.walletDao = walletDao;
    }

    public List<Transaction> getTransactionByUser(Long userId) throws InvalidRequestException {
        try {
            return transactionDao.getTransactionByUser(userId);
        } catch (Exception e) {
            throw new InvalidRequestException("Failed to retrieve transactions for user ID: " + userId);
        }
    }

    public List<Transaction> getTransactionsByType(Long userId, String type) throws MissingDataException {
        try {
            return transactionDao.getTransactionsByType(userId, type);
        } catch (Exception e) {
            throw new MissingDataException("Failed to retrieve transactions for user ID: " + userId + " with type: " + type);
        }
    }


    public List<Transaction> getTransactionsByDate(Date from, Date to, Long userId) throws MissingDataException {
        try {
            return transactionDao.getTransactionsByDate(from, to, userId);
        } catch (Exception e) {
            throw new MissingDataException("Failed to retrieve transactions by date for user ID: " + userId);
        }
    }

    public Transaction addTransaction(Transaction transaction) throws InvalidRequestException {
        try {
            return transactionDao.addTransaction(transaction);
        } catch (Exception e) {
            throw new InvalidRequestException("Failed to add transaction");
        }
    }

    public Transaction credit(Long userId, Long amount) throws InvalidRequestException, MissingDataException {
        if (amount <= 0) {
            throw new InvalidRequestException("Credit amount must be greater than zero.");
        }

        // Retrieve the wallet for the user
        Optional<Wallet> walletOpt = walletDao.getWalletByUserId(userId);

        if (!walletOpt.isPresent()) {
            throw new MissingDataException("Wallet does not exist for user ID: " + userId);
        }

        Wallet wallet = walletOpt.get();
        wallet.setBalance(wallet.getBalance() + amount);

        // Update the wallet
        walletDao.updateWallet(wallet);

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setType("CREDIT");
        transaction.setTimestamp(new Date());

        return transactionDao.addTransaction(transaction);
    }


    public Transaction debit(Long userId, Long amount) throws InvalidRequestException {
        if (amount <= 0) {
            throw new InvalidRequestException("Debit amount must be greater than zero.");
        }


            Optional<Wallet> walletOpt = walletDao.getWalletByUserId(userId);

            if (!walletOpt.isPresent()) {
                throw new InvalidRequestException("Wallet does not exist for user ID: " + userId);
            }

            Wallet wallet = walletOpt.get();
            if (wallet.getBalance() < amount) {

                throw new InvalidRequestException("Insufficient balance for user ID: " + userId);
            }

            wallet.setBalance(wallet.getBalance() - amount);
            walletDao.updateWallet(wallet);

            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setAmount(amount);
            transaction.setType("DEBIT");
            transaction.setTimestamp(new Date());
            return transactionDao.addTransaction(transaction);


    }
}

