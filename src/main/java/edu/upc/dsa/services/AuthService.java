package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.User;
import edu.upc.dsa.services.dto.Login;
import edu.upc.dsa.services.dto.Register;
import edu.upc.dsa.services.dto.Token;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/auth", description = "Authentication endpoints")
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthService {

//    private final SystemManager gameManager = SystemManager.getInstance();

    @POST
    @Path("/register")
    @ApiOperation(value = "Register a new user")
    @ApiResponses({
            @ApiResponse(code = 201, message = "User created", response = User.class),
            @ApiResponse(code = 409, message = "User already exists"),
            @ApiResponse(code = 400, message = "Missing fields")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(Register req) {
        if (req == null) return Response.status(Response.Status.BAD_REQUEST).entity("Body required").build();
        String username = req.getUsername();
        String password = req.getPassword();
        String email = req.getEmail();
        if (username == null || password == null || email == null
                || username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("username, password, email are required").build();
        }
        int res = SystemManager.createUser(username, password, email);
        if (res == -1) return Response.status(Response.Status.CONFLICT).entity("Username already exists").build();
        else if (res == -2) return Response.status(Response.Status.CONFLICT).entity("Email already used").build();
        User u = SystemManager.getUser(username);
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
    public Response login(Login req) {
        if (req == null) return Response.status(Response.Status.BAD_REQUEST).entity("Body required").build();
        String username = req.getUsername();
        String password = req.getPassword();
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("username and password required").build();
        String token = SystemManager.login(username, password);
        if (token == null)
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        return Response.ok(new Token(token)).build();
    }

    @DELETE
    @Path("/logout")
    @ApiOperation(value = "Logout and invalidate token")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Logout successful"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        User user = SystemManager.authenticate(auth);
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        SystemManager.logout(auth);
        return Response.ok().build();
    }
}
