package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;

/**
 * Models the CreateDrawer for KB Module.
 * @author Mazen Louis <mlouis@sugarcrm.com>
 *
 */
public class KBCreateDrawer extends CreateDrawer {
	/**
	 * Initializes the KBCreateDrawer and specifies its parent module so that it knows which fields are available.
	 * @param parentModule - the module that owns this KBCreateDrawer, likely passed in using the module's this variable when constructing the CreateDrawer.
	 * @throws Exception
	 */
	public KBCreateDrawer(StandardModule parentModule) throws Exception {
		super(parentModule);
	}

	/**
	 * Set a field in the this KBCreateDrawer.
	 * <p>
	 * @param field String of the field name to set.
	 * @param value String value to be set.
	 * @throws Exception
	 */
	@Override
	public void setField(String field, String value) throws Exception {
		if (field != null && value != null && getEditField(field) != null) {
			VoodooUtils.voodoo.log.fine("Editing field: " + field);
			VoodooUtils.pause(100);
			VoodooUtils.voodoo.log.fine("Setting " + field + " field to: " + value);
			if(field.equals("body")) {
				VoodooUtils.focusFrame(new VoodooControl("iframe", "css", "[id$='_ifr']").waitForElement());
			}else {
				getEditField(field).scrollIntoViewIfNeeded(false);
			}
			getEditField(field).set(value);
			VoodooUtils.pause(100);
			if(field.equals("body")) {
				VoodooUtils.focusDefault();
			}
		} else {
			VoodooUtils.voodoo.log.warning("Field " + field +
					" was in the supplied record data, but no value was given or no Edit Control was available.");
		}
	}
}