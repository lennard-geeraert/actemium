package repository;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domein.Contracttype;

public class ContractypeDaoJpa extends GenericDaoJpa<Contracttype> implements ContracttypeDao 
{
	public ContractypeDaoJpa() 
	{
		super(Contracttype.class);
	}

	@Override
	public List<Contracttype> geefContracttypeMetStatus(boolean isActief) throws EntityNotFoundException 
	{
		try {
			return em.createNamedQuery("Contracttype.findOneWithStatus",Contracttype.class)
					.setParameter("isActief", isActief).getResultList();
		}catch(NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public boolean bestaatContracttype(String naam) throws EntityNotFoundException 
	{			
		try {
		Contracttype type = em.createNamedQuery("Contracttype.findOneWithNaam",Contracttype.class)
					.setParameter("naam",naam).getSingleResult();
		return true;
		}catch(NoResultException ex) {
			return false;
			
		}
	}
}
