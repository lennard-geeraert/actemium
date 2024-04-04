package domein;

import java.time.LocalDate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import domein.enums.StatusGebruiker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class Gebruiker 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private LocalDate datumRegistratie;
	@Transient
	private StringProperty userName;
	@Transient
	private StringProperty password;
	@Transient
	private StringProperty status;
	@Transient
	private StringProperty adres;
	@Transient
	private StringProperty email;
	@Transient
	private StringProperty telefoonummer;
	
	public Gebruiker(String password, LocalDate date, String userName, String email, String adres,String telefoonummer)
	{
		this();
		setPassword(password);
		setEmail(email);
		setAdres(adres);
		setTelefoonummer(telefoonummer);
		setStatus(StatusGebruiker.Actief);
		setDatumRegistratie(date);
		setUserName(userName);
	}
	
	protected Gebruiker()
	{
		this.userName = new SimpleStringProperty();
		this.adres = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
		this.telefoonummer = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
		this.status = new SimpleStringProperty();
	}
	
	@Access(AccessType.PROPERTY)
	public String getTelefoonummer() 
	{
		return telefoonummer.get();
	}

	public void setTelefoonummer(String telefoonummer) 
	{
		if(telefoonummer==null || telefoonummer.isBlank()) {
			throw new IllegalArgumentException();
		}
		if(!telefoonummer.matches("\\d{4}/\\d{6}")) {
			throw new IllegalArgumentException();
		}
		this.telefoonummer.set(telefoonummer);
	}
	
	public StringProperty telefoonummer()
	{
		return this.telefoonummer;
	}

	public long getId() 
	{
		return id;
	}
	
	public void setId(long id) 
	{
		this.id = id;
	}
	
	@Access(AccessType.PROPERTY)
	public String getUserName() 
	{
		return userName.get();
	}
	
	public void setUserName(String userName) 
	{
		if(userName==null || userName.isBlank()) {
			throw new IllegalArgumentException();
		}
		this.userName.set(userName);
	}
	
	public StringProperty userName()
	{
		return this.userName;
	}
	
	@Access(AccessType.PROPERTY)
	public String getPassword() 
	{
		return password.get();
	}
	
	public void setPassword(String password) 
	{
		this.password.set(password);;
	}
	
	public StringProperty password()
	{
		return this.password;
	}
	
	@Access(AccessType.PROPERTY)
	public StatusGebruiker getStatus() 
	{
		return StatusGebruiker.valueOf(status.get());
	}
	
	public void setStatus(StatusGebruiker status) 
	{
		if(status==null) {
			throw new IllegalArgumentException();
		}
		this.status.set(status.toString());
	}
	
	public StringProperty status()
	{
		return status;
	}
	
	@Access(AccessType.PROPERTY)
	public String getAdres() 
	{
		return adres.get();
	}
	
	public void setAdres(String adres) 
	{		
		this.adres.set(adres);;
	}
	
	public StringProperty adres()
	{
		return this.adres;
	}
	
	public LocalDate getDatumRegistratie() 
	{
		return datumRegistratie;
	}
	
	public void setDatumRegistratie(LocalDate datumRegistratie) 
	{
		this.datumRegistratie = datumRegistratie;
	}
	
	@Access(AccessType.PROPERTY)
	public String getEmail() 
	{
		return email.get();
	}
	
	public void setEmail(String email) 
	{
		if(email==null || email.isBlank()) {
			throw new IllegalArgumentException();
		}
		this.email.set(email);
	}
	
	public StringProperty email()
	{
		return this.email;
	}
}
