package com.sugarcrm.test.tasks;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21161 extends SugarTest {

	public void setup() throws Exception {
		sugar.tasks.api.create();
		FieldSet fs = new FieldSet();
		fs.put("subject", testName);
		sugar.tasks.api.create(fs);
		sugar.login();	
	}

	/**
	 * New action dropdown list in tasks list view page
	 * @throws Exception
	 */
	@Test
	public void Tasks_21161_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Tasks and select all records in the listView
		sugar.tasks.navToListView();
		sugar.tasks.listView.getControl("actionDropdown").assertVisible(true);
		boolean isDisable = sugar.tasks.listView.getControl("actionDropdown").isDisabled();
		Assert.assertTrue("Assert Action DropDown is Disabled Failed",isDisable);
		sugar.tasks.listView.getControl("selectAllCheckbox").click();
		sugar.tasks.listView.openActionDropdown();

		// Asserting the options available in Action DropDown(except Merge) & Executing Delete function
		sugar.tasks.listView.getControl("massUpdateButton").assertVisible(true);
		sugar.tasks.listView.getControl("exportButton").assertVisible(true);
		VoodooControl delete = sugar.tasks.listView.getControl("deleteButton");
		delete.assertVisible(true);
		delete.click();
		sugar.alerts.getWarning().confirmAlert();
		sugar.tasks.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}