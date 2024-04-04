package domein;

import java.time.LocalDate;
import java.util.List;

import domein.enums.RolWerknemer;
import domein.enums.StatusGebruiker;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public interface IWerknemer 
{
	ObservableList<Ticket> getTickets();
	void voegTicketToe(Ticket t);
	int getAantalPogingen();
	boolean isAanmeldenGelukt();
	LocalDate getAanmeldDatum();
	RolWerknemer getRol();
	String getFirstName();
	String getLastName();
	long getId();
	LocalDate getDatumRegistratie();
	String getUserName();
	String getAdres();
	String getEmail();
	String getPassword();
	StatusGebruiker getStatus();
	String getTelefoonummer();
	List<Ticket> geefAfgehandeldeTickets();
	StringProperty userName();
	StringProperty adres();
	StringProperty email();
	StringProperty telefoonummer();
	StringProperty password();
	StringProperty status();
	StringProperty lastName();
	StringProperty firstName();
	StringProperty rol();
}