package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.User;
import edu.upc.dsa.models.dto.UserLogin;
import edu.upc.dsa.models.dto.UserRegister;
import edu.upc.dsa.models.dto.Token;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/auth", description = "Authentication endpoints")
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthService {

    private final SystemManager gameManager = SystemManager.getInstance();

    @POST
    @Path("/register")
    @ApiOperation(value = "Register a new user")
    @ApiResponses({
            @ApiResponse(code = 201, message = "User created", response = User.class),
            @ApiResponse(code = 409, message = "User already exists"),
            @ApiResponse(code = 400, message = "Missing fields")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(UserRegister req) {
        if (req == null) return Response.status(Response.Status.BAD_REQUEST).entity("Body required").build();
        String username = req.getUsername();
        String password = req.getPassword();
        String email = req.getEmail();
        if (username == null || password == null || email == null
                || username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("username, password, email are required").build();
        }
        int res = gameManager.createUser(username, password, email);
        if (res == -1) return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
        User u = gameManager.getUser(username);
        return Response.status(Response.Status.CREATED).entity(u).build();
    }

    @POST
    @Path("/login")
    @ApiOperation(value = "Login and get token")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Login ok", response = Token.class),
        @ApiResponse(code = 401, message = "Invalid credentials")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserLogin req) {
        if (req == null) return Response.status(Response.Status.BAD_REQUEST).entity("Body required").build();
        String username = req.getUsername();
        String password = req.getPassword();
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("username and password required").build();
        String token = gameManager.login(username, password);
        if (token == null)
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        return Response.ok(new Token(token)).build();
    }
}
