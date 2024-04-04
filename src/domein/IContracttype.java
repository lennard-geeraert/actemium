package domein;

import java.util.Set;

import domein.enums.ManierTicketAanmaak;
import domein.enums.StatusContracttype;
import domein.enums.TijdstippenAanmaakTicket;
import javafx.beans.property.StringProperty;

public interface IContracttype
{
	long getId();
	String getNaam();
	StatusContracttype getIsActief();

	ManierTicketAanmaak getManierTicketAanmaak();

	TijdstippenAanmaakTicket getTijdStippenMogelijkheid();
	int getMaximaleAfhandeltijd();
	int getMinimaleAfhandeltijd();
	int getPrijsContract();
	Long getLopendeContracten();

   long getAantalBehandeldeTickets();
   Set<Contract> getContracten(); 
   StringProperty naam();
   StringProperty manierTicketAanmaak();
   StringProperty tijdStippenMogelijkheid();
  
   StringProperty  isActief();
}
