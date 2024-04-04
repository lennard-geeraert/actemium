package domein;

import java.time.LocalDate;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import domein.enums.StatusContract;
import domein.enums.StatusTicket;

@Entity
@Table(name = "Contract")
public class Contract 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private LocalDate eindDatum;
	private int doorlooptijd;
	private LocalDate startDatum;
	private StatusContract statusContract;
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
	private List<Ticket> lijstTickets;
	
	public Contract(int doorlooptijd, LocalDate start)  
	{	
		setDoorlooptijd(doorlooptijd);
		setStartDatum(start);
		setStatusContract(StatusContract.InAanvrag);
		setEindDatum(this.startDatum.plusYears(doorlooptijd));
		lijstTickets = new ArrayList<Ticket>();
	}
	
	public Contract(int doorlooptijd, LocalDate start, StatusContract status) 
	{
		this(doorlooptijd,start);
		setStatusContract(status);
		setEindDatum(this.startDatum.plusYears(doorlooptijd));
		lijstTickets = new ArrayList<Ticket>();
	}
	
	protected Contract() {}
	
	public void addTickets(Ticket t) 
	{
		if(t==null) {
			throw new IllegalArgumentException();
		}
		lijstTickets.add(t);
	}
	
	public Long getAantalBehandeldeTickets() 
	{
		return lijstTickets.stream().filter(t -> t.getStatus().equals(StatusTicket.Afgehandeld)).count();
	}
	
	public List<Ticket> getLijstTickets() 
	{
		return lijstTickets;
	}

	public LocalDate getEindDatum() 
	{
		return eindDatum;
	}

	public void setEindDatum(LocalDate eindDatum) 
	{
		this.eindDatum = eindDatum;
	}

	public int getDoorlooptijd() 
	{
		return doorlooptijd;
	}

	public void setDoorlooptijd(int doorlooptijd) 
	{
		this.doorlooptijd = doorlooptijd;
	}

	public LocalDate getStartDatum() 
	{
		return startDatum;
	}

	public void setStartDatum(LocalDate startDatum) 
	{
		this.startDatum = startDatum;
	}

	public StatusContract getStatusContract() 
	{
		return statusContract;
	}

	public void setStatusContract(StatusContract statusContract) 
	{
		this.statusContract = statusContract;
	}	
}
