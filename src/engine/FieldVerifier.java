package engine;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This class provides methods to verify if fields like passwords or e-mail addresses are syntactically correct. 
 * @author propriétaire
 *
 */
public class FieldVerifier
{

    public static boolean verifyName(String value)
    {
        return value.length() > 0;
    }

    public static boolean verifyEMailAddress(String email)
    {
        return email.indexOf('@') >=0;
    }

    public static boolean verifyPassword(String password)
    {
        return password.length() >= 6;
    }

    public static boolean verifyTagUID(String uid)
    {
        return uid.matches("([0-9A-F][0-9A-F]:){5}[0-9A-F][0-9A-F]");
    }

    public static boolean verifyTagName(String name)
    {
        return name.length() > 0;
    }

	public static boolean verifyImageFileName(String imageFileName)
	{
		return verifyImageFileName(new File(imageFileName));
	}


	public static boolean verifyImageFileName(File imageFile)
	{
		try {
			if(imageFile == null)
				throw new NullPointerException();
			
			new ImageIcon(ImageIO.read(imageFile)); // to test if the file can be loaded as an image.
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	
	
//	public static boolean verify(String value)
//	{
//		return true;
//	}
	
}
