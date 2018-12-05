package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22672 extends SugarTest {
	LeadRecord myLead;

	public void setup() throws Exception {	
		FieldSet emailSettings = testData.get("env_email_setup").get(0);
		myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSettings);
	}

	/**
	 * Edit Archived Mail_Verify that editing archived email related to a lead can be canceled.
	 * @throws Exception
	 */	
	@Test
	public void Leads_22672_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		FieldSet customFS = testData.get(testName).get(0);
		myLead.navToRecord();

		// TODO: VOOD-963 & VOOD-960
		VoodooControl dashboardTitle =  new VoodooControl("a", "css", "div.dashboard-pane > div > div > div > div > div > div > h1 > span > span > div > a");
		if(!dashboardTitle.queryContains(customFS.get("dashboard"), true)) {
			// sugar().dashboard.chooseDashboard("My Dashboard");
			new VoodooControl("a", "css", "[data-type='dashboardtitle'] > span > div > a").click();
			VoodooUtils.waitForReady();
			new VoodooControl("a", "css", "[data-type='dashboardtitle'] > span > div > ul > li").click();
			VoodooUtils.waitForReady();
		}

		// TODO: VOOD-798 Lib support in create/verify Archive Email from History Dashlet
		new VoodooControl("a", "css", "li.row-fluid.sortable:nth-child(2) span.btn-group.dashlet-toolbar a").click();
		new VoodooControl("a", "css", "div.thumbnail.dashlet.ui-draggable a[data-dashletaction='archiveEmail']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "div.input-append.date.datetime input[data-type='date']").assertAttribute("class", customFS.get("required_class_name"));
		new VoodooControl("input", "css", "span.edit input[name='from_address']").assertAttribute("class", customFS.get("required_class_name"));
		new VoodooControl("div", "css", ".fld_to_addresses.edit div.select2-container").assertAttribute("class", customFS.get("required_class_name"));
		new VoodooControl("input", "css", "input[name='subject']").assertAttribute("class", customFS.get("required_class_name"));

		// TODO: VOOD-797 Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", "div.input-append i.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		new VoodooControl("input", "css", ".fld_from_address input").set(customFS.get("from"));
		new VoodooControl("input", "css", ".fld_to_addresses input").set(customFS.get("to"));
		new VoodooControl("input", "css", "div.select2-result-label").click();
		new VoodooControl("input", "css", "input[name=subject]").set(customFS.get("message"));

		// Click on Archive button
		new VoodooControl("a", "css", ".fld_archive_button a").click();
		VoodooUtils.waitForReady();

		// Verify that archived email exist on email sub-panel 
		StandardSubpanel emailSub = sugar().leads.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSub.expandSubpanel();
		emailSub.assertContains(customFS.get("status"), true);

		// TODO: VOOD-814 -Lib support for handling My Dashboard->History Dashlet in recordview
		// Verify the one Archive email exist in Emails tab
		VoodooControl emailDashLet = new VoodooControl("a", "css", "ul.dashlet-row li.row-fluid:nth-child(2) ul.dashlet-cell div.dashlet-tab:nth-child(2) a");
		emailDashLet.click();
		VoodooControl verifyEmailCount = new VoodooControl("span", "css", "ul.dashlet-row li.row-fluid:nth-child(2) ul.dashlet-cell div.dashlet-tab:nth-child(2) span.count");
		verifyEmailCount.assertEquals(customFS.get("count"), true); 
		new VoodooControl("a", "css", "ul.dashlet-row li.row-fluid:nth-child(2) ul.dashlet-cell div.tab-pane.active p a:nth-of-type(2)").assertEquals(customFS.get("message"), true);
		VoodooControl clickArchivedEmail = new VoodooControl("a", "css", "ul.dashlet-row li.row-fluid:nth-child(2) ul.dashlet-cell div.tab-pane.active a:nth-child(2)");
		clickArchivedEmail.click();		
		VoodooUtils.focusFrame("bwc-frame");

		// Click on edit link
		new VoodooControl("a", "id", "edit_button").click();
		VoodooUtils.waitForReady();

		// Click cancel
		new VoodooControl("input", "id", "CANCEL_HEADER").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		myLead.navToRecord();

		// Verify that there is no new archived email record created in "History" sub-panel of the lead.
		emailDashLet.click();
		verifyEmailCount.assertEquals(customFS.get("count"), true);

		// TODO: VOOD-791 - Need to have lib support for Delete Email record(s)
		clickArchivedEmail.click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", "span.ab").click();
		new VoodooControl("a", "css", "a#delete_button").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();	

		// Verify that Archived email deleted
		myLead.navToRecord();
		emailDashLet.click();
		verifyEmailCount.assertEquals(customFS.get("no_record_count"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}