package data;

import java.util.ArrayList;
import java.util.List;

public class Profil
{
	private List<Tag> tags;
	private String name;
	
	public Profil(String name)
	{
		this.name = name;
		tags = new ArrayList<>();
	}
	
	String getName()
	{
		return name;
	}



	public List<Tag> getTags()
	{
		return tags;
	}
	
	public boolean addTag(Tag tag)
	{
		return tags.add(tag);
	}
	
	public boolean removeTag(Tag tag)
	{
		return tags.remove(tag);
	}
	
	public void removeAllTags()
	{
		tags.clear();
	}
}
