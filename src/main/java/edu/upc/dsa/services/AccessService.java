package edu.upc.dsa.services;

import edu.upc.dsa.GameManager;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.User;
import io.swagger.annotations.*;

import javax.ws.rs.*;

@Api(value = "/game", description = "Endpoint to Fishing Game Service")
@Path("/game")
public class AccessService {

    private GameManager gameManager;

    public AccessService() {
        this.gameManager = GameManager.getInstance();

    }
//
//    // ---------------- USERS ----------------
//
//    @GET
//    @ApiOperation(value = "Get user info", notes = "User basic info", response = User.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successful", response = User.class),
//            @ApiResponse(code = 404, message = "User not found")
//    })
//    @Path("/user/status/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getUser(@PathParam("id") String id) {
//        User user = this.sistemaGestion.getUser(id);
//        if (user == null)
//            return Response.status(404).entity("User not found").build();
//        return Response.status(200).entity(user).build();
//    }
//
//    @POST
//    @ApiOperation(value = "Create a new User", notes = "Creates user with id and username")
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "User created", response = User.class),
//            @ApiResponse(code = 400, message = "Validation error")
//    })
//    @Path("/user")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response createUser(User user) {
//        if (user.getId() == null || user.getUsername() == null) {
//            return Response.status(400).entity("id and username are required").build();
//        }
//        User created = this.sistemaGestion.createUser(user);
//        return Response.status(201).entity(created).build();
//    }
//
//    @PATCH
//    @ApiOperation(value = "Update an existing User", notes = "Partial update")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "User updated", response = User.class),
//            @ApiResponse(code = 404, message = "User not found")
//    })
//    @Path("/user/{userId}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response updateUser(@PathParam("userId") String userId, User user) {
//        User updated = this.sistemaGestion.updateUser(userId, user);
//        if (updated == null)
//            return Response.status(404).entity("User not found").build();
//        return Response.status(200).entity(updated).build();
//    }
//
//    @DELETE
//    @ApiOperation(value = "Delete an existing User", notes = "Deletes user by id")
//    @ApiResponses(value = {
//            @ApiResponse(code = 204, message = "User deleted"),
//            @ApiResponse(code = 404, message = "User not found")
//    })
//    @Path("/user/{userId}")
//    public Response deleteUser(@PathParam("userId") String userId) {
//        User u = this.sistemaGestion.getUser(userId);
//        if (u == null)
//            return Response.status(404).entity("User not found").build();
//        this.sistemaGestion.deleteUser(userId);
//        return Response.status(204).build();
//    }
//
//    @GET
//    @ApiOperation(value = "Get all users", notes = "Returns a list of all users")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successful", response = User.class, responseContainer = "List")
//    })
//    @Path("/users")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAllUsers() {
//        List<User> users = this.sistemaGestion.getAllUsers();
//        return Response.status(200).entity(users).build();
//    }

    /*@POST
    @ApiOperation(value = "Create a new user only giving essential parameters", notes = "Creates an user by only giving 3 parameters.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Validation error")
    })
    @Path("/user/create")
    public Response createNewUser(@PathParam("name") String name, @PathParam("password") String password, @PathParam("email") String email) {



    }*/
}
