package repository;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import domein.Contracttype;

public interface ContracttypeDao extends GenericDao<Contracttype> 
{
	public List<Contracttype> geefContracttypeMetStatus(boolean isActief) throws EntityNotFoundException;
	public boolean bestaatContracttype(String naam) throws EntityNotFoundException;
}
