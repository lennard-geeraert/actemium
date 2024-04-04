package repository;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domein.Ticket;
import domein.Werknemer;

public class TicketDaoJpa extends GenericDaoJpa<Ticket> implements TicketDao 
{
	public TicketDaoJpa() 
	{
		super(Ticket.class);
	}

	@Override
	public List<Ticket> geefTicketsVanWerknemer(Werknemer w) throws EntityNotFoundException 
	{
		try {
			return em.createNamedQuery("Ticket.findAllFromEmployee", Ticket.class)
					.setParameter("toegewezenTechnieker", w.getUserName())
					.getResultList();
		} catch(NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}

}
