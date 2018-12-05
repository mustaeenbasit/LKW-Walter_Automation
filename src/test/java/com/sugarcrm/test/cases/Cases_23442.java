package com.sugarcrm.test.cases;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23442 extends SugarTest {
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		FieldSet emailSetupData = testData.get("env_email_setup").get(0);
		sugar().cases.api.create();
		sugar().login();
		
		// Set up outbound email account
		sugar().admin.setEmailServer(emailSetupData);
	}

	/**
	 * No notice is displayed on the view summary window after an email with invalid receiver sent
	 * @throws Exception
	 */
	@Test
	public void Cases_23442_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		customData = testData.get(testName).get(0);		
		
		// Go to Emails -> Compose
		sugar().navbar.navToModule(sugar().emails.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-843 Need lib support to handle new email composer UI
		new VoodooControl("button", "id", "composeButton").click(); // Click on compose mail button
		VoodooUtils.waitForReady();
		
		// Choose related module 'Cases' and a created Case record 
		new VoodooControl("select", "id", "data_parent_type1").set(sugar().cases.moduleNameSingular);
		new VoodooControl("input", "id", "data_parent_name1").set(sugar().cases.getDefaultData().get("name"));
		VoodooUtils.waitForReady();
		
		// Set invalid receiver and send an email
		new VoodooControl("input", "id", "addressTO1").set(customData.get("mail_to"));
		new VoodooControl("input","id","emailSubject1").set(customData.get("subject"));
		VoodooUtils.focusFrame("htmleditor1_ifr"); // focus email body iframe
		new VoodooControl("body", "id", "tinymce").set(customData.get("body"));
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "css", "#composeHeaderTable1 tr:nth-child(1) td:nth-child(1) button:nth-child(1)").click();
		VoodooUtils.focusDefault();
		VoodooUtils.pause(5000); // Pause needed to send email
		
		// Go to Record view of the created Case
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		
		StandardSubpanel emailSubpanel = sugar().cases.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		StandardSubpanel KBSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		
		// Check Emails sub panel and click email subject link
		emailSubpanel.expandSubpanel();
		KBSubpanel.scrollIntoViewIfNeeded(false);
		emailSubpanel.clickLink(customData.get("subject"), 1);
		VoodooUtils.waitForReady();
		
		// Verify that No notice is displayed.
		Assert.assertFalse("Notice is displayed", VoodooUtils.isDialogVisible());
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
