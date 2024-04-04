package repository;

import javax.persistence.EntityNotFoundException;

import domein.IKlant;
import domein.Klant;

public interface KlantDao  extends GenericDao<Klant>
{
	public IKlant geefKlantMetNaam(String userName) throws EntityNotFoundException;
}
