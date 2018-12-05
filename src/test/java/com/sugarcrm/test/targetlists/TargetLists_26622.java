package com.sugarcrm.test.targetlists;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class TargetLists_26622 extends SugarTest {
	FieldSet customData;
	TargetRecord targetRecord;
	TargetListRecord targetListRecord;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		targetRecord = (TargetRecord) sugar.targets.api.create();
		targetListRecord = (TargetListRecord) sugar.targetlists.api.create();
		sugar.login();
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);

		// Setting the Target list 'type' to 'Seed'
		sugar.targetlists.recordView.edit();

		// TODO: VOOD-997: Value isn't set in Type field when sugar.targetlists.api.create(DataSource)
		sugar.targetlists.recordView.getEditField("listType").set(customData.get("listType"));
		sugar.targetlists.recordView.save();

		// Associate the target to the above target list
		sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural).linkExistingRecord(targetRecord);

		// takes care of green success message
		if(sugar.alerts.getSuccess().queryVisible())
			sugar.alerts.getSuccess().closeAlert(); 
	}

	/**
	 * Verifying copying target list without Name or duplicate name
	 * 
	 * @throws Exception
	 */
	@Test
	public void TargetLists_26622_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.targetlists.recordView.copy();

		// In the edit view, set the name of the target list record to null
		sugar.targetlists.createDrawer.getEditField("targetlistName").set("");
		sugar.targetlists.createDrawer.save();

		// verifying an error message is displayed upon saving a target list without name
		sugar.alerts.getError().assertVisible(true);
		sugar.alerts.getError().assertContains(customData.get("message"), true);
		sugar.alerts.getError().closeAlert();
		sugar.targetlists.createDrawer.cancel();

		// In the edit view, keeping the the name of the target list record as same as before and saving
		sugar.targetlists.recordView.copy();
		VoodooUtils.waitForReady();

		// Save and navigate to the record
		sugar.targetlists.createDrawer.save();

		// verifying that all the fields in the original target list record are appearing in the new target list record
		sugar.targetlists.recordView.getDetailField("targetlistName").assertEquals(targetListRecord.get("targetlistName"), true);
		sugar.targetlists.recordView.getDetailField("description").assertEquals(targetListRecord.get("description"), true);
		sugar.targetlists.recordView.getDetailField("listType").assertContains(customData.get("listType"), true);
		StandardSubpanel targetSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural);
		targetSubpanel.scrollIntoView();
		targetSubpanel.expandSubpanel();
		String fullname = targetRecord.get("firstName") + " " + targetRecord.get("lastName");

		// TODO: VOOD-1424 Once resolved, below line should be replaced by verify method 
		targetSubpanel.getDetailField(1, "fullName").assertContains(fullname, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}