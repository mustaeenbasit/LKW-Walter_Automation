package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest; 

public class Emails_26510 extends SugarTest {
	LeadRecord myLeadData;
	FieldSet emailSetupData, customData;
	String noDataMessage = null;
	public void setup() throws Exception {
		emailSetupData = testData.get(testName+"_smtp_settings").get(0);	
		customData = testData.get(testName).get(0);	
		
		FieldSet fs = new FieldSet();
		fs.put("firstName", customData.get("first_name"));
		fs.put("lastName", customData.get("last_name"));
		myLeadData = (LeadRecord) sugar.leads.api.create(fs);
		sugar.login();
		
		// Set email settings in admin
		sugar.admin.setEmailServer(emailSetupData);
	}

	/**
	 * Verify "To" email address is auto-populated in Archived Email
	 * @throws Exception
	 */
	@Test
	public void Emails_26510_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		myLeadData.navToRecord();
		sugar.leads.recordView.edit();
		new VoodooControl("input", "css", ".newEmail.input-append").set(emailSetupData.get("userName"));
		sugar.leads.recordView.save();
		sugar.alerts.waitForLoadingExpiration();
		
		// TODO VOOD-963
		VoodooControl dashboardTitle = new VoodooControl("a", "css", "span.fld_name.detail a:nth-child(1)");
		if(dashboardTitle.getText().contains("Help Dashboard")) {
			dashboardTitle.click();
			VoodooUtils.waitForAlertExpiration();
			new VoodooControl("a", "css", "span.fld_name.detail ul.dropdown-menu li a").click();
			sugar.alerts.waitForLoadingExpiration();
		}

		// TODO: VOOD-798. Lib support in create/verify Archive Email from History Dashlet
		new VoodooControl("a", "css", "li.row-fluid.sortable:nth-child(2) span.btn-group.dashlet-toolbar a").waitForVisible();
		new VoodooControl("a", "css", "li.row-fluid.sortable:nth-child(2) span.btn-group.dashlet-toolbar a").click();
		new VoodooControl("a", "css", "div.thumbnail.dashlet.ui-draggable a[data-dashletaction='archiveEmail']").click();
		new VoodooControl("input", "css", ".fld_from_address input").waitForElement();
		
		// TODO: VOOD-797. Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", "div.input-append i.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		new VoodooControl("input", "css", ".fld_from_address input").waitForVisible();
		new VoodooControl("input", "css", ".fld_from_address input").set(customData.get("from"));
		
		// Verify "To" email address is auto-populated in Archived Email
		new VoodooControl("span", "css", "[data-title='"+emailSetupData.get("userName")+"']").assertContains(customData.get("first_name")+" "+customData.get("last_name"), true);
		new VoodooControl("input", "name", "subject").set(customData.get("subject"));
		VoodooUtils.focusFrame("mce_0_ifr");
		new VoodooControl("body", "id", "tinymce").set(customData.get("email_desc"));
		VoodooUtils.focusDefault();	
		new VoodooControl("a", "css", ".fld_archive_button a").click();
		sugar.alerts.waitForLoadingExpiration();

		// TODO: VOOD-814 - Lib support for handling My Dashboard->History Dashlet in recordview
		new VoodooControl("a", "css", "ul.dashlets li ul li:nth-child(2) .dashlet-tabs-row div:nth-child(2)").waitForElement();
		new VoodooControl("a", "css", "ul.dashlets li ul li:nth-child(2) .dashlet-tabs-row div:nth-child(2)").click();
		
		// Verify Archived Email "1" on History Dashlet
		new VoodooControl("span", "css", ".dashlets li ul li:nth-child(2) .dashlet-tabs-row div:nth-child(2) span.count").assertEquals(customData.get("one_archive"), true); 
		new VoodooControl("a", "css", ".dashlets li ul li:nth-child(2) .dashlet-tabs-row div:nth-child(2) a").click();
				
		// Delete Archived Email
		new VoodooControl("a", "css", "ul.dashlets li ul li:nth-child(2) .dashlet-tabs-row div:nth-child(2) a").click();
		new VoodooControl("a", "css", "ul.dashlets li ul li:nth-child(2) .tab-pane.active ul li p a:nth-child(2)").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// TODO: VOOD-791 - Need to have lib support for Delete Email record(s)
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", "#detail_header_action_menu span.ab").waitForElement();
		new VoodooControl("span", "css", "#detail_header_action_menu span.ab").click();
		new VoodooControl("a", "css", "a#delete_button").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		
		//Verify the archived email is deleted
		myLeadData.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
		
		// TODO:  VOOD-814 -Leads: Add support for Emails and Campaign Log subpanels.
		new VoodooControl("a", "css", "ul.dashlets li ul li:nth-child(2) .dashlet-tabs-row div:nth-child(2)").click();
		new VoodooControl("div", "css", "ul.dashlets li ul li:nth-child(2) .tab-pane.active").assertContains(customData.get("message"), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}
		
	public void cleanup() throws Exception {}
}
