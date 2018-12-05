package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21042 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().quotes.api.create();
		sugar().login();
	}

	/**
	 * Create Note_Verify that relevant modules records can be selected for notes record from note create view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21042_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to quotes record view
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Go to History Sub-panel's notes create view
		// TODO: VOOD-1000
		new VoodooControl("a", "css", "#formHistory a").click();
		VoodooUtils.focusDefault();
		
		// Click "Related To" Dropdown button to select a related module record, such as accounts
		sugar().notes.createDrawer.getEditField("relRelatedToModule").set(sugar().accounts.moduleNameSingular);
		
		// Enter 'a' in select field to search and select record from list
		sugar().notes.createDrawer.getEditField("relRelatedToValue").set("a");
		
		// Verify that relevant modules records is selected after clicking the records link
		sugar().notes.createDrawer.getEditField("relRelatedToValue").assertContains(sugar().accounts.getDefaultData().get("name"), true);
		
		sugar().notes.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}