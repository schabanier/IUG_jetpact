package data;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Tag
{
	private String uid;
	
	private String objectName;

	private Image objectImage;
	
	private List<Position> lastPositions;
	

	public Tag(String uid, String objectName)
	{
		this(uid, objectName, null);
	}
	
	public Tag(String uid, String objectName, Image objectImage)
	{
		this.uid = uid;
		this.objectName = objectName;
		this.objectImage = objectImage;
		
		lastPositions = new ArrayList<>();
	}
	
	
	public String getUid()
	{
		return uid;
	}
	
	
	public String getObjectName()
	{
		return objectName;
	}
	
	public void setObjectName(String objectName)
	{
		if(objectName == null)
			throw new NullPointerException();
		
		this.objectName = objectName;
	}
	
	
	public Image getObjectImage()
	{
		return objectImage;
	}

	public void setObjectImage(Image objectImage)
	{
		this.objectImage = objectImage;
	}
	
	
	public void addLastPosition(Position p)
	{
		lastPositions.add(p);
	}
	
	public List<Position> getLastPositions()
	{
		return lastPositions;
	}
}
