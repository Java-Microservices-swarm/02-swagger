package hu.javalife.heroesofempires.hero;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author krisztian
 */
@Path("hero")
@ApplicationScoped
@Api(value = "/hero", consumes = "application/json")
public class HeroResource {
    private List<HeroDataModel> heroes = new ArrayList<>();
    
    public HeroResource() {
        HeroDataModel hero;
        for(int i=0; i<10;i++){
            hero = new HeroDataModel();
            hero.setId(i);
            hero.setName("Tigris-öüóőúűáéí");
            hero.setDescription("....");
            heroes.add(hero);
        }
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Hero by Id",
        notes = "Hero by Id",
        response = HeroDataModel.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Name of Hero not available")
    })
    public Response add(
            @ApiParam(value = "Name of Hero", required = true) @FormParam("name") String pName, 
            @ApiParam(value = "Description of Hero", required = true) @FormParam("desc") String pDesc){
        List<HeroDataModel> tmp = heroes.stream()
                .filter(hero -> hero.getName().equals(pName))
                .collect(Collectors.toList());
        
        if(tmp.isEmpty()) {
            HeroDataModel hero = new HeroDataModel();
            hero.setName(pName);
            hero.setDescription(pDesc);
            long maxID = Long.MIN_VALUE;
            for(HeroDataModel h: heroes){
                if(h.getId()>maxID)
                    maxID=h.getId();
            }
            maxID++;
            hero.setId(maxID);
            heroes.add(hero);
            return Response.ok(hero).build();
        }
        else {
            return Response.status(400).build();
        }                    
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all heroes",
        notes = "List of heroes",
        response = HeroDataModel.class,
        responseContainer = "List")    
    public List<HeroDataModel> getAll(){ return heroes;}
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Hero by Id",
        notes = "Hero by Id",
        response = HeroDataModel.class)
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Hero not found"),
        @ApiResponse(code = 500, message = "Data error, multiple ID"),
    })
    public Response getById(
            @ApiParam(value = "ID of Hero", required = true) @PathParam("id") @DefaultValue("0") long pId){
        List<HeroDataModel> tmp = heroes.stream()
                .filter(hero -> hero.getId() == pId)
                .collect(Collectors.toList());
        
        if(tmp.isEmpty()) {
            return Response.status(404).build();
        }
        else if(tmp.size()>1) {
            return Response.status(500).build();
        }
        else{
            return Response.ok(tmp.get(0)).build();
        }            
    }
    
    @GET
    @Path("/query")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Heroes by name",
        notes = "Heroes by name",
        response = HeroDataModel.class,
        responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Hero not found"),
    })
    public Response getByName(
            @ApiParam(value = "Name of Hero", required = true) @QueryParam("name") @DefaultValue("") String pName){
        List<HeroDataModel> tmp = heroes.stream()
                .filter(hero -> hero.getName().toLowerCase().contains(pName.toLowerCase()))
                .collect(Collectors.toList());
        
        if(tmp.isEmpty()) {
            return Response.status(404).build();
        }
        else {
            return Response.ok(tmp).build();
        }            
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Hero by Id",
        notes = "Hero by Id",
        response = HeroDataModel.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Name not available"),
        @ApiResponse(code = 404, message = "Hero not found"),
        @ApiResponse(code = 500, message = "Data error, multiple ID"),
    })
    public Response modifyById(
            @ApiParam(value = "ID of Hero", required = true) @PathParam("id") @DefaultValue("0") long pId,
            @ApiParam(value = "New name of Hero", required = true) @FormParam("name") String pName, 
            @ApiParam(value = "New description of Hero", required = true) @FormParam("desc") String pDesc){
        List<HeroDataModel> tmpIds = heroes.stream()
                .filter(hero -> hero.getId() == pId)
                .collect(Collectors.toList());
        
        if(tmpIds.isEmpty()) {
            return Response.status(404).build();
        }
        else if(tmpIds.size()>1) {
            return Response.status(500).build();
        }
        else{
            List<HeroDataModel> tmpName = heroes.stream()
                .filter(hero -> hero.getName().equals(pName))
                .collect(Collectors.toList());
            if(tmpName.isEmpty()){
                tmpIds.get(0).setName(pName);
                tmpIds.get(0).setDescription(pDesc);
                return Response.ok(tmpIds.get(0)).build();
            }
            else{
                return Response.status(400).build();
            }
            
        }            
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete Hero by Id",
        notes = "Deelte Hero by Id")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Hero not found"),
        @ApiResponse(code = 500, message = "Data error, multiple ID"),
    })
    public Response deleteById(
            @ApiParam(value = "ID of Hero", required = true) @PathParam("id") @DefaultValue("0") long pId){
        List<HeroDataModel> tmp = heroes.stream()
                .filter(hero -> hero.getId() == pId)
                .collect(Collectors.toList());
        
        if(tmp.isEmpty()) {
            return Response.status(404).build();
        }
        else if(tmp.size()>1) {
            return Response.status(500).build();
        }
        else{
            heroes.remove(tmp.get(0));
            return Response.ok(tmp.get(0)).build();
        }            
    }


    @GET
    @Path("/part")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Part of Heroes",
        notes = "part of heroes and ordering",
        response = PagerViewModel.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Not implemented sort property"),
        @ApiResponse(code = 401, message = "Initial index is too large"),
        @ApiResponse(code = 402, message = "Not implemented sort direction")
        
    })
    public Response getPart(
            @ApiParam(value = "Name of Sort propery ", required = true) @QueryParam("sort") @DefaultValue("name") String pSort,
            @ApiParam(value = "Sort direction ", required = true) @QueryParam("direction") @DefaultValue("ASC") String pDiection,
            @ApiParam(value = "Initial index", required = true) @QueryParam("start") @DefaultValue("0") int pStart,
            @ApiParam(value = "Count of elements", required = true) @QueryParam("count") @DefaultValue("0") int pCount){
        
        if(!"name".equals(pSort)) {return Response.status(400).build();}
        if(pStart>heroes.size()) {return Response.status(401).build();}
        if(!"ASC".equals(pDiection.toUpperCase()) && !"DESC".equals(pDiection.toUpperCase()) ) {
            return Response.status(402).build();
        }
        
        PagerViewModel res = new PagerViewModel(heroes.size(),pStart,pCount,null);
        List<HeroDataModel> copy = new ArrayList<>(heroes);
        if("ASC".equals(pSort.toUpperCase())){
            Collections.sort(copy, new HeroNameAscComparator());
        }
        else{
            Collections.sort(copy, new HeroNameDescComparator());
        }
        res.setData(copy.subList(pStart, ((pStart+pCount)>copy.size())?copy.size():(pStart+pCount)));
        
        return Response.ok(res).build();
    }
        
    
}
