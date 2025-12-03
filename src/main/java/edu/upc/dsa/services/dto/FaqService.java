package edu.upc.dsa.services.dto;

import edu.upc.dsa.models.Faq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/faqs", description = "FAQs endpoints")
@Path("/faqs")
@Produces(MediaType.APPLICATION_JSON)
public class FaqService {

    @GET
    @ApiOperation(
            value = "Get list of FAQs",
            notes = "Returns the list of frequently asked questions for the fishing game",
            response = Faq.class,
            responseContainer = "List"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "FAQ list returned correctly")
    })
    public Response getFaqs() {

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
}
