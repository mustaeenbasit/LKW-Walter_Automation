package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24320 extends SugarTest {
	StandardSubpanel emailsSubpanel;
	FieldSet customFS = new FieldSet();
	
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);

		// History Dashlet already added in Opportunity > recordView
		// Go to Opportunities Record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		// Choose My DashBoard to display in module recordView
		VoodooControl dashboard = sugar.accounts.dashboard.getControl("dashboard");
		if(!dashboard.queryContains("My Dashboard", true))
			sugar.cases.dashboard.chooseDashboard("My Dashboard");
		
		// Create an Archive Email from History Dashlet.
		// TODO: VOOD-960
		VoodooControl historyDashLetCtrl = new VoodooControl("i", "css", "ul.dashlet-row li.row-fluid:nth-child(5) .dropdown-toggle.btn.btn-invisible .fa.fa-plus");
		historyDashLetCtrl.scrollIntoViewIfNeeded(false);
		historyDashLetCtrl.click();
		new VoodooControl("a", "css", "ul.dashlet-row li.row-fluid:nth-child(5) .dropdown-menu [data-dashletaction='archiveEmail']").click();
		VoodooUtils.waitForReady();
		
		// Fill composeEmail fields and save as Archive
		customFS = testData.get(testName).get(0);
		new VoodooControl("input", "css", ".layout_Emails.drawer.active .fld_from_address.edit input").set(customFS.get("fromAddress"));
		
		// Go to Addressbook and select ToAddress
		sugar().opportunities.recordView.composeEmail.getControl("addressBook").click();
				
		// TODO: VOOD-1423 -Need lib support for Opportunity > recordView > composeEmail > ToAddress > AddressBook	 
		VoodooControl checkbox = new VoodooControl("input", "css", ".flex-list-view tr:nth-child(1) td:nth-child(1) input[type='checkbox']");
		checkbox.waitForVisible();
		checkbox.click();
		new VoodooControl("a", "css", "a[name='done_button']").click();
		sugar().opportunities.recordView.composeEmail.getControl("subject").set(customFS.get("subject"));
		VoodooUtils.waitForReady();
		sugar().opportunities.recordView.composeEmail.addBodyMessage(customFS.get("body"));
		
		// Set date sent
		new VoodooControl("input", "css", ".fld_date_sent.edit input").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".detail.fld_archive_button a").click();
		VoodooUtils.waitForReady();

		// Verify that Archived email records exist in the Email sub-panel of an opportunity.
		emailsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailsSubpanel.scrollIntoViewIfNeeded(false);
		emailsSubpanel.assertContains(customFS.get("subject"), true);
		emailsSubpanel.assertContains(customFS.get("status"), true);
	}

	/**
	 * Verify that a new archived email record is displayed in "History" sub-panel after editing an archived email.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24320_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-960
		// Click Email record inside History dashlet -> Emails tab to open archived email message in the record view 
		new VoodooControl("a", "css", "[data-voodoo-name='history'] .dashlet-tabs-row div:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "[data-voodoo-name='history'] .tab-pane.active li p a:nth-child(2)").click();
		VoodooUtils.waitForReady();
		sugar().emails.detailView.edit();
		
		// TODO: VOOD-1028
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "subjectfield").set(testName);
		VoodooUtils.focusDefault();
		sugar().emails.editView.save();

		// Go back to Opportunities module -> Record view and go to Emails subpanel 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		// Verify that information in the subpanel is updated. 
		emailsSubpanel.scrollIntoViewIfNeeded(false);
		emailsSubpanel.assertContains(testName, true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}