package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.User;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/game", tags = {"game"})
@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
public class GameService {
    private final SystemManager gm = SystemManager.getInstance();

    public static class CaptureRequest {
        public String fishId;
        public double weight;
        public CaptureRequest() {}
    }

    @POST
    @Path("/capture")
    @ApiOperation(value = "Capture a fish in-game")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Fish captured", response = Inventory.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Fish or User not found")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response capture(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth, CaptureRequest req) {
        User u = gm.authenticate(auth);
        if (u == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        if (req == null || req.fishId == null) return Response.status(Response.Status.BAD_REQUEST).build();
        int res = gm.capturedFish(u.getUsername(), req.fishId, req.weight);
        if (res == -2) return Response.status(Response.Status.NOT_FOUND).entity("Fish not found").build();
        if (res == -1) return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        return Response.ok(u.getInventory()).build();
    }
}
