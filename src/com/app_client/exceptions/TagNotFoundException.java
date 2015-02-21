package com.app_client.exceptions;

public class TagNotFoundException extends Exception
{
	private static final long serialVersionUID = 5397349417381524503L;
	
	public TagNotFoundException(String tagId)
	{
		super(tagId);
	}
	
	public String getTagIdNotFound()
	{
		return super.getMessage();
	}
}
