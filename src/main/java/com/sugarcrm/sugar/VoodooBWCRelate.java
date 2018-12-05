package com.sugarcrm.sugar;

import com.sugarcrm.sugar.views.View;

/**
 * Exposes functionality for interacting directly with select lists in
 * SugarCRM.  The present implementation is specific to select in BWC Modules; 
 * Sidecar dropdown should be handled by VoodooSelect; regular select tags should 
 * be handled with VoodooControl, not with this class.
 * 
 * @author David Safar <dsafar@sugarcrm.com>
 * @author Jessica Cho
 */
public class VoodooBWCRelate extends VoodooControl {
	View selectWidget = new View();

	public VoodooBWCRelate(String tagIn, String strategyNameIn, String hookStringIn)
			throws Exception {
		super(tagIn, strategyNameIn, hookStringIn);
		
		selectWidget.addControl("clearBtn", "button", "id", this.hookString.replace("btn_", "btn_clr_"));
		selectWidget.addControl("textInput", "input", "id", this.hookString.replace("btn_", ""));
	}

	/**
	 * Set the text of a related field in BWC modules.  Focus should already be
	 * made in the bwc-frame when this method is called.
	 * @param toSet	text/value to be set
	 * @throws Exception 
	 */
	public void set(String toSet) throws Exception {
		VoodooUtils.voodoo.log.info("Setting " + this + " to " + toSet);
		
		waitForElement().click();
		VoodooUtils.pause(1000);
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "XPATH", "//a[contains(text(), '" + toSet + "')]").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
	}
}
