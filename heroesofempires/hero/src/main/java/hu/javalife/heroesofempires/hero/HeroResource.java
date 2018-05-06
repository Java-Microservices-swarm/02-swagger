package hu.javalife.heroesofempires.hero;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
public class HeroResource {
    private List<Hero> heroes = new ArrayList<>();
    
    public HeroResource() {
        Hero hero;
        for(int i=0; i<10;i++){
            hero = new Hero();
            hero.setId(i);
            hero.setName("Tigris-öüóőúűáéí");
            hero.setDescription("....");
            heroes.add(hero);
        }
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(
            @FormParam("name") String pName, 
            @FormParam("desc") String pDesc){
        List<Hero> tmp = heroes.stream()
                .filter(hero -> hero.getName().equals(pName))
                .collect(Collectors.toList());
        
        if(tmp.isEmpty()) {
            Hero hero = new Hero();
            hero.setName(pName);
            hero.setDescription(pDesc);
            long maxID = Long.MIN_VALUE;
            for(Hero h: heroes){
                if(h.getId()>maxID)
                    maxID=h.getId();
            }
            maxID++;
            hero.setId(maxID);
            heroes.add(hero);
            return Response.ok(hero).build();
        }
        else {
            return Response.status(500).build();
        }            
        
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Hero> getAll(){ return heroes;}
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") @DefaultValue("0") long pId){
        List<Hero> tmp = heroes.stream()
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
    public Response getByName(@QueryParam("name") @DefaultValue("") String pName){
        List<Hero> tmp = heroes.stream()
                .filter(hero -> hero.getName().toLowerCase().contains(pName.toLowerCase()))
                .collect(Collectors.toList());
        
        if(tmp.isEmpty()) {
            return Response.status(404).build();
        }
        else {
            return Response.ok(tmp).build();
        }            
    }
    
}
