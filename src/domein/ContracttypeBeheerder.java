package domein;

import java.util.List;
import java.util.function.Predicate;

import domein.enums.ManierTicketAanmaak;
import domein.enums.StatusContracttype;
import domein.enums.TijdstippenAanmaakTicket;
import exceptions.EntityBestaalAlException;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.ContractypeDaoJpa;
import repository.GenericDaoJpa;

public class ContracttypeBeheerder 
{
	private ContractypeDaoJpa contracttypeDaoJpa;
	private ObservableList<IContracttype> contracttype;
	private FilteredList<IContracttype> contracttypeFiltered;
	
	public ContracttypeBeheerder()
	{
		this(new ContractypeDaoJpa());
	}
	
	public ContracttypeBeheerder(ContractypeDaoJpa contracttypeDao) 
	{
		this.contracttypeDaoJpa = contracttypeDao;
		this.contracttype = FXCollections.observableArrayList(contracttypeDao.findAll());
		this.contracttypeFiltered = new FilteredList<>(contracttype,p->true);
	}
	
	public ObservableList<IContracttype> getAlleContracttypes()
	{
		return	contracttype;
	}
	
	public void filterContract(Predicate<IContracttype> prec) {
		contracttypeFiltered.setPredicate(prec);
	}
	
	public FilteredList<IContracttype> getFilteredContract(){
		return this.contracttypeFiltered;
	}
	
	public List<Contracttype> getAlleContracttypesWithStatus(boolean isActief)
	{
		return	contracttypeDaoJpa.geefContracttypeMetStatus(isActief);
	}
	
	public Contracttype getContracttypesWithId(long id)
	{
		return	contracttypeDaoJpa.get(id);
	}
	
	public void voegContracttypeToe(Contracttype contracttype) throws VerplichtVeldenException, EntityBestaalAlException
	{
		try {
			GenericDaoJpa.startTransaction();
			if(contracttypeDaoJpa.bestaatContracttype(contracttype.getNaam())) {
				throw new EntityBestaalAlException("Contract kan niet aangemaakt worden omdat naam niet uniek is");
			}
			this.contracttype.add(contracttype);

			contracttypeDaoJpa.insert(contracttype);
		
			GenericDaoJpa.commitTransaction();
		}catch(EntityBestaalAlException e) {
			throw e;
		}
	}
	
	public void editContracttype(Contracttype type,String naam,ManierTicketAanmaak manier, TijdstippenAanmaakTicket tijdStippenMogelijkheid,
			String maximaleAfh,String minimaleAfh, String prijsContract,StatusContracttype isActief) throws VerplichtVeldenException, EntityBestaalAlException, FouteInvoerException 
	{
		try {
			GenericDaoJpa.startTransaction();
			if(type.heeftContracttypeLopendeContracten()) {
				type.editStatus(isActief);
			}else {
			if(!type.getNaam().equals(naam) && contracttypeDaoJpa.bestaatContracttype(naam)) {
				throw new EntityBestaalAlException("Contracttype kan niet gewijzigd worden omdat naam al in gebruik is");
			}
			type.editContract(naam, manier, tijdStippenMogelijkheid, maximaleAfh, minimaleAfh, prijsContract, isActief);
			}
			GenericDaoJpa.commitTransaction();
		}
		catch(VerplichtVeldenException v) {
			throw v;
		}
	}
	
	public void verwijderContracttype(Contracttype type) 
	{
		GenericDaoJpa.startTransaction();
		type.verwijderContracttype();
		GenericDaoJpa.commitTransaction();
	}		
}
