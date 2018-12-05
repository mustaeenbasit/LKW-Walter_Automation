package com.sugarcrm.test.targets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Targets_28332  extends SugarTest {
	public void setup() throws Exception {
		sugar().targets.api.create();
		sugar().login();
	}

	/**
	 *  Target - Convert Target: Verify that a target can be converted to a lead multiple times.
	 *
	 * @throws Exception
	 */
	@Test
	public void Targets_28332_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().targets.navToListView();
		sugar().targets.listView.clickRecord(1);
		sugar().targets.recordView.openPrimaryButtonDropdown();

		// Converting the Target for first time
		// TODO: VOOD-1151
		VoodooControl convertTarget= new VoodooControl("a", "css", ".fld_convert_button.detail a");
		convertTarget.click();
		FieldSet fs=testData.get(testName).get(0);
		sugar().targets.createDrawer.getEditField("account_name").set(fs.get("account_name1"));

		// Verify user moves to Targets record view after save
		sugar().targets.createDrawer.save();
		sugar().targets.recordView.assertVisible(true);

		// Verify that converted lead link displayed after first conversion
		// TODO: VOOD-1298
		VoodooControl convertedTarget= new VoodooControl("div", "css", "[data-voodoo-name='convert-results'] div");
		convertedTarget.assertVisible(true);
		sugar().targets.navToListView();
		sugar().targets.listView.clickRecord(1);
		sugar().targets.recordView.openPrimaryButtonDropdown();

		// Converting the Target for second time
		convertTarget.click();
		sugar().targets.createDrawer.getEditField("account_name").set(fs.get("account_name2"));
		sugar().targets.createDrawer.save();
		sugar().targets.createDrawer.ignoreDuplicateAndSave();
		sugar().targets.recordView.assertVisible(true);

		// Verify that converted lead link displayed after second conversion and newly converted leads are displayed properly in list view of leads module
		convertedTarget.assertVisible(true);
		sugar().leads.navToListView();
		sugar().leads.listView.verifyField(1, "accountName",fs.get("account_name2"));
		sugar().leads.listView.verifyField(2, "accountName",fs.get("account_name1"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
