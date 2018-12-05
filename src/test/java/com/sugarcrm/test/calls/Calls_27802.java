package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_27802 extends SugarTest {
	FieldSet fs;
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar.calls.api.create();
		sugar.contacts.api.create();
		sugar.leads.api.create();
		sugar.opportunities.api.create();
		sugar.login();
	}

	/**
	 * Verify that Guest field displays correctly from preview for Calls
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_27802_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verifying for Calls
		// Creating a call that has at least 1 Contact, 1 Lead and 1 User, also make it Related To an Opportunity record.
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();

		// Selecting contact
		sugar.calls.createDrawer.getEditField("relatedToParentType").set(sugar.contacts.moduleNameSingular);
		sugar.calls.createDrawer.getEditField("relatedToParentName").set(sugar.contacts.getDefaultData().get("lastName"));  

		// Selecting Lead
		sugar.calls.recordView.getEditField("relatedToParentType").set(sugar.leads.moduleNameSingular);
		sugar.calls.recordView.getEditField("relatedToParentName").set(sugar.leads.getDefaultData().get("lastName"));

		// Relating meeting to an opportunity record
		sugar.calls.recordView.getEditField("relatedToParentType").set(sugar.opportunities.moduleNameSingular);
		sugar.calls.recordView.getEditField("relatedToParentName").set(sugar.opportunities.getDefaultData().get("name"));

		sugar.calls.recordView.save();

		sugar.calls.navToListView();

		// Preview the call from listview.
		sugar.calls.listView.previewRecord(1);

		// Verify that Contact is in Guests field.
		// TODO: VOOD-1223 Need library support to get records in Guests list in Meetings record view
		new VoodooControl("div", "css", ".participants").assertContains(sugar.contacts.getDefaultData().get("lastName"), true);

		// Verify that lead is in Guests field.
		new VoodooControl("div", "css", ".participants").assertContains(sugar.leads.getDefaultData().get("lastName"), true);

		// Verify that user is in Guests field.
		new VoodooControl("div", "css", ".participants").assertContains(fs.get("user"), true);

		// Go to opportunity record
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.clickRecord(1);

		// Preview the meeting from the meeting subpanel in opportunity record view
		sugar.opportunities.recordView.subpanels.get("Calls").clickPreview(1);

		// Verify that Contact is in Guests field.
		// TODO: VOOD-1223 Need library support to get records in Guests list in Meetings record view
		new VoodooControl("div", "css", ".participants").assertContains(sugar.contacts.getDefaultData().get("lastName"), true);

		// Verify that lead is in Guests field.
		new VoodooControl("div", "css", ".participants").assertContains(sugar.leads.getDefaultData().get("lastName"), true);

		// Verify that user is in Guests field.
		new VoodooControl("div", "css", ".participants").assertContains(fs.get("user"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
