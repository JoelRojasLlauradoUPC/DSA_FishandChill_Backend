package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.Question;
import io.swagger.annotations.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@Api(value = "/question", description = "Question endpoints")
@Path("/question")
@Produces(MediaType.APPLICATION_JSON)
public class InfoService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Receive a question from the app")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Question received", response = Question.class),
            @ApiResponse(code = 400, message = "Invalid question")
    })
    public Response postQuestion(Question question) {

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

        SystemManager.receiveQuestion(question);

        return Response.status(Response.Status.CREATED).entity(question).build();
    }
}
