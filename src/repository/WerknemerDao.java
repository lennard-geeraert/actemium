package repository;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import domein.IWerknemer;
import domein.Werknemer;

public interface WerknemerDao extends GenericDao<Werknemer>
{
	public IWerknemer geefWerknemerMetNaam(String userName) throws EntityNotFoundException;
	public List<Werknemer> getAlleTechniekersInList() throws EntityNotFoundException;
}
