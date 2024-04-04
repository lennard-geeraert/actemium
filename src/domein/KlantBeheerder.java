package domein;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import domein.enums.KlantFilter;
import domein.enums.StatusGebruiker;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDaoJpa;
import repository.KlantDaoJpa;

public class KlantBeheerder 
{	
	private KlantDaoJpa klantDao;
	private ObservableList<IKlant> klanten;
	private ObservableList<ContactPersoon> contactpersonen;
	private FilteredList<IKlant> filteredList;
	
	public KlantBeheerder()
	{
		this(new KlantDaoJpa());
	}
	
	public KlantBeheerder(KlantDaoJpa klantDao) 
	{
		this.klantDao = klantDao;
		this.klanten = FXCollections.observableArrayList(klantDao.findAll());
		this.filteredList = new FilteredList<>(klanten, p -> true);
	}
	
	public Klant geefKlantMetId(long id) 
	{
		return klantDao.get(id);
	}
	
	public Klant geefKlantMetNaam(String userName) 
	{
		Klant klant;
		try {
			if(userName == null || userName.isBlank()) {
				throw new EntityNotFoundException("Gebruikersnaam moet ingevuld zijn.");
			}
			klant = klantDao.geefKlantMetNaam(userName);
		}catch(EntityNotFoundException ex) {
			throw new IllegalArgumentException("Klant " + userName + " is niet gevonden");
		}
		return klant;
	}
	
	public ObservableList<IKlant> geefAlleKlanten()
	{
		return klanten;
	}
	
	public FilteredList<IKlant> getFilteredList()
	{
		return this.filteredList;
	}
	
	public void filterKlant(Predicate<IKlant> prek) 
	{
		System.out.println("filterklant");
		filteredList.setPredicate(prek);
		System.out.println(filteredList);
	}
	
	public ObservableList<Klant> geefAlleKlanten(KlantFilter kf)
	{
		return switch(kf)
		{						
		case active -> (ObservableList<Klant>) klantDao.findAll().stream().filter(Klant::isActief).collect(Collectors.toList());
		case nietActieve -> (ObservableList<Klant>) klantDao.findAll().stream().filter(klant -> klant.isActief()==false).collect(Collectors.toList());
		case alphabetisch ->(ObservableList<Klant>) klantDao.findAll().stream().sorted(Comparator.comparing(Klant::getUserName)).collect(Collectors.toList());
		default -> null;
		};
	}
	
	public void voegKlantToe(Klant klant) 
	{
		try {			
			GenericDaoJpa.startTransaction();
			klanten.add(klant);
			klantDao.insert(klant);
			GenericDaoJpa.commitTransaction();				
		} catch(Exception e) {
			throw new IllegalArgumentException("Het is niet gelukt om de klant toe te voegen");
		}
	}
	
	
	public void editKlant(Klant k, String name, String userName, String password, String email, String adres, StatusGebruiker status,String tel) throws VerplichtVeldenException, FouteInvoerException
	{
		try {
			GenericDaoJpa.startTransaction();
			k.editKlant(name, userName, password, email, adres, status,tel);
			GenericDaoJpa.commitTransaction();
		} catch (VerplichtVeldenException e) {
			throw e;
		}
	}
	
	public void editStatusKlant(Klant k, StatusGebruiker status)
	{
		GenericDaoJpa.startTransaction();
		k.setStatus(status);
		GenericDaoJpa.commitTransaction();
	}
	
	public ObservableList<ContactPersoon> geefContactPersonenVanKlant(Klant k)
	{
		return this.contactpersonen;
	}
	
	public void voegContactPersoonToe(ContactPersoon cp, Klant k)
	{
		//is genericdaojpa hier nodig???
		try {			
			GenericDaoJpa.startTransaction();
			this.contactpersonen.add(cp);
			k.addContactPersoon(cp);
			GenericDaoJpa.commitTransaction();				
		} catch(Exception e) {
			throw new IllegalArgumentException("Het is niet gelukt om de contactpersoon toe te voegen");
		}
	}
	
	public void voegCPToeAanBijnaToegevoegdeKlant(Klant k, ContactPersoon cp)
	{
		k.addContactPersoon(cp);
	}
	
	public void initialiseerContactpersonen(Klant k)
	{
		this.contactpersonen = FXCollections.observableArrayList(k.getContactPersonen());
	}
	
	public void verwijderContactPersoon(Klant k, String email) throws VerplichtVeldenException
	{
		try {			
			GenericDaoJpa.startTransaction();
			ContactPersoon contact = null;
			for (ContactPersoon c : contactpersonen) 
			{
				if(c.getEmail().equals(email))
					contact = c;
			}
			if(contactpersonen.size() <= 1) {
				throw new VerplichtVeldenException("Contactpersoon kan niet verwijderd worden, \nminstens 1 contactpersoon is verplicht");
			}
			k.verwijderContactPersoon(email);
			contactpersonen.remove(contact);
			GenericDaoJpa.commitTransaction();				
		} catch(VerplichtVeldenException e) {
			throw e;
		}
	}
}
