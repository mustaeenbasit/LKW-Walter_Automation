package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_26509 extends SugarTest {

	public void setup() throws Exception {				
		sugar().leads.api.create();
		sugar().login();
		
		// Set email settings in admin
		sugar().admin.setEmailServer(testData.get("env_email_setup").get(0));
	}

	/**
	 * TC 26509: "From" is populated correctly in Archived Email
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_26509_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		
		FieldSet myArchiveEmail = testData.get(testName).get(0);	
		
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Go to "My Dashboard" at RHS in History Dashlet
		// TODO: VOOD-798 - Lib support in create/verify Archive Email from History Dashlet	
		new VoodooControl("i", "css", ".record-cell .dropdown-toggle i").click();
		new VoodooControl("a", "css", ".record-cell .dropdown ul li a").click();
		VoodooUtils.waitForReady();
		VoodooControl historyActionBtn = new VoodooControl("a", "css", ".row-fluid.sortable:nth-child(2) .btn.dropdown-toggle");
		historyActionBtn.scrollIntoViewIfNeeded(true);
		
		// Clicking the 'Archive Email' option
		new VoodooControl("a", "css", "div.thumbnail.dashlet.ui-draggable a[data-dashletaction='archiveEmail']").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-797 - Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", "div.input-append i.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		new VoodooControl("input", "css", ".fld_from_address input").set(myArchiveEmail.get("from"));
		new VoodooControl("input", "css", ".fld_to_addresses input").set(myArchiveEmail.get("to"));
		new VoodooControl("div", "css", "div.select2-result-label").click();
		new VoodooControl("input", "css", ".fld_subject input").set(myArchiveEmail.get("subject"));
		new VoodooControl("a", "css", ".fld_archive_button a").click();

		StandardSubpanel emailSubPanel = sugar().leads.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubPanel.hover();
		emailSubPanel.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify the From field has correct value
		// TODO: VOOD-792 - Need lib support to handle or access email records
		new VoodooControl("slot", "css", "div.detail.view tr:nth-child(4) td:nth-child(2) slot").assertContains((myArchiveEmail.get("from")), true);
		
		// TODO: VOOD-791 - Need to have lib support for Delete Email record(s)
		new VoodooControl("span", "css", "span.ab").click();
		new VoodooControl("a", "css", "a#delete_button").click();
		VoodooUtils.dismissDialog();
		VoodooUtils.focusDefault();				

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}