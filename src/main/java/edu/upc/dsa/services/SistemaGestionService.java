package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.SystemManagerImpl;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Api(value = "/game", description = "Game Service")
@Path("/game")
public class SistemaGestionService {

    private final SystemManager sm = SystemManagerImpl.getInstance();

    // ---------- USERS ----------

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a new User", notes = "Create user with username / password / email")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public Response createUser(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Faltan campos obligatorios (username / password)")
                    .build();
        }

        // si no viene id, lo generamos
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }

        User created = sm.createUser(user);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/user/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get user info", notes = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public Response getUser(@PathParam("id") String userId) {
        User u = sm.getUser(userId);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User no encontrado")
                    .build();
        }
        return Response.ok(u).build();
    }

    @PATCH
    @Path("/user/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update an existing User", notes = "Partial update")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public Response updateUser(@PathParam("userId") String userId, User changes) {
        User updated = sm.updateUser(userId, changes);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User no encontrado")
                    .build();
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/user/{userId}")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Delete an existing User", notes = "Delete user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User deleted"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public Response deleteUser(@PathParam("userId") String userId) {
        User u = sm.getUser(userId);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User no encontrado")
                    .build();
        }
        sm.deleteUser(userId);
        return Response.ok("User eliminado").build();
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all users", notes = "Returns the list of all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users returned")
    })
    public Response getAllUsers() {
        List<User> users = sm.getAllUsers();

        // IMPORTANTE: envolvemos la lista para que Jersey pueda serializarla a JSON
        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
        return Response.ok(entity).build();
    }

    // ---------- LOGIN ----------

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "User login", notes = "Login with username + password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login OK"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Invalid credentials")
    })
    public Response login(User loginData) {
        String username = loginData != null ? loginData.getUsername() : null;
        String password = loginData != null ? loginData.getPassword() : null;

        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Faltan username o password")
                    .build();
        }

        // Buscar usuario por username
        User found = null;
        for (User u : sm.getAllUsers()) {
            if (username.equals(u.getUsername())) {
                found = u;
                break;
            }
        }

        if (found == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Usuario no encontrado")
                    .build();
        }

        if (!password.equals(found.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Contraseña incorrecta")
                    .build();
        }

        return Response.ok(found).build();
    }
}
