package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.User;
import edu.upc.dsa.services.dto.RequestCapturedFish;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/game", tags = {"game"})
@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
public class GameService {
//    private final SystemManager gm = SystemManager.getInstance();

    public static class CaptureRequest {
        public String fishId;
        public double weight;
        public CaptureRequest() {}
    }

    @POST
    @Path("/captured")
    @ApiOperation(value = "Capture a fish in-game")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Fish captured"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Fish or User not found")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response capture(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth, RequestCapturedFish req) {
        User user = SystemManager.authenticate(auth);
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        edu.upc.dsa.models.Fish fish = SystemManager.getFish(req.getSpeciesName());
        if (fish == null) return Response.status(Response.Status.NOT_FOUND).entity("Fish species not found").build();
        SystemManager.captureFish(user, fish, req.getWeight());
        return Response.ok().build();
    }
}
