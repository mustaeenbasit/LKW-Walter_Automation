package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_29026 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the correct timing should appear in View Change Log of Accounts
	 * module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_29026_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.openPrimaryButtonDropdown();

		// Verifying Change Log Drawer is Empty
		// TODO: VOOD-695,738
		VoodooControl viewChangeLogCtrl = new VoodooControl("a", "css",
				"[name='audit_button']");
		viewChangeLogCtrl.click();
		
		VoodooUtils.waitForReady();
		
		new VoodooControl("em", "css", ".list-view .block-footer em").assertEquals("No data available.", true);
		VoodooControl closeChangeViewLog = new VoodooControl("a", "css", ".fld_close_button a");
		closeChangeViewLog.click();
		sugar().accounts.recordView.edit();

		// TODO: VOOD-1005
		sugar().accounts.recordView.showMore();
		new VoodooSelect("a", "css", ".fld_team_name.edit .select2-choice")
		.set(sugar().users.getQAUser().get("userName"));
		sugar().accounts.recordView.save();
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		viewChangeLogCtrl.click();

		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1582
		String changeDateRow1 = new VoodooControl("div", "css",
				".layout_Audit table tr.single:nth-of-type(1) .fld_date_created div")
		.getText();
		String changeDateRow2 = new VoodooControl("div", "css",
				".layout_Audit table tr.single:nth-of-type(2) .fld_date_created div")
		.getText();

		// Verifying Changed time in both the Rows are Equal
		Assert.assertTrue("Changed Time are not equal: "+changeDateRow1+" and "+changeDateRow2,
				changeDateRow1.equals(changeDateRow2));
		closeChangeViewLog.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}