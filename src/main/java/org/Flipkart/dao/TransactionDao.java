package org.Flipkart.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.Flipkart.core.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.util.Date;
import java.util.List;

public class TransactionDao extends AbstractDAO<Transaction> {
    public TransactionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public List<Transaction> getTransactionByUser(Long userId){
        Query<Transaction> query = currentSession().createNamedQuery("core.Transaction.findAllTransactionByUserId", Transaction.class);
        query.setParameter("id", userId);
        return query.getResultList();
    }

    public List<Transaction> getTransactionsByDate(Date startDate, Date endDate, Long userId) {
        Query<Transaction> query = currentSession().createNamedQuery("core.Transaction.getTransactionByDate", Transaction.class);
        query.setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("id", userId);
        return query.getResultList();

    }

    public Transaction addTransaction(Transaction transaction) {

        return persist(transaction);
    }

    public List<Transaction> getTransactionsByType(Long userId, String type) {
        Query<Transaction> query = currentSession().createNamedQuery("core.Transaction.findTransactionByType", Transaction.class);
        query.setParameter("userId", userId);
        query.setParameter("type", type);
        return query.getResultList();
    }



}
