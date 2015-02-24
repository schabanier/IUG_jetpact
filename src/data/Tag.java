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
	
	
	String getUid()
	{
		return uid;
	}
	
	
	public String getObjectName()
	{
		return objectName;
	}
	
	void setObjectName(String objectName)
	{
		this.objectName = objectName;
	}
	
	
	public Image getObjectImage()
	{
		return objectImage;
	}

	void setObjectImage(Image objectImage)
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
