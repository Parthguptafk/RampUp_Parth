package org.Flipkart;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.Flipkart.core.Transaction;
import org.Flipkart.core.User;
import org.Flipkart.core.Wallet;
import org.Flipkart.dao.TransactionDao;
import org.Flipkart.dao.UserDao;
import org.Flipkart.dao.WalletDao;
import org.Flipkart.resource.TransactionResource;
import org.Flipkart.resource.UserResource;
import org.Flipkart.resource.WalletResource;
import org.Flipkart.service.TransactionService;
import org.Flipkart.service.UserService;
import org.Flipkart.service.WalletService;



public class ManagerApplication extends Application<ManagerConfiguration> {

    private final HibernateBundle<ManagerConfiguration> hibernate = new HibernateBundle<ManagerConfiguration>(Transaction.class, Wallet.class, User.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(ManagerConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(String[] args) throws Exception {
        new ManagerApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ManagerConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(ManagerConfiguration configuration,
                    Environment environment) {
        System.out.println("Starting Wallet application...");
        final UserDao userDao = new UserDao(hibernate.getSessionFactory());
        final TransactionDao transactionDao = new TransactionDao(hibernate.getSessionFactory());
        final WalletDao walletDao = new WalletDao(hibernate.getSessionFactory());

        final UserService userService = new UserService(userDao);
        final TransactionService transactionService = new TransactionService(transactionDao, walletDao);
        final WalletService walletService = new WalletService(walletDao);

        environment.jersey().register(new UserResource(userService));
        environment.jersey().register(new TransactionResource(transactionService));
        environment.jersey().register(new WalletResource(walletService));


    }
}