package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GenericDaoJpa<T> implements GenericDao<T>
{
	private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("actemium");
	protected static final EntityManager em = emf.createEntityManager();
	
	private final Class<T> type;
	
	public GenericDaoJpa(Class<T> type) 
	{
		this.type = type;
	}
	
	public static void closePersistency() 
	{
		em.close();
		emf.close();
	}
	
	public static void startTransaction() 
	{
		if(!em.getTransaction().isActive()) {
		em.getTransaction().begin();
		}
	}
	
	public static void commitTransaction() 
	{
		em.getTransaction().commit();
	}
	
	public static void rollbackTransaction() 
	{
		em.getTransaction().rollback();
	}
	
	@Override
	public List<T> findAll() 
	{
		return  em.createNamedQuery(type.getSimpleName() + ".findAll", type).getResultList();
	}

	@Override
	public T get(Long id) 
	{
		T entity = em.find(type, id);
		return entity;
	}

	@Override
	public T update(T object) 
	{
		return em.merge(object);
	}

	@Override
	public void delete(T object) 
	{
		em.remove(em.merge(object));
	}

	@Override
	public void insert(T object) 
	{
		em.persist(object);
	}

	@Override
	public boolean exists(Long id) 
	{
		T entity = em.find(type, id);
		return entity != null;
	}
}
