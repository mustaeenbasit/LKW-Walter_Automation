package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_16928 extends SugarTest {	
	
	public void setup() throws Exception {
		sugar.login();		
	}
	
	/**
	 * Test Case 16928: Verify save record with empty email address item in edit view.
	 * @throws Exception
	 */
	@Test	
	public void Emails_16928_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		DataSource ds = testData.get(testName); 
		// Go to account edit view.
		sugar.accounts.navToListView();
		sugar.accounts.listView.create();
		sugar.accounts.createDrawer.showMore();
        
		// Populate the name field without filling email address and save
		sugar.accounts.createDrawer.getEditField("name").set(ds.get(0).get("name"));
		sugar.accounts.createDrawer.save();
		sugar.alerts.getSuccess().closeAlert();
        
		// Verify that email address field is blank
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.recordView.getDetailField("emailAddress").assertEquals(ds.get(0).get("emailaddress1"), true);
		
		// Edit record, fill out first email address field, add one more email address line but leave it blank
		sugar.accounts.recordView.edit();
		sugar.accounts.createDrawer.getEditField("emailAddress").set(ds.get(0).get("emailaddress2"));
		//TODO: VOOD-1005 - Add support for plus/minos buttons to add/remove second email address 
		new VoodooControl("class", "css", ".control-group.email .fa-plus").click();
		sugar.accounts.recordView.save();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that email address field is displaying the correct email now 
		sugar.accounts.recordView.getDetailField("emailAddress").assertContains(ds.get(0).get("emailaddress2"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}