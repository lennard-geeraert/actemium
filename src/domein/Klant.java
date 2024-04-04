package domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import domein.enums.StatusContract;
import domein.enums.StatusGebruiker;
import exceptions.FouteInvoerException;
import exceptions.VerplichtVeldenException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "Klant")
@NamedQueries({@NamedQuery(name = "Klant.findAll", query = "SELECT d FROM Klant d"), 
	@NamedQuery(name = "Klant.findOne", query = "SELECT k FROM Klant k WHERE k.userName = :userName")})
@Access(AccessType.FIELD)
public class Klant extends Gebruiker implements IKlant
{	
	private boolean isActief;
	@Transient
	private StringProperty name;
	@OneToMany(fetch =  FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<ContactPersoon> contactPersoon;
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private ArrayList<Ticket> tickets;
	@OneToMany(fetch =  FetchType.EAGER, cascade = CascadeType.PERSIST)
	private ArrayList<Contract> contracten;
	private LocalDate aanmeldDatum;
	
	public Klant(String name, String userName, String password, String email, String adres, LocalDate date,String tel) 
	{
		super(password, date, userName, email, adres,tel);
		this.name = new SimpleStringProperty();
		setName(name);
		setActief(true);
		this.contactPersoon = new ArrayList<ContactPersoon>();
		this.tickets = new ArrayList<Ticket>();
		this.contracten = new ArrayList<Contract>();
	}
	
	public Klant(Builder builder) 
	{
		super(builder.password, builder.datumRegistratie, builder.userName, builder.email, builder.adres,builder.telefoonnummers);
		this.name = new SimpleStringProperty();
		setName(builder.naam);
		setContactPersonen(builder.contactPersonen);
		setActief(true);
		setStatus(StatusGebruiker.Actief);
		this.tickets = new ArrayList<Ticket>();
		this.contactPersoon = new ArrayList<ContactPersoon>();
		this.contracten = new ArrayList<Contract>();
	}
	
	protected Klant() 
	{
		this.name = new SimpleStringProperty();
	}
	
	public void addContactPersoon(ContactPersoon c) 
	{
		if(c==null) {
			throw new IllegalArgumentException();
		}
		this.contactPersoon.add(c);
	}
	
	public void addContract(Contract c) 
	{
		if(c==null) {
			throw new IllegalArgumentException();
		}
		this.contracten.add(c);
	}
	
	@Override
	public List<ContactPersoon> getContactPersonen() 
	{
		return contactPersoon;
	}

	public void setContactPersonen(List<ContactPersoon> contactPersonen) 
	{
		this.contactPersoon = contactPersonen;
	}
	
	public void addTicket(Ticket t)
	{
		if(t==null) {
			throw new IllegalArgumentException();
		}
		tickets.add(t);
	}

	@Override
	public boolean isActief() 
	{
		return isActief;
	}

	private void setActief(boolean isActief) 
	{
		this.isActief = isActief;
	}

	@Override
	@Access(AccessType.PROPERTY)
	public String getName() 
	{
		return name.get();
	}

	public void setName(String name)
	{
		if(name==null || name.isBlank()) {
			throw new IllegalArgumentException();
		}		
		this.name.set(name);
	}
	
	@Override
	public StringProperty name() 
	{
		return this.name;
	}
	
	@Override
	public ArrayList<Ticket> getTickets() 
	{
		return tickets;
	}

	public void setTickets(ArrayList<Ticket> tickets) 
	{
		this.tickets = tickets;
	}
	
	@Override
	public LocalDate getAanmeldDatum() 
	{
		return null;
	}
	
	public void setAanmeldDatum(LocalDate aanmeldDatum) 
	{
		this.aanmeldDatum = aanmeldDatum;
	}
	
	public static class Builder 
	{
		private String userName;
		private String password;
		private StatusGebruiker status;
		private String adres;
		private LocalDate datumRegistratie;
		private String email;
		private String telefoonnummers ;
		private String naam;
		private boolean actief;
		private List<ContactPersoon> contactPersonen;
		
		public Builder userName(String userName) {
			this.userName = userName;
			return this;
		}
		
		public Builder telefoonnummers(String telefoonnummers) {
			this.telefoonnummers = telefoonnummers;
			return this;
		}
		
		public Builder contactPersonen(List<ContactPersoon> contactPersonen) {
			this.contactPersonen = contactPersonen;
			return this;
		}
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder status(StatusGebruiker status) {
			this.status = status;
			return this;
		}
		
		public Builder adres(String adres) {
			this.adres = adres;
			return this;
		}
		public Builder datumReg(LocalDate date) {
			this.datumRegistratie = date;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public StatusGebruiker getStatus() {
			return status;
		}

		public void setStatus(StatusGebruiker status) {
			this.status = status;
		}

		public String getAdres() {
			return adres;
		}

		public void setAdres(String adres) {
			this.adres = adres;
		}

		public LocalDate getDatumRegistratie() {
			return datumRegistratie;
		}

		public void setDatumRegistratie(LocalDate datumRegistratie) {
			this.datumRegistratie = datumRegistratie;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getTelefoonnummers() {
			return telefoonnummers;
		}

		public void setTelefoonnummers(String telefoonnummers) {
			this.telefoonnummers = telefoonnummers;
		}

		public String getNaam() {
			return naam;
		}

		public void setNaam(String naam) {
			this.naam = naam;
		}

		public boolean isActief() {
			return actief;
		}

		public void setActief(boolean actief) {
			this.actief = actief;
		}

		public Builder naam(String naam) {
			this.naam = naam;
			return this;
		}
		
		public Builder firstname(boolean actief) {
			this.actief =actief;
			return this;
		}
		
		public Klant build() throws VerplichtVeldenException, FouteInvoerException 
		{
			List<String> verplichteAttributen = new ArrayList<String>();
			List<String> fouteInvoerAttributen = new ArrayList<String>();
			Klant k = null;
			if(naam == null || naam.isBlank() ) {
				verplichteAttributen.add("naam");
			}
			if(adres == null || adres.isBlank()) {
				verplichteAttributen.add("adres");
			}
			if(telefoonnummers == null || telefoonnummers.isBlank()) {
				verplichteAttributen.add("telefoonnummer");
			}
			if(!telefoonnummers.matches("\\d{4}/\\d{6}")) {
				fouteInvoerAttributen.add("telefoonnummer (vb: 0482/041472)");
			}
			if(userName == null || userName.isEmpty()) {
				verplichteAttributen.add("gebruikersnaam ");
			}
			if(email == null || email.isBlank()) {
				verplichteAttributen.add("email");
			}
			if(password == null || password.isBlank()) {
				verplichteAttributen.add("wachtwoord");
			}
			if(password.length() < 6) {
				fouteInvoerAttributen.add("wachtwoord (minstens 6 karakters)");
			}
			if(!isValid(email)) {
				fouteInvoerAttributen.add("email (vb: jan@gmail.com)");
			}
			if(!verplichteAttributen.isEmpty()) {
				throw new VerplichtVeldenException("Een nieuwe klant kan niet aangemaakt worden omdat volgende velden verplicht zijn: ",verplichteAttributen);
			}
			if(!fouteInvoerAttributen.isEmpty()) {
				throw new FouteInvoerException("Een nieuwe klant kan niet aangemaakt worden omdat volgende velden fout zijn: ", fouteInvoerAttributen);
			}
		 k = new Klant(this);
			return k;
		}
	}

	public void editKlant(String name, String userName, String password, String email, String adres, StatusGebruiker status,String telefoonnummer) throws VerplichtVeldenException, FouteInvoerException
	{
		List<String> verplichteAttributen = new ArrayList<String>();
		List<String> fouteInvoerAttributen = new ArrayList<String>();
	
		if(name == null || name.isBlank()) {
			verplichteAttributen.add("naam");
		}
		if(adres == null || adres.isBlank()) {
			verplichteAttributen.add("adres");
		}
		if(userName == null || userName.isEmpty() ) {
			verplichteAttributen.add("gebruikersnaam");
		}
		if(telefoonnummer == null || telefoonnummer.isBlank()) {
			verplichteAttributen.add("telefoonnummer");
		}
		if(!telefoonnummer.matches("\\d{4}/\\d{6}")) {
			fouteInvoerAttributen.add("telefoonnummer (vb: 0482/041472)");
		}
		if(email == null || email.isBlank()) {
			verplichteAttributen.add("email");
		}
		if(!isValid(email)) {
			fouteInvoerAttributen.add("email (vb: jan@gmail.com)");
		}
		if(contactPersoon == null || contactPersoon.isEmpty()) {
			verplichteAttributen.add("contactpersoon");
		}
		if(password == null || password.isBlank()) {
			verplichteAttributen.add("wachtwoord");
		}
		if(password.length() < 6) {
			fouteInvoerAttributen.add("wachtwoord (minstens 6 karakters)");
		}
		if(!verplichteAttributen.isEmpty()) {
			throw new VerplichtVeldenException("Een  klant kan niet gewijzigd worden omdat volgende gegevens verplicht zijn: ",verplichteAttributen);
		}
		if(!fouteInvoerAttributen.isEmpty()) {
			throw new FouteInvoerException("Een nieuwe klant kan niet aangemaakt worden omdat volgende velden fout zijn: ", fouteInvoerAttributen);
		}
		setName(name);
		setTelefoonummer(telefoonnummer);
		setUserName(userName);
		setPassword(password);
		setEmail(email);
		setAdres(adres);
		setStatus(status);
	}
	
	public void verwijderContactPersoon(String email)
	{
		if(email==null || email.isBlank()) {
			throw new IllegalArgumentException("Email is leeg");
		}
		if(!isValid(email)) {
			throw new IllegalArgumentException("Email is niet geldig");
		}
		
		ContactPersoon contact = null;
		for (ContactPersoon c : contactPersoon) 
		{
			if(c.getEmail().equals(email))
				contact = c;
		}
		
		if(contact == null) {
			throw new NullPointerException("Contact kan niet verwijderd worden want deze bestaat niet.");
		}
		contactPersoon.remove(contact);
	}
	
	static boolean isValid(String email) 
	{
		   String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		   return email.matches(regex);
	}
	
	public boolean heeftKlantActiefContract() 
	{
		for (Contract c : contracten) {
			if(c.getStatusContract().equals(StatusContract.Lopend)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Contract> getContracten() 
	{
		return this.contracten;
	}
}
