package domein;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import javax.persistence.EntityNotFoundException;

import domein.enums.RolWerknemer;
import domein.enums.StatusGebruiker;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDaoJpa;
import repository.WerknemerDaoJpa;

public class GebruikerBeheerder 
{
	private WerknemerDaoJpa werknemersDao;
	private ObservableList<IWerknemer> werknemers;
	private FilteredList<IWerknemer> werknemersFilterList;
	
	public GebruikerBeheerder(WerknemerDaoJpa wdj) 
	{
		werknemersDao = wdj;
		this.werknemers = FXCollections.observableArrayList(werknemersDao.findAll());
		this.werknemersFilterList = new FilteredList<>(werknemers,p ->true);
	}
	
	public void voegWerknemerToe(Werknemer werknemer) 
	{
		try {			
			GenericDaoJpa.startTransaction();
			werknemers.add(werknemer);
			werknemersDao.insert(werknemer);
			GenericDaoJpa.commitTransaction();				
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ObservableList<IWerknemer> getAlleWerknemers()
	{
		return werknemers;
	}
	
	public FilteredList<IWerknemer> getFilteredWerknemerList(){
		return this.werknemersFilterList;
	}
	
	public void filterWerknemer(Predicate<IWerknemer> prew) {
		werknemersFilterList.setPredicate(prew);
	}
	
	public Werknemer geefWerknemerById(long id) 
	{
		Werknemer w;
		
		if(werknemersDao.exists(id)) {
			w = werknemersDao.get(id);
			return w;
		}
		return null;
	}
	
	public void editWerknemer(Werknemer w, String firstName, String lastName, String password, RolWerknemer rol, String userName, 
			String adres, String email, StatusGebruiker status,String tel) throws VerplichtVeldenException, FouteInvoerException 
	{
		try {
			GenericDaoJpa.startTransaction();
			w.editWerknemer(firstName, lastName, password, rol, userName, adres, email, status,tel);
			GenericDaoJpa.commitTransaction();
			//Als ge het catcht waarom nog eens trowen?
		}catch(VerplichtVeldenException f) {
			throw f;
		}
	}
	
	public void verwijderWerknemer(Werknemer w) 
	{
		w.setStatus(StatusGebruiker.Geblokkeerd);
	}
	
	public IWerknemer meldAan1(String userName, String password)
	{
		Werknemer w;
		try {
			w = werknemersDao.geefWerknemerMetNaam(userName);
			if(w.getAantalPogingen() >= 5 || w.getStatus() == StatusGebruiker.Geblokkeerd) {
				registreerAanmeldpoging(LocalDate.now(), userName, false);
				w.setStatus(StatusGebruiker.Geblokkeerd);
				w.setAantalPogingen(0);
				throw new NoSuchElementException(userName + " heeft al meer dan 5 keer geprobeerd aan te melden en wordt tijdelijk geblokkeerd");
			}
			if(!w.getPassword().equals(password)) {
				registreerAanmeldpoging(LocalDate.now(), userName, false);
				throw new IllegalArgumentException("De gebruikersnaam of wachtwoord is onjuist");
			}
			registreerAanmeldpoging(LocalDate.now(), userName, true);
			return w;
			
		} catch(EntityNotFoundException ex) {
			throw new IllegalArgumentException("De gebruikersnaam of wachtwoord is onjuist");
		}
	}
	
	private void registreerAanmeldpoging(LocalDate date, String userName, boolean gelukt)
	{
		Werknemer w = werknemersDao.geefWerknemerMetNaam(userName);
		GenericDaoJpa.startTransaction();
		w.setAanmeldDatum(date);
		w.setAanmeldenGelukt(gelukt);
		if(!gelukt) {
			w.setAantalPogingen(w.getAantalPogingen() + 1);
		}
		GenericDaoJpa.commitTransaction();
	}
	
	public StatusGebruiker geefStatusWerknemer(String userName)
	{
		Werknemer w = werknemersDao.geefWerknemerMetNaam(userName);
		if(w == null)
			return null;
		return w.getStatus();
	}
	
	public Werknemer geefWerknemerMetNaam(String userName)
	{
		return werknemersDao.geefWerknemerMetNaam(userName);
	}
}
