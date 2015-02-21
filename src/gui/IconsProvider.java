package gui;

import java.awt.Image;

import javax.swing.ImageIcon;

public class IconsProvider
{
	public static final int OBJECT_LITTLE_IMAGE_WIDTH = 25;
	public static final int OBJECT_LITTLE_IMAGE_HEIGHT = 25;

	public static final int OBJECT_BIG_IMAGE_WIDTH = 180;
	public static final int OBJECT_BIG_IMAGE_HEIGHT = 180;
	
	
	public static final ImageIcon iconDeleteElement = new ImageIcon(IconsProvider.class.getResource("/images/remove.png"));
	public static final ImageIcon iconEditElement = new ImageIcon(IconsProvider.class.getResource("/images/edit.png"));

	public static final Image defaultObjectImageBig = new ImageIcon(IconsProvider.class.getResource("/images/default_object_image.png")).getImage().getScaledInstance(OBJECT_BIG_IMAGE_WIDTH, OBJECT_BIG_IMAGE_HEIGHT, Image.SCALE_FAST);
	public static final Image defaultObjectImageLittle = new ImageIcon(IconsProvider.class.getResource("/images/default_object_image.png")).getImage().getScaledInstance(OBJECT_LITTLE_IMAGE_WIDTH, OBJECT_LITTLE_IMAGE_HEIGHT, Image.SCALE_FAST);

}
