package org.Flipkart.resource;

import io.dropwizard.hibernate.UnitOfWork;
import javax.transaction.Transactional;
import org.Flipkart.core.Transaction;
import org.Flipkart.dto.CreditDebitRequest;
import org.Flipkart.exception.*;
import org.Flipkart.service.TransactionService;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Path("/")
@Transactional
public class TransactionResource {

    Logger logger = Logger.getLogger(TransactionResource.class.getName());
    private final TransactionService transactionService;

    @Inject
    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transaction/{id}")
    public Response getTransactionByUser(@PathParam("id") Long id) throws InvalidRequestException {
        List<Transaction> transactions = transactionService.getTransactionByUser(id);
        if (transactions.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No transactions found for user ID: " + id).build();
        }
        return Response.ok(transactions).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transaction/date")
    public Response getTransactionByDate(
            @QueryParam("startDateTime") String startDateTimeStr,
            @QueryParam("endDateTime") String endDateTimeStr,
            @QueryParam("userId") Long userId) throws MissingDataException {


        Date startDateTime;
        Date endDateTime;

        try {
            startDateTime = java.sql.Timestamp.valueOf(startDateTimeStr);
            endDateTime = java.sql.Timestamp.valueOf(endDateTimeStr);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid date-time format. Use 'YYYY-MM-DD HH:MM:SS'.")
                    .build();
        }


        List<Transaction> transactions = transactionService.getTransactionsByDate(startDateTime, endDateTime, userId);

        if (transactions.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No transactions found for user ID: " + userId + " within the given date range.")
                    .build();
        }

        return Response.ok(transactions).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transaction/type")
    public Response getTransactionByType(
            @QueryParam("userId") Long userId,
            @QueryParam("type") String type) throws MissingDataException {

        List<Transaction> transactions = transactionService.getTransactionsByType(userId, type);
        if (transactions.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No transactions found for user ID: " + userId + " with type: " + type)
                    .build();
        }
        return Response.ok(transactions).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transaction/add")
    public Response addTransaction(@Valid Transaction transaction) throws InvalidRequestException {

        Transaction addedTransaction = transactionService.addTransaction(transaction);


        return Response.status(Response.Status.CREATED)
                .entity(addedTransaction)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transaction/credit")
    public Response credit(@Valid CreditDebitRequest request) throws InvalidRequestException, MissingDataException {
        try {
            Transaction transaction = transactionService.credit(request.getUserId(), request.getAmount());
            return Response.ok(transaction).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transaction/debit")
    public Response debit(@Valid CreditDebitRequest request) throws InvalidRequestException {
        try {
            Transaction transaction = transactionService.debit(request.getUserId(), request.getAmount());
            return Response.ok(transaction).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
