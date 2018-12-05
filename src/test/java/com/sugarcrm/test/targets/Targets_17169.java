package com.sugarcrm.test.targets;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Targets_17169 extends SugarTest {
	DataSource targetDS;

	public void setup() throws Exception {
		targetDS = testData.get(testName);
		sugar().targets.api.create(targetDS);
		sugar().login();
	}

	/**
	 * Verify Duplicate check on target name and account name while creating a new target
	 * @throws Exception
	 */
	@Test
	public void Targets_17169_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().targets.navToListView();
		sugar().targets.listView.create();
		sugar().targets.createDrawer.getEditField("firstName").set(targetDS.get(4).get("firstName"));
		sugar().targets.createDrawer.getEditField("lastName").set(targetDS.get(4).get("lastName"));
		sugar().targets.createDrawer.getEditField("account_name").set(targetDS.get(4).get("account_name"));
		sugar().targets.createDrawer.save();

		//Verify "Ignore Duplicate and Save" button exists
		sugar().targets.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);

		// Verify target display
		VoodooControl myProspects = new VoodooControl("table", "css", "table.duplicates-selectedit");
		myProspects.assertContains(targetDS.get(0).get("firstName")+" "+targetDS.get(0).get("lastName"),true);
		myProspects.assertContains(targetDS.get(2).get("firstName")+" "+targetDS.get(2).get("lastName"),true);
		myProspects.assertContains(targetDS.get(1).get("firstName")+" "+targetDS.get(1).get("lastName"),false);
		myProspects.assertContains(targetDS.get(3).get("firstName")+" "+targetDS.get(3).get("lastName"),false);
		sugar().targets.createDrawer.ignoreDuplicateAndSave();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
