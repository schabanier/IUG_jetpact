/*
 * Created by JFormDesigner on Thu Jan 22 16:17:24 CET 2015
 */

package test;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Solene Chabanier
 */
public class stuffinder  {

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - SolÃ¨ne Chabanier
		frame1 = new JFrame();
		panel1 = new JPanel();
		label3 = new JLabel();
		textField1 = new JTextField();
		label4 = new JLabel();
		passwordField1 = new JPasswordField();
		button1 = new JButton();
		button2 = new JButton();
		dialog1 = new JDialog();
		label8 = new JLabel();
		textField2 = new JTextField();
		label9 = new JLabel();
		textField5 = new JTextField();
		label10 = new JLabel();
		textField4 = new JTextField();
		label5 = new JLabel();
		textField3 = new JTextField();
		label6 = new JLabel();
		passwordField2 = new JPasswordField();
		label7 = new JLabel();
		passwordField3 = new JPasswordField();
		button3 = new JButton();
		button4 = new JButton();

		//======== frame1 ========
		{
			frame1.setTitle("Authentification");
			frame1.setAlwaysOnTop(true);
			frame1.setBackground(new Color(102, 0, 255));
			Container frame1ContentPane = frame1.getContentPane();
			frame1ContentPane.setLayout(new FormLayout(
				"9*(default, $lcgap), default",
				"9*(default, $lgap), default"));

			//======== panel1 ========
			{
				panel1.setBackground(new Color(0, 153, 153));

				// JFormDesigner evaluation mark
				panel1.setBorder(new javax.swing.border.CompoundBorder(
					new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
						"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
						javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
						java.awt.Color.red), panel1.getBorder())); panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

				panel1.setLayout(new FormLayout(
					"10*(default, $lcgap), default",
					"11*(default, $lgap), default"));

				//---- label3 ----
				label3.setText("Pseudo:");
				panel1.add(label3, CC.xywh(3, 5, 5, 2));
				panel1.add(textField1, CC.xy(9, 5));

				//---- label4 ----
				label4.setText("Mot de passe:");
				panel1.add(label4, CC.xy(3, 9));
				panel1.add(passwordField1, CC.xy(9, 9));

				//---- button1 ----
				button1.setText("Cr\u00e9er un compte");
				panel1.add(button1, CC.xy(3, 15));

				//---- button2 ----
				button2.setText("Connexion");
				panel1.add(button2, CC.xy(9, 15));
			}
			frame1ContentPane.add(panel1, CC.xy(1, 1, CC.LEFT, CC.TOP));
			frame1.pack();
			frame1.setLocationRelativeTo(frame1.getOwner());
		}

		//======== dialog1 ========
		{
			dialog1.setTitle("Cr\u00e9er compte Stuffinder");
			dialog1.setResizable(false);
			dialog1.setBackground(new Color(0, 153, 153));
			Container dialog1ContentPane = dialog1.getContentPane();
			dialog1ContentPane.setLayout(new FormLayout(
				"8*(default, $lcgap), default",
				"8*(default, $lgap), default"));

			//---- label8 ----
			label8.setText("Nom:");
			dialog1ContentPane.add(label8, CC.xy(3, 1));
			dialog1ContentPane.add(textField2, CC.xywh(7, 1, 7, 1));

			//---- label9 ----
			label9.setText("Pr\u00e9nom:");
			dialog1ContentPane.add(label9, CC.xy(3, 3));
			dialog1ContentPane.add(textField5, CC.xywh(7, 3, 7, 1));

			//---- label10 ----
			label10.setText("Adresse mail:");
			dialog1ContentPane.add(label10, CC.xy(3, 5));
			dialog1ContentPane.add(textField4, CC.xywh(7, 5, 7, 1));

			//---- label5 ----
			label5.setText("Pseudo:");
			dialog1ContentPane.add(label5, CC.xy(3, 7));
			dialog1ContentPane.add(textField3, CC.xywh(7, 7, 7, 1));

			//---- label6 ----
			label6.setText("Mot de passe:");
			dialog1ContentPane.add(label6, CC.xy(3, 9));
			dialog1ContentPane.add(passwordField2, CC.xywh(7, 9, 7, 1));

			//---- label7 ----
			label7.setText("Confirmer:");
			dialog1ContentPane.add(label7, CC.xy(3, 11));
			dialog1ContentPane.add(passwordField3, CC.xywh(7, 11, 7, 1));

			//---- button3 ----
			button3.setText("Annuler");
			dialog1ContentPane.add(button3, CC.xy(3, 13));

			//---- button4 ----
			button4.setText("Cr\u00e9er");
			dialog1ContentPane.add(button4, CC.xy(11, 13));
			dialog1.pack();
			dialog1.setLocationRelativeTo(dialog1.getOwner());
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - SolÃ¨ne Chabanier
	private JFrame frame1;
	private JPanel panel1;
	private JLabel label3;
	private JTextField textField1;
	private JLabel label4;
	private JPasswordField passwordField1;
	private JButton button1;
	private JButton button2;
	private JDialog dialog1;
	private JLabel label8;
	private JTextField textField2;
	private JLabel label9;
	private JTextField textField5;
	private JLabel label10;
	private JTextField textField4;
	private JLabel label5;
	private JTextField textField3;
	private JLabel label6;
	private JPasswordField passwordField2;
	private JLabel label7;
	private JPasswordField passwordField3;
	private JButton button3;
	private JButton button4;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
