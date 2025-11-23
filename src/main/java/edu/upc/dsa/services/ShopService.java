package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.FishingRod;
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

//    private final SystemManager gm = SystemManager.getInstance();

    @POST
    @Path("/fishing_rods/{fishing_rod_name}/buy")
    @ApiOperation(value = "Buy a fishing rod")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Fishing rod bought"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Fishing rod not found"),
            @ApiResponse(code = 409, message = "Fishing rod already owned or not enough coins")
    })
    public Response buyFishingRod(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth,
                                    @PathParam("fishing_rod_name") String fishingRodName) {

        User user = SystemManager.authenticate(auth);
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        FishingRod fishingRod = SystemManager.getFishingRod(fishingRodName);
        if (fishingRod == null) return Response.status(Response.Status.NOT_FOUND).entity("Fishing rod not found").build();
        int res = SystemManager.buyFishingRod(user, fishingRod);
        if (res == -1) return Response.status(Response.Status.CONFLICT).entity("Fishing rod already owned").build();
        else if (res == -2) return Response.status(Response.Status.CONFLICT).entity("Not enough coins").build();
        return Response.status(Response.Status.OK).build();
    }
}
