package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.User;
import edu.upc.dsa.services.dto.CapturedFish;
import edu.upc.dsa.services.dto.FishingRod;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Api(value = "/me", description = "Self endpoints")
@Path("/me")
@Produces(MediaType.APPLICATION_JSON)
public class MeService {

//    private final SystemManager gm = SystemManager.getInstance();

    @GET
    @ApiOperation(value = "Get my profile")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = User.class),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response me(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        User u = SystemManager.authenticate(auth);
        if (u == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        return Response.ok(u).build();
    }

    @GET
    @Path("/captured_fishes")
    @ApiOperation(value = "Get my captured fishes")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = CapturedFish.class, responseContainer = "List") ,
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response getMyCapturedFishes(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        User user = SystemManager.authenticate(auth);
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        List<edu.upc.dsa.models.CapturedFish> capturedFishes = SystemManager.getCapturedFishes(user);
        List<CapturedFish> resCapturedFishes = new ArrayList<CapturedFish>();
        for (edu.upc.dsa.models.CapturedFish cf : capturedFishes) {
            resCapturedFishes.add(new CapturedFish(cf.getFishSpecies(), cf.getWeight(), cf.getCaptureTime()));
        }

        return Response.ok(new GenericEntity<List<CapturedFish>> (resCapturedFishes) {}).build();
    }

    @GET
    @Path("/owned_fishing_rods")
    @ApiOperation(value = "Get my owned fishing rods")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = FishingRod.class, responseContainer = "List") ,
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response getMyOwnedFishingRods(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        User user = SystemManager.authenticate(auth);
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        List<edu.upc.dsa.models.FishingRod> ownedFishingRods = SystemManager.getOwnedFishingRods(user);
        List<FishingRod> resOwnedFishingRods= new ArrayList<FishingRod>();
        for (edu.upc.dsa.models.FishingRod fr : ownedFishingRods) {
            resOwnedFishingRods.add(new FishingRod(fr));
        }
        return Response.ok(new GenericEntity<List<FishingRod>> (resOwnedFishingRods) {}).build();
    }

    @DELETE
    @ApiOperation(value = "Delete my user account")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User deleted"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response deleteMyAccount(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        User user = SystemManager.authenticate(auth);
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        SystemManager.deleteUser(user);
        return Response.ok().entity("User deleted").build();
    }

}
