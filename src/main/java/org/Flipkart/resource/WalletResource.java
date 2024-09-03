package org.Flipkart.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.validation.Valid;
import io.dropwizard.hibernate.UnitOfWork;
import org.Flipkart.core.Wallet;
import org.Flipkart.exception.*;
import org.Flipkart.service.WalletService;
import java.util.Optional;

@Path("/")
public class WalletResource {

    private final WalletService walletService;


    public WalletResource(WalletService walletService) {
        this.walletService = walletService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("healthcheck")
    @UnitOfWork
    public Response healthCheck() {

        return Response.ok("Service is up and running").build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("wallet/add")
    @UnitOfWork
    public Response addWallet(@Valid Wallet wallet) throws InvalidRequestException {
        // Add wallet using the service
        Wallet addedWallet = walletService.addWallet(wallet);
        return Response.status(Response.Status.CREATED)
                .entity(addedWallet)
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("wallet/update")
    @UnitOfWork
    public Response updateWallet(@Valid Wallet wallet) throws InvalidRequestException {

        Wallet updatedWallet = walletService.updateWallet(wallet);
        return Response.ok(updatedWallet).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("balance/{userId}")
    @UnitOfWork
    public Response getBalanceById(@PathParam("userId") Long userId) throws InvalidRequestException {

        Optional<Long> balance = walletService.getBalanceById(userId);
        if (balance.isPresent()) {
            return Response.ok(balance.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Wallet not found for user ID: " + userId)
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("wallet/{userId}")
    @UnitOfWork
    public Response getWalletByUserId(@PathParam("userId") Long userId) throws InvalidRequestException {

        Optional<Wallet> wallet = walletService.getWalletByUserId(userId);
        if (wallet.isPresent()) {
            return Response.ok(wallet.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Wallet not found for user ID: " + userId)
                    .build();
        }


    }
}
