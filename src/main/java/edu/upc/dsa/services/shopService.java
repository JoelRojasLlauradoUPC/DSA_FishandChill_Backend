package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.User;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/shop", description = "Shop endpoints")
@Path("/shop")
@Produces(MediaType.APPLICATION_JSON)
public class ShopService {

    private final SystemManager gm = SystemManager.getInstance();

    @POST
    @Path("/rods/{rodId}/buy")
    @ApiOperation(value = "Buy a fishing rod")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Bought", response = Inventory.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Rod not found"),
            @ApiResponse(code = 409, message = "Already owned or not enough coins")
    })
    public Response buy(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth,
                        @PathParam("rodId") String rodId) {
        User u = gm.authenticate(auth);
        if (u == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        int res = gm.boughtFishingRod(u.getUsername(), rodId);
        if (res == -2) return Response.status(Response.Status.NOT_FOUND).entity("Rod not found").build();
        if (res == -3) return Response.status(Response.Status.CONFLICT).entity("Not enough coins").build();
        if (res == -4) return Response.status(Response.Status.CONFLICT).entity("Already owned").build();
        if (res != 1) return Response.status(Response.Status.BAD_REQUEST).entity("Unknown error").build();
        return Response.ok(u.getInventory()).build();
    }
}
