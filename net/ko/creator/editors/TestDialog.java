package net.ko.creator.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;

public class TestDialog extends Composite {

	private Group grpOptions = null;
	private Button ckCreateControllerFile = null;
	private Button ckCreateMessagesFile = null;

	public TestDialog(Composite parent, int style) {
		super(parent, style);
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        createGrpOptions();
        this.setSize(new Point(548, 325));
			
	}

	/**
	 * This method initializes grpOptions	
	 *
	 */
	private void createGrpOptions() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		grpOptions = new Group(this, SWT.NONE);
		//GridData data = new GridData(GridData.FILL_BOTH);
		//data.widthHint=200;
		//data.heightHint=100;
		grpOptions.setLayout(new GridLayout());
		//grpOptions.setLayoutData(data);
		
		grpOptions.setBounds(new Rectangle(30, 28, 273, 160));
		ckCreateControllerFile = new Button(grpOptions, SWT.CHECK);
		ckCreateControllerFile.setText("Ajouter le contrôleur");
		ckCreateControllerFile.setLayoutData(gridData);
		ckCreateMessagesFile = new Button(grpOptions, SWT.CHECK);
		ckCreateMessagesFile.setText("Créer le fichier de messages par défaut");
		ckCreateMessagesFile.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
					}
				});
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
