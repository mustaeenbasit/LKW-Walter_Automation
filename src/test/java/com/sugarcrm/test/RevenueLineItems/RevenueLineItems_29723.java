package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class RevenueLineItems_29723 extends SugarTest {
	public void setup() throws Exception {
		// Login
		sugar().login();
	}

	/**
	 * Verify that RLI is not appearing in relate to drop down filed when it is not configured
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_29723_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet relatedToModuleData = testData.get(testName).get(0);

		// Go to Tasks -> Create Task
		sugar().tasks.navToListView();
		sugar().tasks.listView.create();
		sugar().tasks.createDrawer.showMore();

		// Click Related to Field drop down and search for RLI module
		VoodooSelect relatedToParentTypeCtrl = (VoodooSelect) sugar().tasks.createDrawer.getEditField("relRelatedToParentType");
		relatedToParentTypeCtrl.click();
		relatedToParentTypeCtrl.selectWidget.getControl("searchBox").set(relatedToModuleData.get("relatedToModule"));

		// Verify that RLI should not be listed in the "Related to" drop down
		// TODO: VOOD-1463
		new VoodooControl("li", "css", "#select2-drop .select2-results").assertEquals(relatedToModuleData.get("noMatchesFound"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}