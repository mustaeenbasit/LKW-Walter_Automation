package com.sugarcrm.sugar.views;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class StudioResetView extends View {
	protected static StudioResetView view;
	
	private StudioResetView() throws Exception {
		addControl("resetNow", "button", "id", "execute_repair");
		addControl("checkRelationships", "input", "css", "input[name='relationships']");
		addControl("checkFields", "input", "css", "input[name='fields']");
		addControl("checkLayouts", "input", "css", "input[name='layouts']");
		addControl("checkLabels", "input", "css", "input[name='labels']");
		addControl("checkExtensions", "input", "css", "input[name='extensions']");
	}
	
	public static StudioResetView getInstance() throws Exception {
		if (view == null)
			view = new StudioResetView();
		return view;
	}
	
	/**
	 * Reset a Studio module.
	 * Should already be in the Module Studio Reset View.
	 * 
	 * @throws Exception
	 */
	public void reset() throws Exception {
		FieldSet fs = new FieldSet();
		fs.put("Relationships", "true");
		fs.put("Fields", "true");
		fs.put("Layouts", "true");
		fs.put("Labels", "true");
		fs.put("Extensions", "true");

		reset(fs);
	}

	/**
	 * Reset a Studio module.
	 * Should already be in the Module Studio Reset View.
	 * 
	 * @param FieldSet options contains enable/disable key/value options for ALL the checkboxes on the reset view
	 * @throws Exception
	 */
	public void reset(FieldSet options) throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("checkRelationships").set(options.get("Relationships"));
		getControl("checkFields").set(options.get("Fields"));
		getControl("checkLayouts").set(options.get("Layouts"));
		getControl("checkLabels").set(options.get("Labels"));
		getControl("checkExtensions").set(options.get("Extensions"));

		getControl("resetNow").click();
		VoodooUtils.waitForReady(120000);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}
}