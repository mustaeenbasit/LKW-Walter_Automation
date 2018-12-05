package com.sugarcrm.test.accounts;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Accounts_22969 extends SugarTest {
	AccountRecord myAccount;
	StandardSubpanel caseSub;
	VoodooControl caseRow;
	
	public void setup() throws Exception {
		// TODO VOOD-756 Add method to standard and BWC subpanel object to verify if record is present or not present in the sub-panel
		caseRow = new VoodooControl("div", "css", "div[data-voodoo-name='Cases'] td[data-type='name'] div");

		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}
	
	/**
	 * Verify that a new case is created from "CASES" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Accounts_22969_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();
		caseSub = sugar().accounts.recordView.subpanels.get("Cases");
		caseSub.addRecord();

		DataSource ds = testData.get(testName);
		sugar().cases.createDrawer.getEditField("name").set(ds.get(0).get("name"));
		sugar().cases.createDrawer.save();

		caseRow.assertEquals(ds.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}