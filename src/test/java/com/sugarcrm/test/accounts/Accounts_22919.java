package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Accounts_22919 extends SugarTest {
	StandardSubpanel memberOrganizationSubPanel;

	public void setup() throws Exception {
		// Create multiple account records.
		DataSource recordList = new DataSource();
		recordList.add(sugar().accounts.getDefaultData());
		FieldSet record = new FieldSet();
		record.put("name", testName);
		recordList.add(record);
		sugar().accounts.api.create(recordList);
		sugar().login();

		// Existing account member for the account needed.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(2);
		memberOrganizationSubPanel = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		memberOrganizationSubPanel.scrollIntoViewIfNeeded(false);
		AccountRecord acc = new AccountRecord(recordList.get(1));
		memberOrganizationSubPanel.linkExistingRecord(acc);
	}

	/**
	 * Account Detail - Member Organizations sub-panel_Verify that Member Organization detail information related to this account can be viewed by clicking "Account Name" link on "MEMBER ORGANIZATIONS" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22919_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		memberOrganizationSubPanel.scrollIntoViewIfNeeded(false);

		// Verify member account record view related to the account is displayed.
		memberOrganizationSubPanel.clickLink(testName, 1);
		sugar().accounts.recordView.getDetailField("name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}