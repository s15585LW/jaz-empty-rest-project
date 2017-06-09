package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.Car;
import domain.People;
import domain.Person;
import domain.services.PersonService;

@Path("/people")
@Stateless
public class PeopleResources {

	private PersonService db = new PersonService();
	
	@PersistenceContext
	EntityManager em;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		List<Person> result = em.createNamedQuery("person.all", Person.class).getResultList();
		
		People entity = new People();
		entity.setPeople(result);
		return Response.ok(entity).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response Add(Person person) {
		em.persist(person);
		return Response.ok(person.getId()).build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") int id){
		Person result = em.createNamedQuery("person.id", Person.class).setParameter("personId", id).getSingleResult();
		if(result==null){
			return Response.status(404).build();
		}
		return Response.ok(result).build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, Person p){
		Person result = em.createNamedQuery("person.id", Person.class).setParameter("personId", id).getSingleResult();
		if(result==null){
			return Response.status(404).build();
		}
		result.setName(p.getName());
		result.setSurname(p.getSurname());
		em.persist(result);
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") int id){
		Person result = em.createNamedQuery("person.id", Person.class).setParameter("personId", id).getSingleResult();
		if(result==null){
			return Response.status(404).build();
		}
		db.update(result);
		return Response.ok().build();
	}
	
	@GET
	@Path("/{personId}/cars")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Car> getCars(@PathParam("personId") int personId){
		Person result = em.createNamedQuery("person.id", Person.class).setParameter("personId", personId).getSingleResult();
		if(result==null)
			return null;
		return result.getCars();
	}
	
	@POST
	@Path("/{id}/cars")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCar(@PathParam("id") int personId, Car car){
		Person result = em.createNamedQuery("person.id", Person.class).setParameter("personId", personId).getSingleResult();
		if(result==null)
			return Response.status(404).build();
		result.getCars().add(car);
		car.setPerson(result);
		em.persist(car);
		return Response.ok().build();
	}
	
}
