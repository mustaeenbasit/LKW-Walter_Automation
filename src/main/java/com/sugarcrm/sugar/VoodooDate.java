package com.sugarcrm.sugar;

import com.sugarcrm.sugar.views.View;

/**
 * Exposes functionality for interacting directly with SugarCRM date fields which use the date
 * picker library.
 * 
 * @author David Safar <dsafar@sugarcrm.com>
 */
public class VoodooDate extends VoodooControl {
	// TODO: Add support for the picker UI.

	public VoodooDate(String tagIn, String strategyNameIn, String hookStringIn)
		throws Exception {
		super(tagIn, strategyNameIn, hookStringIn);
	}

	/**
	 * Set the text of the date field using JavaScript to avoid interference by the date picker
	 * library.
	 * @param 	toSet	date value to be set
	 * @throws	Exception
	 */
	public void set(String toSet) throws Exception {
		VoodooUtils.voodoo.log.info("Setting " + this + " to " + toSet);

		waitForElement().executeJavascript("jQuery(arguments[0]).val('" + toSet + "').change().blur();");
		VoodooUtils.waitForReady();
	}	
}
