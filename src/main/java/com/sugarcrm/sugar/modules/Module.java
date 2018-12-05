package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.views.Dashboard;
import com.sugarcrm.sugar.views.Menu;
import com.sugarcrm.sugar.AppModel;
import com.sugarcrm.sugar.PortalAppModel;

/**
 * Module base class from which all modules extend (through RecordsModule for
 * modules which contain records or directly for modules that don't.)
 * 
 * @author David Safar <dsafar@sugarcrm.com>
 * 
 */
public abstract class Module {
	/** The singular name of this module, e.g. "Account" */
	public String moduleNameSingular = "";
	/** The plural name of this module, e.g. "Accounts" */
	public String moduleNamePlural = "";
	/** A representation of the menu for this module. */
	public Menu menu;
	/** A representation of the dashboard. */
	public Dashboard dashboard;
	/**
	 * Backwards compatibility flag. False for a StandardModule, true for a
	 * backwards-compatibility module.
	 */
	public boolean bwc = false;

	public Module() throws Exception {
	}

	/**
	 * To be overridden if needed by subclasses. Any initialization which needs
	 * access to other modules should be done here and not in the constructor to
	 * prevent errors based on the order in which modules are constructed.
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
	}

	/**
	 * Returns a single menu of this module type
	 * 
	 * @return a menu object
	 * @throws Exception
	 */

	public Menu getMenu() throws Exception {
		return menu;
	}

	public boolean isBwc() {
		return bwc;
	}

	/**
	 * Helper method to return a AppModel instance
	 * @return AppModel instance
	 */
	protected static AppModel sugar() {
		return AppModel.getInstance();
	}

	/**
	 * Helper method to return a PortalAppModel instance
	 * @return PortalAppModel instance
	 */
	protected static PortalAppModel portal() {
		return PortalAppModel.getInstance();
	}
}