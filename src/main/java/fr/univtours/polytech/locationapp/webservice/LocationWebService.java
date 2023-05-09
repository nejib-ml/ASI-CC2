package fr.univtours.polytech.locationapp.webservice;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.univtours.polytech.locationapp.business.LocationBusinessLocal;
import fr.univtours.polytech.locationapp.model.LocationBean;

@Path("v1")
public class LocationWebService {
	@Inject
	private LocationBusinessLocal business;

	@GET
	@Path("locations/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public LocationBean getLocation(@PathParam("id") String id) {
		return business.getLocation(Integer.valueOf(id));
	}

	@GET
	@Path("locations")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<LocationBean> getAllLocations(@QueryParam("filtre") String city, @QueryParam("tri") String sortOrder) {
		List<LocationBean> allLocations = business.getLocations();
		List<LocationBean> filteredAndSortedLocations = business.filterAndSortLocations(allLocations, city, sortOrder);
		return filteredAndSortedLocations;
	}

	@DELETE
	@Path("locations/{id}")
	public Response deleteLocation(@PathParam("id") String id,
			@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization) {
		if (authorization == null) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (!authorization.equals("42")) {
			return Response.status(Status.FORBIDDEN).build();
		}

	    LocationBean locationBean = business.getLocation(Integer.valueOf(id));

	    if (locationBean == null) {
	        return Response.status(Status.NOT_FOUND).build();
	    }

	    business.deleteLocation(Integer.valueOf(id));
	    return Response.ok().build();
	}
	
	@POST
	@Path("locations")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response createLocation(LocationBean locationBean, @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization) {
		if (authorization == null) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (!authorization.equals("42")) {
			return Response.status(Status.FORBIDDEN).build();
		}

		business.addLocation(locationBean);
		//LocationBean locationBean = business.getLocation(Integer.valueOf(id));
		
	    return Response.ok().build();
	}
	
	@POST
	@Path("locations")
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	public Response createLocation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @FormParam("address") String address,
	        @FormParam("city") String city,
	        @FormParam("nightPrice") Double price,
	        @FormParam("zipCode") Integer zipCode) {
		
		if (authorization == null) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (!authorization.equals("42")) {
			return Response.status(Status.FORBIDDEN).build();
		}

		LocationBean locationBean = new LocationBean();
		locationBean.setAddress(address);
		locationBean.setCity(city);
		locationBean.setNightPrice(price);
		locationBean.setZipCode(String.valueOf(zipCode));
		
		business.addLocation(locationBean);
		//LocationBean locationBean = business.getLocation(Integer.valueOf(id));
		
	    return Response.ok().build();
	}

	@PUT
	@Path("locations")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response editLocation(LocationBean newLocationBean, @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization) {
		if (authorization == null) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (!authorization.equals("42")) {
			return Response.status(Status.FORBIDDEN).build();
		}

	    if (newLocationBean.getId() == null) {
	        return Response.status(Status.NOT_FOUND).build();
	    }
	    
		LocationBean oldLocation = business.getLocation(newLocationBean.getId());
	    if (oldLocation == null) {
	        return Response.status(Status.NOT_FOUND).build();
	    }
	    
	    if(!("".equals(String.valueOf(newLocationBean.getNightPrice()))) && !(" ".equals(String.valueOf(newLocationBean.getNightPrice()))) && !(newLocationBean.getNightPrice()==null)) {
	    	oldLocation.setNightPrice(newLocationBean.getNightPrice());
	    }
	    
	    if(!("".equals(newLocationBean.getAddress())) && !(" ".equals(newLocationBean.getAddress())) && !(newLocationBean.getAddress()==null)) {
	    	oldLocation.setAddress(newLocationBean.getAddress());
	    }
	    
	    if(!("".equals(newLocationBean.getCity())) && !(" ".equals(newLocationBean.getCity())) && !(newLocationBean.getCity()==null)) {
	    	oldLocation.setCity(newLocationBean.getCity());
	    }
	    
	    if(!("".equals(newLocationBean.getZipCode())) && !(" ".equals(newLocationBean.getZipCode())) && !(newLocationBean.getZipCode()==null)) {
	    	oldLocation.setZipCode(newLocationBean.getZipCode());
	    }
	    
	    if(!("".equals(newLocationBean.getPicture())) && !(" ".equals(newLocationBean.getPicture())) && !(newLocationBean.getPicture()==null)) {
	    	oldLocation.setPicture(newLocationBean.getPicture());
	    }
		
	    business.updateLocation(oldLocation);
	    
	    return Response.ok().build();
	}
}
