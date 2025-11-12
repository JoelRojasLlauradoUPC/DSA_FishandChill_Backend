package edu.upc.dsa.services;

import edu.upc.dsa.SystemManager;
import edu.upc.dsa.models.Fish;
import edu.upc.dsa.models.FishingRod;
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

    private final SystemManager gm = SystemManager.getInstance();

    @GET
    @Path("/species")
    @ApiOperation(value = "List all fish species")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = Fish.class, responseContainer = "List")
    })
    public Response listSpecies() {
        Collection<Fish> values = gm.getAllFishSpecies().values();
        return Response.ok(values).build();
    }

    @GET
    @Path("/rods")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List all fishing rods")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = FishingRod.class, responseContainer = "List")
    })
    public Response listRods() {
        List<FishingRod> rods = new ArrayList<>(gm.getAllFishingRods().values());
        return Response.ok(new GenericEntity<List<FishingRod>>(rods) {}).build();
    }


    @POST
    @Path("/species/add")
    @ApiOperation(value = "Add a fish species")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Fish created", response = Fish.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "Fish already exists")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSpecies(Fish fish) {
        if (fish == null) return Response.status(Response.Status.BAD_REQUEST).entity("Fish body required").build();
        int res = gm.addFishSpecies(fish);
        if (res == -1) return Response.status(Response.Status.CONFLICT).entity("Fish already exists").build();
        return Response.status(Response.Status.CREATED).entity(fish).build();
    }

    @POST
    @Path("/rods/add")
    @ApiOperation(value = "Add a fishing rod")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Rod created", response = FishingRod.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "Rod already exists")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRod(FishingRod rod) {
        if (rod == null) return Response.status(Response.Status.BAD_REQUEST).entity("Rod body required").build();
        int res = gm.addFishingRod(rod);
        if (res == -1) return Response.status(Response.Status.CONFLICT).entity("Rod already exists").build();
        return Response.status(Response.Status.CREATED).entity(rod).build();
    }

    @GET
    @Path("/rods/loadAll")
    @Produces(MediaType.TEXT_PLAIN)
    public Response loadDictionaryGet() {
        int res = gm.loadRodsDictionary();
        if (res == -1) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Not possible to load rod dictionary").build();
        }
        return Response.ok("Rod dictionary loaded").build(); // 200 OK
    }

}
