package data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Account
{
	private String pseudo;
	private String firstName;
	private String lastName;
	private Date birthDate;
	private String emailAddress;
	
	private List<Tag> tags;
	private List<Profile> profils;
	
	public Account(String pseudo, String firstName, String lastName, Date birthDate, String emailAddress)
	{
		if(pseudo == null)
			throw new NullPointerException();
		
		this.pseudo = pseudo;
		
		setBirthDate(birthDate);
		setFirstName(firstName);
		setLastName(lastName);
		setMailAddress(emailAddress);
		
		tags = new ArrayList<>();
		profils = new ArrayList<>();
	}

	public List<Tag> getTags()
	{
		return tags;
	}

	public List<Profile> getProfils()
	{
		return profils;
	}

	public String getPseudo()
	{
		return pseudo;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		if(firstName == null)
			throw new NullPointerException();
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		if(lastName == null)
			throw new NullPointerException();
		this.lastName = lastName;
	}

	public Date getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate(Date birthDate)
	{
		if(birthDate == null)
			throw new NullPointerException();
		this.birthDate = birthDate;
	}

	public String getMailAddress()
	{
		return emailAddress;
	}

	public void setMailAddress(String emailAddress)
	{
		if(emailAddress == null)
			throw new NullPointerException();
		
		this.emailAddress = emailAddress;
	}
}
