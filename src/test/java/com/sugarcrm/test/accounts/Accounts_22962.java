package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22962 extends SugarTest {
	StandardSubpanel memberOrganizationsSubPanel;

	public void setup() throws Exception {
		AccountRecord myAccountRecord1 = (AccountRecord) sugar().accounts.api.create();
		
		// Create second account with different name 
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		AccountRecord myAccountRecord2 = (AccountRecord) sugar().accounts.api.create(fs);
		sugar().login();
		
		// Existing account member related to an account record needed
		myAccountRecord2.navToRecord();
		memberOrganizationsSubPanel = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		memberOrganizationsSubPanel.linkExistingRecord(myAccountRecord1);
	}

	/**
	 * Account Detail - Member Organizations sub-panel - Edit _Verify that 
	 * account member record related to this account can be edited by clicking "edit" icon.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22962_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Already in recordView, edit sub-panel in-line record and update with new value
		FieldSet customData = testData.get(testName).get(0);
		memberOrganizationsSubPanel.editRecord(1, customData);
		
		// Verify that the modified account member information is displayed on "MEMBER ORGANIZATIONS" sub-panel.
		memberOrganizationsSubPanel.verify(1, customData, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}