package repository;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domein.Werknemer;

public class WerknemerDaoJpa extends GenericDaoJpa<Werknemer> implements WerknemerDao
{
	public WerknemerDaoJpa() 
	{
		super(Werknemer.class);
	}

	@Override
	public Werknemer geefWerknemerMetNaam(String userName) throws EntityNotFoundException
	{
		try {
			return em.createNamedQuery("Werknemer.findOne", Werknemer.class)
					.setParameter("userName", userName)
					.getSingleResult();
		} catch(NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public List<Werknemer> getAlleTechniekersInList() throws EntityNotFoundException 
	{
		try {
			return em.createNamedQuery("Werknemer.findTechniekers", Werknemer.class)
					.getResultList();
		} catch(NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
}
