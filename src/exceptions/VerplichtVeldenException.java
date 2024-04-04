package exceptions;

import java.util.ArrayList;
import java.util.List;

public class VerplichtVeldenException extends Exception 
{
	private List<String> verplichteAttributen = new ArrayList<String>();
	
	public VerplichtVeldenException() 
	{
		super();
	}

	public VerplichtVeldenException(String message, List<String> verplichteAttributen) 
	{
		super(message);
		this.verplichteAttributen = verplichteAttributen;
	}

	public VerplichtVeldenException(String message) 
	{
		super(message);
	}

	public String getInformatieVanNietIngevuldeElementen() 
	{
		StringBuilder string = new StringBuilder();
		for(String s : verplichteAttributen) {
			
			string.append(s).append(", ");
			
		}
		string.delete(string.length() - 2, string.length());
		return string.toString();
	}
}
