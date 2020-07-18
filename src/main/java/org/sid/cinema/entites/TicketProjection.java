package org.sid.cinema.entites;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="ticketProj",types={org.sid.cinema.entites.Ticket.class})
public interface TicketProjection {
	
	public Long getId();	
	public String getNomClient();
	public double getPrix();
	public Integer getCodePayment();
	public boolean getReserve();
	public Place getplace();
	
}
