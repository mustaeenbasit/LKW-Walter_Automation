package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.modules.Module;

/**
 * Models the Menu for SugarCRM modules. 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class Menu extends View {

	/**
	 * Initializes the menu and specifies its parent module.  
	 * @param parentModule - the module that owns this menu
	 * @throws Exception 
	 */
	public Menu(Module parentModule) throws Exception {
		super(parentModule);
	}
}