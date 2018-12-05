package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Targets_28920 extends SugarTest {
	FieldSet csvData = new FieldSet();

	public void setup() throws Exception {
		csvData = testData.get(testName).get(0);

		// 2 target records one with API & other with custom data
		FieldSet customData = new FieldSet();
		customData.put("firstName", csvData.get("first_name"));
		customData.put("lastName", csvData.get("last_name"));
		customData.put("fullName", csvData.get("full_name"));
		sugar().targets.api.create();
		sugar().targets.api.create(customData);

		sugar().login();
	}

	/**
	 * Verify that Mass Updates should use the Module Title
	 *
	 */
	@Test
	public void Targets_28920_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String qaUserName = sugar().users.getQAUser().get("userName");
		VoodooControl assignedUser = sugar().targets.recordView.getDetailField("assignedTo");

		// Mass update records with "assigned to = qauser" fields
		sugar().targets.navToListView();
		sugar().targets.listView.toggleSelectAll();

		// TODO: VOOD-1397
		sugar().targets.listView.openActionDropdown();
		sugar().targets.listView.massUpdate();

		// TODO: VOOD-1003
		//sugar().targets.massUpdate.getControl("massUpdateField02").set(csvData.get("assigned_to_label"));
		//sugar().targets.massUpdate.getControl("massUpdateValue02").set(qaUserName);
		new VoodooSelect("a", "css", "div[data-voodoo-name='"+ sugar().targets.moduleNamePlural +"'] .filter-body.clearfix .controls.filter-field a").set(csvData.get("assigned_to_label"));
		new VoodooSelect("a", "css", "div[data-voodoo-name='"+ sugar().targets.moduleNamePlural +"'] .filter-body.clearfix:nth-of-type(2) .controls.filter-value a").set(qaUserName);
		new VoodooControl("a", "css", "div[data-voodoo-name='"+ sugar().targets.moduleNamePlural +"'] .fld_update_button.massupdate a").click();

		// Verify module title on mass update overlay
		// TODO: VOOD-1003
		VoodooControl massupdateProgress = new VoodooControl("h3", "css", "div[data-voodoo-name=massupdate-progress] .modal.progress-modal .header h3");
		// waitForVisible with extra wait needed to check "mass update progress bar" module text
		massupdateProgress.waitForVisible(20000);
		massupdateProgress.assertEquals(csvData.get("mass_update_text"), true);
		VoodooUtils.waitForReady();

		// Verify records assigned to field updated accordingly
		sugar().targets.listView.clickRecord(1);
		sugar().targets.recordView.showMore();
		assignedUser.assertEquals(qaUserName, true);
		sugar().targets.recordView.gotoNextRecord();
		assignedUser.assertEquals(qaUserName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
