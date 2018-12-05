package com.sugarcrm.test.targets;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Targets_17171 extends SugarTest {
	DataSource phoneWorkDS;

	public void setup() throws Exception {
		phoneWorkDS = testData.get(testName);

		// Supply phone work from CSV to default Target record
		FieldSet phoneData = new FieldSet();
		phoneData.put("phoneWork", phoneWorkDS.get(0).get("phoneWork"));
		sugar().targets.api.create(phoneData);
		sugar().login();
	}

	/**
	 * Verify trim the leading/trailing blanks on office phone with the duplicate check
	 * @throws Exception
	 */
	@Test
	public void Targets_17171_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-513
		VoodooControl phoneWorkCtrl = new VoodooControl("table", "css", "table.duplicates-selectedit");

		// Create new target record with same phone number (add leading trailing blanks spaces) and lastname field as we have for 1st record in setup
		sugar().targets.navToListView();

		for (int i = 0; i < phoneWorkDS.size(); i++) {
			sugar().targets.listView.create();
			sugar().targets.createDrawer.showMore();
			sugar().targets.createDrawer.getEditField("lastName").set(sugar().targets.getDefaultData().get("lastName"));
			sugar().targets.createDrawer.getEditField("phoneWork").set(phoneWorkDS.get(i).get("phoneWork"));
			sugar().targets.createDrawer.save();

			// Verify "Ignore Duplicate and Save" button exists
			sugar().targets.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);

			// Verify target display
			phoneWorkCtrl.assertContains(phoneWorkDS.get(0).get("phoneWork"), true);
			if(i == phoneWorkDS.size()-1)
				sugar().targets.createDrawer.ignoreDuplicateAndSave();
			else
				sugar().targets.createDrawer.cancel();
		}

		// TODO: CB-230
		// Verify that only two records exists in the list view
		sugar().targets.listView.getControl("checkbox01").assertExists(true);
		sugar().targets.listView.getControl("checkbox02").assertExists(true);
		sugar().targets.listView.getControl("checkbox03").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
