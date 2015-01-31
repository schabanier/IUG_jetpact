package stuffinder;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class Identification extends JFrame
                            implements ActionListener
{
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - SolÃ¨ne Chabanier
	private JFrame frame1 ;
	private JPanel panel1;
	private JLabel label3;
	private JTextField textField1;
	private JLabel label4;
	private JPasswordField passwordField1;
	private JButton button1;
	private JButton button2;

	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	private void initComponent()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
				// Generated using JFormDesigner Evaluation license - SolÃ¨ne Chabanier
				frame1 = new JFrame();
				panel1 = new JPanel();
				label3 = new JLabel();
				textField1 = new JTextField();
				label4 = new JLabel();
				passwordField1 = new JPasswordField();
				button1 = new BoutonCreerCompte();
				button2 = new BoutonConnexion();
				

				//======== frame1 ========
				{
					frame1.setTitle("Authentification");
					frame1.setAlwaysOnTop(true);
					frame1.setBackground(new Color(102, 0, 255));
					frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame1.setLocationRelativeTo(null);
					frame1.setVisible(true);
					Container frame1ContentPane = frame1.getContentPane();
					frame1ContentPane.setLayout(new FormLayout(
						"9*(default, $lcgap), default",
						"9*(default, $lgap), default"));
					
					button1.addActionListener(this);
					button2.addActionListener(this);

					//======== panel1 ========
					{

						// JFormDesigner evaluation mark
						panel1.setBorder(new javax.swing.border.CompoundBorder(
							new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
								"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
								javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
								java.awt.Color.red), panel1.getBorder())); panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

						panel1.setLayout(new FormLayout(
							"10*(default, $lcgap), default",
							"11*(default, $lgap), default"));
						
						panel1.setBackground(new Color(0,153,153));
						
						panel1.setVisible(true);

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
					frame1ContentPane.add(panel1, CC.xy(1, 1, CC.DEFAULT, CC.TOP));
					frame1.pack();
				
				}
				
				
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() ==button1)
		{// action à faire si clic BoutonCreerCompte//  
			
			AccountCreation accountCreationFenetre = new AccountCreation();
			accountCreationFenetre.setVisible(true);
			
		}	
		if (e.getSource()== button2)
		{// action à faire si clic BoutonConnexion  
			
			
		}
		
	}
	
}

		