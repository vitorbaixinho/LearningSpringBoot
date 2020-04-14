package com.vbaixinho.learningspringboot.clientproxy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.vbaixinho.learningspringboot.model.User;

public interface UserResourceV1 {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> fetchUsers(@QueryParam("gender") String string);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{userUid}")
	public User fetchUser(@PathParam("userUid") UUID userUid);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertNewUser(User user);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateUser(User user);

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{userUid}")
	public void deleteUser(@PathParam("userUid") UUID userUid);
}
