package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.User;
import edu.upc.dsa.services.dto.CapturedFish;
import edu.upc.dsa.services.dto.FishingRod;
import edu.upc.dsa.services.dto.Team;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;

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
    Logger logger = Logger.getLogger(MeService.class);
//    private final SystemManager gm = SystemManager.getInstance();

    @GET
    @ApiOperation(value = "Get my profile")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = edu.upc.dsa.services.dto.User.class) ,
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public Response me(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        User u = SystemManager.authenticate(auth);
        edu.upc.dsa.services.dto.User user = SystemManager.getUser(u.getUsername());

        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        return Response.ok(user).build();
    }

    @GET
    @Path("/change_avatar")
    @ApiOperation(value = "Change my avatar")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Avatar changed", response = edu.upc.dsa.services.dto.User.class) ,
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public Response changeAvatar(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        User u = SystemManager.authenticate(auth);
        if (u == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        String baseAvatarUrl = "https://api.dicebear.com/9.x/avataaars/png?seed=";
        int randomNum = (int)(Math.random() * 10000);
        String avatarUrl = baseAvatarUrl + randomNum + "&size=200";
        SystemManager.updateAvatarUrl(u, avatarUrl);
        u.setAvatarUrl(avatarUrl);
        edu.upc.dsa.services.dto.User user = SystemManager.getUser(u.getUsername());
        return Response.ok(avatarUrl).build();
    }

    @POST
    @Path("/teams/{teamName}/create")
    @ApiOperation(value = "Create a team")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Team created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 409, message = "Team already exists"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public Response createTeam(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth,
                               @PathParam("teamName") String teamName) {
        User user = SystemManager.authenticate(auth);
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        String baseImageUrl = "https://api.dicebear.com/9.x/icons/png?seed=";
        int randomNum = (int)(Math.random() * 10000);
        String imageUrl = baseImageUrl + randomNum + "&size=200";
        int res = SystemManager.createTeam(teamName, imageUrl);
        if (res == -1) return Response.status(Response.Status.CONFLICT).entity("Team already exists").build();
        SystemManager.joinTeam(user, teamName);
        return Response.status(Response.Status.OK).entity("Team created").build();
    }

    @GET
    @Path("/teams/{teamName}/join")
    @ApiOperation(value = "Join a team")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Joined team"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Team not found"),
            @ApiResponse(code = 409, message = "Already in team")
    })
    public Response joinTeam(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth,
                             @PathParam("teamName") String teamName) {
        User user = SystemManager.authenticate(auth);
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        int res = SystemManager.joinTeam(user, teamName);
        if (res == -1) return Response.status(Response.Status.NOT_FOUND).entity("Team not found").build();
        else if (res == -2) return Response.status(Response.Status.CONFLICT).entity("Already in team").build();
        return Response.status(Response.Status.OK).entity("Joined team").build();
    }

    @GET
    @Path("/teams/leave")
    @ApiOperation(value = "Leave a team")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Left team"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    public Response leaveTeam(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
        User user = SystemManager.authenticate(auth);
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        SystemManager.leaveTeam(user);
        return Response.status(Response.Status.OK).entity("Left team").build();
    }



    @POST
    @Path("events/{eventId}/subscribe")
    @ApiOperation(value = "Subscribe to an event")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Subscribed to event")
    })
    public Response subscribeToEvent(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth,
                                     @PathParam("eventId") int eventId) {
        User user = SystemManager.authenticate(auth);
        if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        SystemManager.subscribeToEvent(user, eventId);
        return Response.status(Response.Status.OK).entity("Subscribed to event").build();
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
