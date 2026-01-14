package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.services.dto.LeaderboardEntry;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Api(value = "/leaderboard", description = "Leaderboard endpoints")
@Path("/leaderboard")
@Produces(MediaType.APPLICATION_JSON)
public class LeaderboardService {

    @GET
    @Path("/fishes")
    @ApiOperation(value = "Get leaderboard by fishes currently captured")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = LeaderboardEntry.class, responseContainer = "List")
    })
    public Response getLeaderboard(@QueryParam("limit") @DefaultValue("50") int limit) {
        List<LeaderboardEntry> top = SystemManager.getFishLeaderboardCurrent(limit);
        return Response.ok(new GenericEntity<List<LeaderboardEntry>>(top) {}).build();
    }
}
