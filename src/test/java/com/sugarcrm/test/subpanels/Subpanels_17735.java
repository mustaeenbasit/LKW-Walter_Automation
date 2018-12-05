package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17735 extends SugarTest {
	StandardSubpanel casesSubpanel;
	FieldSet filterData;

	public void setup() throws Exception {
		sugar.accounts.api.create();
		CaseRecord myCase = (CaseRecord) sugar.cases.api.create();
		filterData = testData.get(testName).get(0);
		sugar.login();

		// TODO: VOOD-1320 (Record created via UI due to VOOD-1320) 
		sugar.contacts.create();

		// Link case record to Contact record
		// TODO: VOOD-444
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		casesSubpanel = sugar.contacts.recordView.subpanels.get(sugar.cases.moduleNamePlural);
		casesSubpanel.linkExistingRecord(myCase);
	}

	/**
	 * Verify the subpanel row level action - Edit and Save
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17735_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contacts module
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);

		// Click Related drop down list and select one related module which has records in the sub panel
		sugar.contacts.recordView.setRelatedSubpanelFilter(sugar.cases.moduleNamePlural);

		// Click arrow down key next to preview icon on the record right side, click Edit and make changes to some fields
		casesSubpanel.editRecord(1);
		casesSubpanel.getEditField(1, "name").set(filterData.get("caseName"));
		casesSubpanel.getEditField(1, "relAccountName").set(sugar.accounts.getDefaultData().get("name")); // account is required field

		// Verify that Edit should make the entire row editable as inline editing
		casesSubpanel.getEditField(1, "status").assertExists(true);
		casesSubpanel.getEditField(1, "relAssignedTo").assertExists(true);

		// Click Save 
		casesSubpanel.saveAction(1);

		// Verify that the changes should be saved and updated successfully
		// TODO: VOOD-1424
		casesSubpanel.getDetailField(1, "name").assertEquals(filterData.get("caseName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}