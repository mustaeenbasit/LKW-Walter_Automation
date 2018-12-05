package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;

public class Bugs_28958 extends SugarTest {
	public void setup() throws Exception {             
		sugar().bugs.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		
		// Create a record in Admin-> Bugs-> Releases module
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("release").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1196
		new VoodooControl("input","css","input[name='New']").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input","css",".edit.view tbody tr [name='name']").set(testName);
		new VoodooControl("input","css","input[name='button']").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Search and Select Releases drawer in Bugs module should be displayed Release and Date Modified 
	 * columns only
	 * 
	 */
	@Test
	public void Bugs_28958_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Bugs List view and inline edit
		sugar().bugs.navToListView();
		sugar().bugs.listView.editRecord(1);
		
		// Click on down arrow at "Fixed in Releases" columns
		// TODO: VOOD-1425
		VoodooSelect fixedInRelease = (VoodooSelect) new VoodooSelect("a", "css", ".fld_fixed_in_release_name.edit div a");
		fixedInRelease.scrollIntoViewIfNeeded(false);
		fixedInRelease.click();
		
		// Click on "Search and Select" link
		fixedInRelease.selectWidget.getControl("searchForMoreLink").click();
		VoodooUtils.waitForReady();
		
		// Verify that "Search and Select Releases" drawer displayed "Release version" and "Date Modified" columns
		// TODO: VOOD-1162
		new VoodooControl("th", "css", ".layout_Releases .orderByname").assertVisible(true);
		new VoodooControl("th", "css", ".layout_Releases .orderBydate_modified").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}