package controllers;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import domein.Contracttype;
import domein.ContracttypeBeheerder;
import domein.IContracttype;
import domein.ITicket;
import domein.IWerknemer;
import domein.TicketBeheer;
import domein.enums.ManierTicketAanmaak;
import domein.enums.StatusContracttype;
import domein.enums.TijdstippenAanmaakTicket;
import exceptions.EntityBestaalAlException;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class SupportManagerController implements Controller 
{
	private IWerknemer w;
	private ContracttypeBeheerder contracttypeBeheerder; 
	private TicketBeheer ticketBeheerder;
	private Contracttype geselecteerdeContracttype;
	private IContracttype cType;
	private FilteredList<ITicket> filteredListTickets;
	
	public SupportManagerController(ContracttypeBeheerder contracttypeBeheer,TicketBeheer ticketBeheerder) 
	{
		this.contracttypeBeheerder = contracttypeBeheer; 
		this.ticketBeheerder =  ticketBeheerder;
		setFilteredList();
	}
	
	public void setFilteredList() 
	{
		this.filteredListTickets = new FilteredList<>(ticketBeheerder.geefAlleTickets(),p->true);
	}
	
	public FilteredList<ITicket> geefFilteredListVanTickets()
	{
		return filteredListTickets;	
	}
	public void filterTickets(Predicate<ITicket> filterPredicate)
	{
		filteredListTickets.setPredicate(filterPredicate);
	}
	
	public ObservableList<ManierTicketAanmaak> geefManierTicketAanamak()
	{
		return FXCollections.observableArrayList(ManierTicketAanmaak.Email,ManierTicketAanmaak.Telefonisch,ManierTicketAanmaak.Applicatie);
	}
	
	public ObservableList<StatusContracttype> geefStatussenContracttype()
	{
		return FXCollections.observableArrayList(StatusContracttype.Actief,StatusContracttype.NietActief);
	}
	
	public ObservableList<TijdstippenAanmaakTicket> geefTijdstipMogelijkheid()
	{
		return FXCollections.observableArrayList(TijdstippenAanmaakTicket.Altijd24op7,TijdstippenAanmaakTicket.Van8tot17OpWerkdagen);
	}
	
	public Contracttype getContracttypesWithId(long id)
	{
		return	contracttypeBeheerder.getContracttypesWithId(id);
	}
	
	public void initializeerTickets()
	{
		ticketBeheerder.initializeerTickets();
	}
	
	public List<String> geefTicketPerMaand() 
	{
		return ticketBeheerder.geefTicketGesoorteerdOpMaand().entrySet().stream().map(el ->el.getKey()+"="+el.getValue()).collect(Collectors.toList());
	}
	
	public List<String> geefTicketPerStatus()
	{
		return ticketBeheerder.geefTicketOpStatus().entrySet().stream().map(el->el.getKey()+"="+el.getValue()).collect(Collectors.toList());
	}
	
	public void voegContracttypeToe(String naam,ManierTicketAanmaak manier, TijdstippenAanmaakTicket tijdStippenMogelijkheid,
			String maximaleAfh, String  minimaleAfh, String  prijsContract)  throws VerplichtVeldenException, EntityBestaalAlException, FouteInvoerException{
		
		Contracttype contracttype = new Contracttype.Builder().naam(naam)
				.manierTicketAanmaak(manier)
				.tijdstippenMogelijkheid(tijdStippenMogelijkheid).maximaleAfhandeltijd(maximaleAfh)
				.minimaleAfhandeltijd(minimaleAfh).prijsContract(prijsContract)
				.build();
		contracttypeBeheerder.voegContracttypeToe(contracttype);
	}
	
	public ObservableList<IContracttype> geefAlleContracttypes()
	{
		return (ObservableList<IContracttype>) (Object) contracttypeBeheerder.getAlleContracttypes();
	}
	
	public void filterContract(Predicate<IContracttype> prec) {
		this.contracttypeBeheerder.filterContract(prec);
	}
	
	public FilteredList<IContracttype> geefFilteredContract(){
		return contracttypeBeheerder.getFilteredContract();
	}
	
	public void editContracttype(Contracttype type,String naam,ManierTicketAanmaak manier, TijdstippenAanmaakTicket tijdStippenMogelijkheid,
			String maximaleAfh, String minimaleAfh, String prijsContract,StatusContracttype isActief) throws VerplichtVeldenException, EntityBestaalAlException, FouteInvoerException 
	{		
			contracttypeBeheerder.editContracttype(type, naam, manier, tijdStippenMogelijkheid, maximaleAfh, minimaleAfh, prijsContract, isActief);
	}
	
	public void verwijderContracttype() 
	{
		contracttypeBeheerder.verwijderContracttype(geselecteerdeContracttype);
	}
	
	public List<Contracttype> getAlleContracttypesWithStatus(boolean isActief)
	{
		return contracttypeBeheerder.getAlleContracttypesWithStatus(isActief);
	}

	@Override
	public void setWerknemer(IWerknemer w) 
	{
		this.w = w;	
	}

	@Override
	public IWerknemer getWerknemer() 
	{
		return w;
	}

	public IContracttype getcType() 
	{
		return cType;
	}

	public void setcType(IContracttype cType) 
	{
		this.cType = cType;
	}

	public Contracttype getGeselecteerdeContracttype()
	{
		return geselecteerdeContracttype;
	}
	
	public void setGeselecteerdeContracttype(Contracttype geselecteerdeContracttype) 
	{
		this.geselecteerdeContracttype = geselecteerdeContracttype;
	}
	
	public Long getAantalLopendeContracten() 
	{
		return this.geselecteerdeContracttype.getLopendeContracten();
	}
	
	public Long getAantalBehandeldeTickets() 
	{
		return this.geselecteerdeContracttype.getAantalBehandeldeTickets();
	}
}
