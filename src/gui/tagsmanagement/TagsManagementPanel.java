package gui.tagsmanagement;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TagsManagementPanel extends JPanel
{
	private static final long serialVersionUID = -7659217016124788668L;
	
	private JFrame frameContainer;
	
	public TagsManagementPanel(JFrame frameContainer)
	{
		super(true);
		this.frameContainer = frameContainer;
	}

}
