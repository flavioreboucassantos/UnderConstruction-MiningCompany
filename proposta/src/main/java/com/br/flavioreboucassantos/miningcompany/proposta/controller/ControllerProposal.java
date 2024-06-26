package com.br.flavioreboucassantos.miningcompany.proposta.controller;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.miningcompany.proposta.service.ServiceProposal;
import com.br.flavioreboucassantos.miningcompany.sharedlibrary_all.dto.DtoProposalDetails;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/proposal")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public final class ControllerProposal {

	private final Logger LOG = LoggerFactory.getLogger(ControllerProposal.class);

	private final ServiceProposal serviceProposal;
	private final JsonWebToken jsonWebToken;

	@Inject
	public ControllerProposal(final ServiceProposal serviceProposal, final JsonWebToken jsonWebToken) {
		this.serviceProposal = serviceProposal;
		this.jsonWebToken = jsonWebToken;
	}

	@GET
	@Path("/{id}")
	@RolesAllowed({"user", "manager"})
	public Response findProposalDetails(final @PathParam("id") long idProposal) {
		DtoProposalDetails dtoProposalDetails;
		if ((dtoProposalDetails = serviceProposal.tryFindDtoProposalDetails(idProposal)) == null)
			return serviceProposal.disappointedFind().entity("findProposalDetails-id-NOT_FOUND").build();

		return Response.ok(dtoProposalDetails).build();
	}

	@POST
	@RolesAllowed("proposal-customer")
	public Response createProposal(final DtoProposalDetails dtoProposalDetails) {

		LOG.info("--- Recebendo Proposta de Compra ---");

		serviceProposal.createProposal(dtoProposalDetails);

		return Response.ok().build();
	}

	@DELETE
	@Path("/{id}")
	@RolesAllowed("manager")
	public Response removeProposal(final @PathParam("id") long idProposal) {

		LOG.info("--- Removendo Proposta de Compra ---");

		serviceProposal.removeProposal(idProposal);

		return Response.ok().build();
	}

}
