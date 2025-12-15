package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.services.dto.Faq;
import edu.upc.dsa.services.dto.Question;
import edu.upc.dsa.services.dto.Video;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
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
    @ApiOperation(
            value = "Get list of FAQs"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "FAQ list returned correctly")
    })
    public Response getFaqs() {
        logger.info("Getting FAQs");
        List<Faq> faqs = new ArrayList<>();

        faqs.add(new Faq(
                "How can I catch rare or legendary fish?",
                "Get higher level fishing rods and fish in the deep areas of the map. "
                        + "Rare fish have a lower spawn rate, so you will need some patience."
        ));

        faqs.add(new Faq(
                "What happens if another player eliminates me while I am fishing?",
                "You lose the current run and any fish you haven’t sold yet, "
                        + "but you keep all the rods and upgrades you have already bought."
        ));

        faqs.add(new Faq(
                "How can I earn more coins?",
                "Sell the fish you catch in the shop and complete daily missions or challenges. "
                        + "Rare and legendary fish give you many more coins."
        ));

        faqs.add(new Faq(
                "What are new fishing rods useful for?",
                "Each rod increases the chance of catching more valuable fish, "
                        + "reduces the waiting time while fishing and can increase your casting distance."
        ));

        faqs.add(new Faq(
                "Can I lose a fishing rod that I already bought?",
                "No. Once you buy a rod, it stays linked to your account and you won’t lose it, "
                        + "even if other players eliminate you."
        ));

        faqs.add(new Faq(
                "How can I avoid being eliminated so easily by other players?",
                "Try not to stay too long in the same area, sell your fish often so you don’t risk all your earnings, "
                        + "and upgrade your gear to have better chances to escape."
        ));

        GenericEntity<List<Faq>> entity = new GenericEntity<List<Faq>>(faqs) {};
        return Response.ok(entity).build();
    }



    @POST
    @Path("/question")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Receive a question from the app")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Question received", response = Question.class),
            @ApiResponse(code = 400, message = "Invalid question")
    })
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

        SystemManager.receiveQuestion(question);

        return Response.status(Response.Status.CREATED).entity(question).build();
    }

    @GET
    @Path("/videos")
    @ApiOperation(value = "Get list of videos"    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Video list returned correctly")
    })
    public Response getVideos() {
        logger.info("Getting videos");

        List<Video> videos = new ArrayList<Video>();

        videos.add(new Video(
                "Getting Started with Fishing Adventure",
                "https://www.youtube.com/watch?v=Zcb8yPEItwA"
        ));
        videos.add(new Video(
                "Top 10 Tips to Catch Rare Fish",
                "https://www.youtube.com/watch?v=EIm4HvDgQCM"
        ));
        videos.add(new Video(
                "How to Upgrade Your Fishing Rods Effectively",
                "https://www.youtube.com/watch?v=IY2AMo_yCs4"
        ));
        GenericEntity<List<Video>> entity = new GenericEntity<List<Video>>(videos) {};
        return Response.ok(entity).build();
    }



}
