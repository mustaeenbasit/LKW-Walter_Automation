package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.modules.ProcessModule;

/**
 * Models the CreateDrawer for Process Type Modules (Process Business Rules, Process Definitions, Process Email Templates)
 *
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class ProcessCreateDrawer extends MultiSaveCreateDrawer {
	/**
	 * Initializes the ProcessCreateDrawer and specifies its parent module so that it knows which fields are available.
	 * @param parentModule - the module that owns this CreateDrawer, likely passed in using the module's this variable when constructing the CreateDrawer.
	 * @throws Exception
	 */
	public ProcessCreateDrawer(ProcessModule parentModule) throws Exception {
		super(parentModule);
	}

	/**
	 * Click Save and Design on this Process Create Drawer.
	 * <p>
	 * When used, you will be taken to the Design view for this Process Module.
	 *
	 * @throws Exception
	 */
	public void saveAndDesign() throws Exception {
		openPrimaryButtonDropdown();
		getControl("saveAndDesignButton").click();
	}
}