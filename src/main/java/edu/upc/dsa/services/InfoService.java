package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.User;
import edu.upc.dsa.services.dto.*;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/info", description = "Info endpoints")
@Path("/info")
@Produces(MediaType.APPLICATION_JSON)
public class InfoService {
    Logger logger = Logger.getLogger(InfoService.class);

    @GET
    @Path("/faqs")
    public Response getFaqs() {
        logger.info("Getting FAQs");
        List<Faq> faqs = SystemManager.getFaqs();

        GenericEntity<List<Faq>> entity = new GenericEntity<List<Faq>>(faqs) {};
        return Response.ok(entity).build();
    }

    @POST
    @Path("/question")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postQuestion(Question question) {
        logger.info("posting question: " + question);

        if (question == null ||
                question.getTitle() == null || question.getTitle().isEmpty() ||
                question.getMessage() == null || question.getMessage().isEmpty() ||
                question.getSender() == null || question.getSender().isEmpty()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required fields: title, message, sender")
                    .build();
        }

        if (question.getDate() == null || question.getDate().isEmpty()) {
            question.setDate(LocalDateTime.now().toString());
        }

        SystemManager.postFaqQuestion(question);

        return Response.status(Response.Status.CREATED).entity(question).build();
    }

    @GET
    @Path("/videos")
    public Response getVideos() {
        logger.info("Getting videos");

        List<Video> videos = SystemManager.getVideos();

        GenericEntity<List<Video>> entity = new GenericEntity<List<Video>>(videos) {};
        return Response.ok(entity).build();
    }

    @GET
    @Path("/teams/ranking")
    @ApiResponse( code = 200, message = "OK", response = List.class)
    public Response getTeamsRanking() {
        logger.info("Getting teams");

        List<TeamRanking> ranking = SystemManager.getTeamsRanking();
        GenericEntity<List<TeamRanking>> entity = new GenericEntity<List<TeamRanking>>(ranking) {};
        return Response.ok(entity).build();
    }

    @GET
    @Path("/teams/{teamName}")
    @ApiResponse( code = 200, message = "OK", response = Team.class)
    public Response getTeamMembers(@PathParam("teamName") String teamName) {
        Team team = SystemManager.getTeam(teamName);
        return Response.ok(team).build();
    }

    @GET
    @Path("/leaderboard")
    @ApiOperation(value = "Get leaderboard by fishes currently captured")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = LeaderboardEntry.class, responseContainer = "List")
    })
    public Response getLeaderboard() {
        List<LeaderboardEntry> top = SystemManager.getFishLeaderboardCurrent();
        return Response.ok(new GenericEntity<List<LeaderboardEntry>>(top) {}).build();
    }

    @GET
    @Path("/events/{eventId}")
    @ApiOperation(value = "Get users registered in an event")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = EventUser.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public Response getRegisteredUsers(
            @ApiParam(value = "Event ID", required = true, example = "1")
            @PathParam("eventId") int eventId
    ) {
        List<EventUser> users = SystemManager.getUsersRegisteredInEvent(eventId);

        return Response.ok(new GenericEntity<List<EventUser>>(users) {}).build();
    }

}