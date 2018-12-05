package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Bugs_28962 extends SugarTest {
	DataSource customData = new DataSource();
	
	public void setup() throws Exception {             
		sugar().bugs.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		
		// Create records in Admin-> Bugs-> Releases module
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("release").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		customData = testData.get(testName);
		
		// TODO: VOOD-1196
		for (int i = 0; i < 2; i++) {
			new VoodooControl("input","css","input[name='New']").click();
			VoodooUtils.focusDefault();
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("input","css",".edit.view tbody tr [name='name']").set(testName+"_"+i);
			new VoodooControl("select", "css", "[name='status']").set(customData.get(i).get("status"));
			new VoodooControl("input","css","input[name='button']").click();
		}
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Search and Select Releases drawer in Bugs module should not displayed Inactive releases
	 * 
	 */
	@Test
	public void Bugs_28962_execute() throws Exception {
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
		
		// Verify that Pre-defined filter (i.e. Active releases) is applied and records are filtered by active status
		// TODO: VOOD-1162
		new VoodooControl("div", "css", ".layout_Releases .choice-filter").assertContains(customData.get(0).get("filter"), true);
		new VoodooControl("div", "css", ".list.fld_name div").assertContains(testName+"_0", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}