package controllers;

import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import domein.GebruikerBeheerder;
import domein.IWerknemer;
import repository.WerknemerDaoJpa;

public class LoginController implements Controller
{
	private GebruikerBeheerder gebruikerBeheerder;
	private IWerknemer w;

	public LoginController() 
	{
		this.gebruikerBeheerder = new GebruikerBeheerder(new WerknemerDaoJpa());
	}
	
	public void meldAan(String userName, String password)
	{
		try {
			IWerknemer w = gebruikerBeheerder.meldAan1(userName, password);
			setWerknemer(w);
		}catch(NoSuchElementException ex ){
			throw new NoSuchElementException(ex.getMessage());
		}catch(EntityNotFoundException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
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
}
