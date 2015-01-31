package stuffinder;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class AccountCreation extends JDialog {
	
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
	
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
				// Generated using JFormDesigner Evaluation license - SolÃ¨ne Chabanier
			
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
				
				
				//======== dialog1 ========
				{
					dialog1.setTitle("Cr\u00e9er compte Stuffinder");
					dialog1.setResizable(false);
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


}
