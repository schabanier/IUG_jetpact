package gui;

import java.awt.Image;

import javax.swing.ImageIcon;

public class IconsProvider
{
	public static final ImageIcon iconDeleteElement = new ImageIcon(IconsProvider.class.getResource("/images/remove.png"));
	public static final ImageIcon iconEditElement = new ImageIcon(IconsProvider.class.getResource("/images/edit.png"));

	public static final Image defaultObjectImageBig = new ImageIcon(IconsProvider.class.getResource("/images/default_object_image.png")).getImage();
	public static final Image defaultObjectImageLittle = new ImageIcon(IconsProvider.class.getResource("/images/default_object_image.png")).getImage().getScaledInstance(25, 25, Image.SCALE_FAST);
}
