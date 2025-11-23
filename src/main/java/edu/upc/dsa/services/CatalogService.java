package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.services.dto.CapturedFish;
import edu.upc.dsa.services.dto.Fish;
import edu.upc.dsa.services.dto.FishingRod;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Api(value = "/catalog", description = "Catalog endpoints")
@Path("/catalog")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogService {

//    private final SystemManager gm = SystemManager.getInstance();

    @GET
    @Path("/fishes")
    @ApiOperation(value = "List all fish species")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = Fish.class, responseContainer = "List")
    })
    public Response getAllFishes() {
        List<edu.upc.dsa.models.Fish> allFishes = SystemManager.getAllFishes();
        List<Fish> resAllFishes = new ArrayList<>();
        for (edu.upc.dsa.models.Fish f : allFishes) {
            resAllFishes.add(new Fish(f));
        }
        return Response.ok(new GenericEntity<List<Fish>> (resAllFishes) {}).build();
    }

    @GET
    @Path("/fishes/{species_name}")
    @ApiOperation(value = "Get fish species by name")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = Fish.class),
            @ApiResponse(code = 404, message = "Species not found")
    })
    public Response getFish(@PathParam("species_name") String speciesName) {
        edu.upc.dsa.models.Fish fish = SystemManager.getFish(speciesName);
        if (fish == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Species not found").build();
        }
        Fish resFish = new Fish(fish);
        return Response.ok(resFish).build();
    }

    @GET
    @Path("/fishing_rods")
    @ApiOperation(value = "List all fishing rods")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = FishingRod.class, responseContainer = "List")
    })
    public Response getAllFishingRods() {
        List<edu.upc.dsa.models.FishingRod> allFishingRods = SystemManager.getAllFishingRods();
        List<FishingRod> resAllFishingRods = new ArrayList<>();
        for (edu.upc.dsa.models.FishingRod r : allFishingRods) {
            resAllFishingRods.add(new FishingRod(r));
        }
        return Response.ok(new GenericEntity<List<FishingRod>> (resAllFishingRods) {}).build();
    }

    @GET
    @Path("/fishing_rods/{fishing_rod_name}")
    @ApiOperation(value = "Get fishing rod by name")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = FishingRod.class),
            @ApiResponse(code = 404, message = "Fishing rod not found")
    })
    public Response getFishingRod(@PathParam("fishing_rod_name") String fishingRodName) {
        edu.upc.dsa.models.FishingRod rod = SystemManager.getFishingRod(fishingRodName);
        if (rod == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Fishing rod not found").build();
        }
        FishingRod resRod = new FishingRod(rod);
        return Response.ok(resRod).build();
    }
}
