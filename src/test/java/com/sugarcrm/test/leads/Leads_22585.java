package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22585 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Test Case 22585: Verify new action dropdown in lead record view.
	 * 
	 * @throws Exception
	 */

	@Test
	public void Leads_22585_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Go to a lead record view and open Action dropdown
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Open 'Actions' dropdown by clicking the down arrow beside Edit action
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-578,VOOD-695
		VoodooControl shareCtrl = new VoodooControl("a", "css", ".fld_share.detail a");
		VoodooControl convertCtrl =new VoodooControl("a", "css", ".fld_lead_convert_button.detail a");
		VoodooControl manageSubscriptionCtrl =new VoodooControl("a", "css", ".fld_manage_subscription_button.detail a");
		VoodooControl downloadVcardCtrl =new VoodooControl("a", "css", ".fld_vcard_button.detail a");
		VoodooControl duplicateCtrl =new VoodooControl("a", "css", ".fld_find_duplicates_button.detail a");
		VoodooControl summaryCtrl =new VoodooControl("a", "css", ".fld_historical_summary_button.detail a");
		VoodooControl logCtrl =new VoodooControl("a", "css", ".fld_audit_button.detail a");

		// Verify that all elements are available
		sugar().leads.recordView.getControl("editButton").assertEquals(customData.get("edit"), true);
		sugar().leads.recordView.getControl("copyButton").assertEquals(customData.get("copy"), true);
		sugar().leads.recordView.getControl("deleteButton").assertEquals(customData.get("delete"), true);
		shareCtrl.assertEquals(customData.get("share"), true);				
		convertCtrl.assertEquals(customData.get("convert"), true);
		manageSubscriptionCtrl.assertEquals(customData.get("manage_subscription"), true);
		downloadVcardCtrl.assertEquals(customData.get("download_vcard"), true);		 
		duplicateCtrl.assertEquals(customData.get("duplicate"), true);
		summaryCtrl.assertEquals(customData.get("summary"), true);
		logCtrl.assertEquals(customData.get("log"), true);	

		sugar().leads.recordView.getControl("editButton").assertExists(true);
		sugar().leads.recordView.getControl("copyButton").assertExists(true);
		sugar().leads.recordView.getControl("deleteButton").assertExists(true);
		shareCtrl.assertExists(true);				
		convertCtrl.assertExists(true);
		manageSubscriptionCtrl.assertExists(true);
		downloadVcardCtrl.assertExists(true); 
		duplicateCtrl.assertExists(true);
		summaryCtrl.assertExists(true);
		logCtrl.assertExists(true);

		// Click on 'Delete' item
		sugar().leads.recordView.getControl("deleteButton").click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify the lead was deleted.
		sugar().leads.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}