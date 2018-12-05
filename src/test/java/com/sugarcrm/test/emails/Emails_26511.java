package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest; 

public class Emails_26511 extends SugarTest {
	LeadRecord myLead;
	FieldSet myArchiveEmail, emailSettings;

	String placeHolder = null, hoursPlaceHolder = null, archived = null, noDataMessage = null, dataFormatPlaceHolder = null;

	public void setup() throws Exception {				
		myArchiveEmail = testData.get(testName).get(0);
		emailSettings = testData.get(testName+"_smtp_settings").get(0);	
		noDataMessage = myArchiveEmail.get("message");
		archived = myArchiveEmail.get("status");
		myLead = (LeadRecord) sugar.leads.api.create();
		sugar.login();
		
		// Set email settings in admin
		sugar.admin.setEmailServer(emailSettings);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that "Archive Email" action in History Dashlet
	 * @throws Exception
	 */
	@Test
	public void Emails_26511_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		myLead.navToRecord();		
		sugar.alerts.waitForLoadingExpiration();
		
		// TODO: VOOD-963 & VOOD-960
		VoodooControl dashboardTitle =  new VoodooControl("a", "css", "div.dashboard-pane > div > div > div > div > div > div > h1 > span:nth-child(1) > span > div > a");
		VoodooUtils.voodoo.log.info(">>>>:"+dashboardTitle.getText()+">>");
		if(!dashboardTitle.getText().contains("My Dashboard")) {
			//sugar.dashboard.chooseDashboard("My Dashboard");
			new VoodooControl("a", "css", "[data-type='dashboardtitle'] > span > div > a").click();
			VoodooUtils.waitForAlertExpiration();
			VoodooUtils.pause(8000);// Required to populate more dashboard list
			new VoodooControl("a", "css", "[data-type='dashboardtitle'] > span > div > ul > li").click();
			sugar.alerts.waitForLoadingExpiration();
		}
				
		// TODO: VOOD-798. Lib support in create/verify Archive Email from History Dashlet
		new VoodooControl("a", "css", "li.row-fluid.sortable:nth-child(2) span.btn-group.dashlet-toolbar a").click();
		new VoodooControl("a", "css", "div.thumbnail.dashlet.ui-draggable a[data-dashletaction='archiveEmail']").click();
		new VoodooControl("input", "css", ".fld_from_address input").waitForVisible();
		new VoodooControl("input", "css", "div.input-append.date.datetime input[data-type='date']").assertAttribute("class", "required");
		new VoodooControl("input", "css", "span.edit input[name='from_address']").assertAttribute("class", "required");
		new VoodooControl("input", "css", "input.select2.required.select2-offscreen").assertAttribute("class", "required");
		new VoodooControl("input", "css", "input[name='subject']").assertAttribute("class", "required");
		
		// TODO: VOOD-797 -Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", "div.input-append i.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		new VoodooControl("input", "css", ".fld_from_address input").set(myArchiveEmail.get("from"));
		new VoodooControl("input", "css", ".fld_to_addresses input").set(myArchiveEmail.get("to"));
		new VoodooControl("input", "css", "div.select2-result-label").click();
		new VoodooControl("input", "css", "input[name=subject]").set(myArchiveEmail.get("message"));
		
		new VoodooControl("a", "css", ".fld_archive_button a").click();

		//Verify that archived email exist on email subpanel 
		myLead.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
		
	    StandardSubpanel emailSub = sugar.leads.recordView.subpanels.get(sugar.emails.moduleNamePlural);
	    emailSub.expandSubpanel();
	    emailSub.assertContains("Archived", true);		
	    
		// TODO: VOOD-814 - Lib support for handling My Dashboard->History Dashlet in recordview
		//Verify the Archive email increase 1 count in Emails tab
		new VoodooControl("a", "css", "ul.dashlet-row li.row-fluid:nth-child(2) ul.dashlet-cell div.dashlet-tab:nth-child(2) a").click();
		new VoodooControl("span", "css", "ul.dashlet-row li.row-fluid:nth-child(2) ul.dashlet-cell div.dashlet-tab:nth-child(2) span.count").assertEquals("1", true); 
		new VoodooControl("a", "css", "ul.dashlet-row li.row-fluid:nth-child(2) ul.dashlet-cell div.tab-pane.active span.label").assertEquals(archived, true);
		new VoodooControl("a", "css", "ul.dashlet-row li.row-fluid:nth-child(2) ul.dashlet-cell div.tab-pane.active a:nth-child(2)").click();		
		new VoodooControl("iframe", "css", "#bwc-frame").waitForVisible();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-791 - Need to have lib support for Delete Email record(s)
		new VoodooControl("span", "css", "span.ab").click();
		new VoodooControl("a", "css", "a#delete_button").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();		
		
		//Verify that archived email is deleted on My Dashboard
		myLead.navToRecord();
		
		// TODO: VOOD-814 - Lib support for handling My Dashboard->History Dashlet in recordview
		new VoodooControl("div", "css", "div.filtered.layout_Emails i.fa-chevron-up").click();
		new VoodooControl("div", "css", "div.filtered.layout_Emails div.flex-list-view.right-actions").assertContains(noDataMessage, true);
		new VoodooControl("div", "css", "div.filtered.layout_Emails i.fa-chevron-up").click();
	
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}
		
	public void cleanup() throws Exception {}
}
