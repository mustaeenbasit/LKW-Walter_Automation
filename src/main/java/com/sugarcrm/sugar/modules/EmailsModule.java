package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.EmailRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * Contains data and tasks associated with the Emails module, such as field
 * data.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com> 
 */
public class EmailsModule extends BWCModule {
	protected static EmailsModule module;
	
	public static EmailsModule getInstance() throws Exception {
		if(module == null) 
			module = new EmailsModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private EmailsModule() throws Exception {
		moduleNameSingular = "Emails";
		moduleNamePlural = "Emails";
		bwcSubpanelName = "Activities";
		recordClassName = EmailRecord.class.getName();
		
		// Load field defs from CSV
		loadFields();

		// Define the columns on the ListView.

		// Needed in the Module Constructor for each subclass because of the order in which classes are initialized.
		subpanels.put("standard", new StandardSubpanel(this));
		subpanels.put("bwc", new BWCSubpanel(this));
		
		// Emails Module Menu items
		// This is a dup of Classic, needed for standard code to work.
		menu = new Menu(this);
		menu.addControl("viewEmails", "a", "css", "[data-navbar-menu-item='LNK_VIEW_MY_INBOX']");
		menu.addControl("createTemplate", "a", "css", "[data-navbar-menu-item='LNK_NEW_EMAIL_TEMPLATE']");
		menu.addControl("viewTemplates", "a", "css", "[data-navbar-menu-item='LNK_EMAIL_TEMPLATE_LIST']");
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Documents...");
		super.init();
		
		// Add Subpanels
		detailView.addSubpanels();

		// Define the columns of the StandardSubpanel
		StandardSubpanel standardsubpanel = (StandardSubpanel) subpanels.get("standard");
		standardsubpanel.addHeader("name");
		standardsubpanel.addHeader("status");
		standardsubpanel.addHeader("date_entered");
		standardsubpanel.addHeader("date_modified");
		standardsubpanel.addHeader("assigned_user_name");

	}

	public void waitForSugarMsgWindow() throws Exception {
		waitForSugarMsgWindow(15000);
	}

	public void waitForSugarMsgWindow(int timeOut) throws Exception {
		new VoodooControl("div", "id", "sugarMsgWindow_h").waitForInvisible(timeOut);
	}
}