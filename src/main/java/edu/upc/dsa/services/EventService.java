package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.services.dto.EventUser;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/events", description = "Events endpoints")
@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
public class EventService {

    @GET
    @Path("/{eventId}/users")
    @ApiOperation(value = "Get users registered in an event")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = EventUser.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public Response getRegisteredUsers(
            @ApiParam(value = "Event ID", required = true, example = "1")
            @PathParam("eventId") String eventId
    ) {
        if (eventId == null || eventId.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("eventId is required").build();
        }

        List<EventUser> users = SystemManager.getUsersRegisteredInEvent(eventId);

        return Response.ok(new GenericEntity<List<EventUser>>(users) {}).build();
    }
}
