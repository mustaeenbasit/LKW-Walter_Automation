package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30244 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		
		// Create a 'Draft' KB with past expiration date
		FieldSet kbData = new FieldSet ();
		kbData.put("date_expiration", customData.get("date_expiration"));
		sugar().knowledgeBase.api.create(kbData);
		
		// Login as admin
		sugar().login();
		
		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that correct messages appear in the Listview and Recordview
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30244_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB module
		sugar().knowledgeBase.navToListView();

		// Inline edit KB record
		sugar().knowledgeBase.listView.editRecord(1);
		
		// Change the status to Published and save
		sugar().knowledgeBase.listView.getEditField(1, "status").set(customData.get("newStatus"));
		sugar().knowledgeBase.listView.saveRecord(1);
		
		// Verify that a red color error message "The Expiration Date must occur on a date after the Publish Date."
		// TODO: VOOD-1819
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().assertEquals(customData.get("alertError1"), true);
		sugar().alerts.getError().closeAlert();
		
		// Cancel editing the record
		sugar().knowledgeBase.listView.cancelRecord(1);
		
		// Click open the KB record and edit it
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.edit();
		
		// Attempting to change status to 'Published' on record view and save
		sugar().knowledgeBase.recordView.getEditField("status").set(customData.get("newStatus"));
		sugar().knowledgeBase.recordView.save();
		
		// Verify that a red color error message "Please resolve any errors before proceeding"
		// TODO: VOOD-1819
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().assertContains(customData.get("alertError2"), true);
		sugar().alerts.getError().closeAlert();
		
		// Verify that the expiration date input box becomes red
		// TODO: VOOD-1755 Need support to assert error class containing input fields in SideCar record view
		VoodooControl expirationDate = new VoodooControl("span", "class", "fld_exp_date");
		expirationDate.scrollIntoViewIfNeeded(false);
		expirationDate.assertAttribute("class", "error", true);
		
		// Hover over the expiration date input box (i.e. the Exclamation circle)
		// TODO: VOOD-1292
		new VoodooControl("i", "css", ".fld_exp_date .fa-exclamation-circle").hover();
		VoodooUtils.waitForReady();
		
		// Verify that the message "The Expiration Date must occur on a date after the Publish Date." is displayed
		new VoodooControl("div", "css", ".tooltip.fade.top.in .tooltip-inner").assertEquals(customData.get("toolTipError"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}