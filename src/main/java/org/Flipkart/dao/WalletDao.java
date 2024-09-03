package org.Flipkart.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.Flipkart.core.Wallet;
import org.Flipkart.exception.InvalidRequestException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.util.Optional;


public class WalletDao extends AbstractDAO<Wallet> {
    public WalletDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Wallet updateWallet(Wallet wallet) throws InvalidRequestException {

        Query<Wallet> query = currentSession().createNamedQuery("core.Wallet.getWalletByUserId", Wallet.class);
        query.setParameter("userId", wallet.getUserId());
        Wallet existingWallet = (Wallet) query.uniqueResult();

        if (existingWallet != null) {

            existingWallet.setBalance(wallet.getBalance());

            return persist(existingWallet);
        } else {

            throw new InvalidRequestException("Wallet not found for user ID: " + wallet.getUserId());
        }
    }

    public Optional<Long> getBalanceById(Long userId) {
        Query<Long> query = currentSession().createNamedQuery("core.Wallet.findBalance", Long.class);
        query.setParameter("userId", userId);
        Long balance = query.uniqueResult();
        return Optional.ofNullable(balance);
    }

    public Optional<Wallet> getWalletByUserId(Long id) {
        Query<Wallet> query = currentSession().createNamedQuery("core.Wallet.getWalletByUserId", Wallet.class);
        query.setParameter("userId", id);
        Wallet wallet = (Wallet) query.uniqueResult();
        return Optional.ofNullable(wallet);
    }

    public Wallet addWallet(Wallet wallet) {
        return persist(wallet);
    }

}
