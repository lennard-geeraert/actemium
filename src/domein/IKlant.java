package domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import domein.enums.StatusGebruiker;
import javafx.beans.property.StringProperty;

public interface IKlant 
{
	List<ContactPersoon> getContactPersonen();
	boolean isActief();
	String getName();
	long getId();
	ArrayList<Ticket> getTickets();
	LocalDate getAanmeldDatum();
	String getUserName();
	String getAdres();
	String getEmail();
	String getPassword();
	String getTelefoonummer();
	StatusGebruiker getStatus();
	StringProperty userName();
	StringProperty adres();
	StringProperty email();
	StringProperty telefoonummer();
	StringProperty password();
	StringProperty status();
	StringProperty name();
}