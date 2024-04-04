package repository;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import domein.Ticket;
import domein.Werknemer;

public interface TicketDao extends GenericDao<Ticket>
{
	public List<Ticket> geefTicketsVanWerknemer(Werknemer w) throws EntityNotFoundException;
}
