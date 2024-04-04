package repository;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domein.Klant;

public class KlantDaoJpa extends GenericDaoJpa<Klant> implements KlantDao  
{
	public KlantDaoJpa() 
	{
		super(Klant.class);	
	}

	@Override
	public Klant geefKlantMetNaam(String userName) throws EntityNotFoundException 
	{
		try {
			return em.createNamedQuery("Klant.findOne", Klant.class)
					.setParameter("userName", userName)
					.getSingleResult();
		} catch(NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
}
