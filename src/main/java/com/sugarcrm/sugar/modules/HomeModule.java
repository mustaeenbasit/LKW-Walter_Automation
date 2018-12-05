package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Dashboard;
import com.sugarcrm.sugar.views.Menu;

/**
 * Contains data and tasks associated with the Home module (screen), such as menu
 * items and module name
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class HomeModule extends Module {
	protected static HomeModule module;
	
	public static HomeModule getInstance() throws Exception {
		if(module == null) 
			module = new HomeModule();
		return module;
	}

	/**
	 * Perform setup which does not depend on other modules.
	 * @throws Exception
	 */
	private HomeModule() throws Exception {
		moduleNameSingular = "Home";
		moduleNamePlural = "Home";

		// Home Module Menu items
		menu = new Menu(this);
		menu.addControl("createDashboard", "a", "css", "li[data-module='Home'] ul[role='menu'] a[data-route='#Home/create']");
		menu.addControl("activityStream", "a", "css", "li[data-module='Home'] ul[role='menu'] a[data-route='#activities']");
		
		// Instantiate a new dashboard object
		// Home is not a StandardModule and therefore the instantiation is not done by the superclass
		dashboard = new Dashboard();
	}

	/**
	 * Perform setup which depends on other modules already being constructed. 	
	 */
	@Override
	public void init() throws Exception {
		VoodooUtils.voodoo.log.info("Init Home...");
		super.init();
	}
} // HomeModule
