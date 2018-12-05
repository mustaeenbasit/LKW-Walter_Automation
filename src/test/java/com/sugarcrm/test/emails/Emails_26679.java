package com.sugarcrm.test.emails;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_26679 extends SugarTest {
	DataSource emailSetup;
	FieldSet customData;

	public void setup() throws Exception {
		sugar.login();
		emailSetup = testData.get(testName);
		customData = testData.get(testName+"_1").get(0);
		
		// configure Admin->Email Settings
		sugar.admin.setEmailServer(emailSetup.get(0));
		
		// Create leads and then edit and add primary email
		sugar.leads.api.create();
		sugar.leads.api.create();
		
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		sugar.leads.recordView.edit();
		new VoodooControl("input", "css", "div[data-name='email'] .fld_email.edit input").set(emailSetup.get(0).get("userName"));
		new VoodooControl("a", "css", "div[data-name='email'] .fld_email.edit a.btn.addEmail").click();
		sugar.leads.recordView.save();
		VoodooUtils.waitForAlertExpiration();
		
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(2);
		sugar.leads.recordView.edit();
		new VoodooControl("input", "css", "div[data-name='email'] .fld_email.edit input").set(emailSetup.get(0).get("userName"));
		new VoodooControl("a", "css", "div[data-name='email'] .fld_email.edit a.btn.addEmail").click();		
		sugar.leads.recordView.save();
		VoodooUtils.waitForAlertExpiration();
		
		VoodooUtils.focusDefault();
	}

	/**
	 * Select multiple recipients to compose an email from listview
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore("MAR-1637")
	public void Emails_26679_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.leads.navToListView();
		sugar.leads.listView.toggleSelectAll();
		sugar.leads.listView.openActionDropdown();
		// TODO: VOOD-938
		new VoodooControl("a", "css", ".dropdown-menu a[name='mass_email_button']").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("input", "css", "input[name='subject']").set(customData.get("subject"));
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("a", "css", "div.btn-toolbar.pull-right > span.actions.btn-group.detail > a").click();		
		new VoodooControl("a", "css", "a.rowaction[name='draft_button']").click();
		VoodooUtils.waitForAlertExpiration();
		
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);		
		// assert in sub-panel
		//TODO: VOOD-809 Lib support for Emails sub panel in record view
		new VoodooControl("a", "css", "div.layout_Emails td:nth-child(2) div a").assertContains(customData.get("subject"), true);
		new VoodooControl("a", "css", "div.layout_Emails td:nth-child(3) div ").assertContains(customData.get("status"), true);
			
		// assert in sub-panel
		//TODO: VOOD-809 Lib support for Emails sub panel in record view
		new VoodooControl("button", "css", "div.headerpane button.btn.btn-invisible.next-row").click();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("a", "css", "div.layout_Emails td:nth-child(2) div a").assertContains(customData.get("subject"), true);
		new VoodooControl("a", "css", "div.layout_Emails td:nth-child(3) div ").assertContains(customData.get("status"), true);
				
		// assert into Emails->My Sent Email folder
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("span", "css", "#emailtree > div > div > div > table > tbody > tr > td.ygtvcell.ygtvcontent > span").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("td", "css", "#emailtree div div div div div:nth-child(1) table tbody tr td.ygtvcell.ygtvcontent").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("a", "css", "tbody.yui-dt-data > tr > td.yui-dt-col-subject.yui-dt-sortable.yui-dt-resizeable > div").assertContains(customData.get("subject"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}	
}
