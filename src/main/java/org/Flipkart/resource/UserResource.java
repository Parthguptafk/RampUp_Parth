package org.Flipkart.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.Flipkart.core.User;
import org.Flipkart.exception.InvalidRequestException;
import org.Flipkart.exception.MissingDataException;
import org.Flipkart.service.UserService;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.logging.Logger;

@Path("/")
public class UserResource {

    Logger logger = Logger.getLogger(UserResource.class.getName());
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users/{id}")
    @UnitOfWork
    public Response getUserById(@PathParam("id") Long id) throws InvalidRequestException {
        final Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return Response.status(200).entity(user.get()).build();
        } else {
            return Response.status(400).entity("User with given id value does not exist").build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users")
    @UnitOfWork
    public Response addUser(@Valid User user) throws MissingDataException {
        logger.info("Saved");
        User savedUser = userService.addUser(user);
        return Response.status(201).entity(savedUser).build();
    }

}
