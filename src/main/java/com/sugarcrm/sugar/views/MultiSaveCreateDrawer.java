package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.modules.StandardModule;

/**
 * This class is the base class for Modules that require additional options on how to save. For now this is limited to
 * Activity Modules (Calls and Meetings) and Process Modules (Process Business Rules, Process Definitions, Process Email
 * Templates).
 * <p>
 * Being Abstract allows us the ability to further customize methods for child extended classes later.
 */
public abstract class MultiSaveCreateDrawer extends CreateDrawer {

	public MultiSaveCreateDrawer(StandardModule parentModule) throws Exception {
		super(parentModule);
		addControl("primaryButtonDropdown", "a", "css", ".btn.dropdown-toggle.btn-primary");
	}

	/**
	 * Opens the primary button's drop down list to expose alternate actions.
	 * 
	 * You must already be on the CreateDrawer to use this method.
	 * Remains on the CreateDrawer.
	 *
	 * @throws Exception
	 */
	public void openPrimaryButtonDropdown() throws Exception {
		getControl("primaryButtonDropdown").click();
		new VoodooControl("ul", "css", ".active .fld_main_dropdown .dropdown-menu").waitForVisible();
	}

	/**
	 * Enum class for "howTosave" use in create method.
	 */
	public enum Save {
		SAVE, SAVE_AND_DESIGN, SAVE_AND_SEND_INVITES
	}
}
