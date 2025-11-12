package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.User;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/me", description = "Self endpoints")
@Path("/me")
@Produces(MediaType.APPLICATION_JSON)
public class MeService {

    private final SystemManager gm = SystemManager.getInstance();

    @GET
    @ApiOperation(value = "Get my profile")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = User.class),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response me(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        User u = gm.authenticate(auth);
        if (u == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        return Response.ok(u).build();
    }

    @GET
    @Path("/inventory")
    @ApiOperation(value = "Get my inventory")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = Inventory.class),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response inventory(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        User u = gm.authenticate(auth);
        if (u == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        Inventory inv = u.getInventory();
        return Response.ok(inv).build();
    }
}
