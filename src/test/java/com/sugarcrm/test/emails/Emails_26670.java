package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_26670 extends SugarTest {
	FieldSet emailSettings;
	DataSource ds;
    LeadRecord myLead;
	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
		myLead = (LeadRecord) sugar.leads.api.create();
		// Configure outgoing email in admin panal
		emailSettings = new FieldSet();
		emailSettings.put("userName", "qa.user.suga@gmail.com");
		emailSettings.put("password", "Nerdapp123");
		emailSettings.put("allowAllUsers", "true");
		sugar.admin.setEmailServer(emailSettings);
		// logout and login again to allow emails setting to become effective 
		sugar.logout();
		sugar.login();
	}

	/**
	 * Sent status should appear in Emails sub panel and History dashlet.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_26670_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// navigate to lead record 
		myLead.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
		// TODO: VOOD-809 - Need Lib support for Emails sub panel in record view
		new VoodooControl("a", "css", ".fld_email_compose_button.small [name = 'email_compose_button']").click();
		// VOOD-843 - Need library support for email composer UI
		new VoodooControl("span", "css", "div.record > div:nth-child(1) span.select2-chosen").waitForVisible();
		sugar.alerts.waitForLoadingExpiration();  
		VoodooControl toFieldCtrl = new VoodooControl("input", "css", ".fld_to_addresses.edit > div > ul > li > input");
		toFieldCtrl.waitForVisible();
		toFieldCtrl.set(ds.get(0).get("to"));
		VoodooControl selectCtrl = new VoodooControl("div", "css", ".select2-result-label");
		selectCtrl.waitForVisible();
		selectCtrl.click();
		VoodooControl subjectCtrl = new VoodooControl("input", "css", "[name='subject']");
		subjectCtrl.waitForVisible();
		subjectCtrl.set(ds.get(0).get("subject"));
		VoodooUtils.focusFrame("mce_0_ifr");
		VoodooControl emailBodyCtrl= new VoodooControl("body", "id", "tinymce");
		emailBodyCtrl.waitForVisible();
		emailBodyCtrl.set(ds.get(0).get("body"));
		VoodooUtils.focusDefault();
		
		// click on send email button  
		new VoodooControl("span", "css", "[data-voodoo-name = 'send_button']").click();
		sugar.alerts.waitForLoadingExpiration();
		// assert status of the sent mail in email sub panel
		new VoodooControl("div", "css", "[data-original-title='Sent']").assertEquals("Sent", true);
		// navigate to Historical Summary page to check status of the send email 
		sugar.leads.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css" , "[name='historical_summary_button']").click();
		sugar.alerts.waitForLoadingExpiration();
		// assert status of the sent mail in Historical Summary 
		VoodooControl titleCtrl = new VoodooControl("div", "css", "[data-type='status']");
		titleCtrl.waitForVisible();
		titleCtrl.assertEquals("Sent", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
