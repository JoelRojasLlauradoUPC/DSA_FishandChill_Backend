/*package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.SystemManagerImpl;
import edu.upc.dsa.models.*;
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

@Api(value = "/shop", description = "Shop Service")
@Path("/shop")
public class shopService {

    private final SystemManager sm = SystemManagerImpl.getInstance();

    @GET
    @Path("/load")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Load user information into the shop", notes = "Get user coins and items that the user has not already bought.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Coins and items loaded"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public Response loadUserData(@PathParam("id") String userId) {
        User userShopping = sm.getUser(userId);
        if (userShopping == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        else{
            List<FishingRod> rodsToShop = filterNotBoughtRods(userID);
        }
    }


}*/
