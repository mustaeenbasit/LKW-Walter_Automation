package com.sugarcrm.test.targetlists;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;


public class TargetLists_26426 extends SugarTest {
	TargetListRecord TL;
	AccountRecord account;
	
	public void setup() throws Exception {
		account = (AccountRecord) sugar.accounts.api.create();
		TL = (TargetListRecord) sugar.targetlists.api.create();
		sugar.login();
	}

	/**
	 * Verify no blank record created after adding records to targetlist
	 * @author Oliver Yang
	 * @throws Exception
	 */
	@Test
	public void TargetLists_26426_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.accounts.navToListView();
		sugar.accounts.listView.checkRecord(1);

		// TODO VOOD-528 hasn't been merged
		sugar.accounts.listView.openActionDropdown();
		new VoodooControl("a", "css", ".fld_addtolist_button a").click();
		new VoodooControl("a", "css", "span.fld_update_button a.btn").waitForVisible();

		new VoodooSelect("span", "css", ".fld_prospect_lists_name.edit a").set(TL.getRecordIdentifier());
		new VoodooControl("a", "css", "span.fld_update_button a.btn").click();
		sugar.alerts.getSuccess().closeAlert();		

		// Assert there is no new account record created
		sugar.accounts.listView.getControl(String.format("link%02d", 2)).assertExists(false);

		// Assert targetlist has been related to correct account record
		TL.navToRecord();
		FieldSet acc = new FieldSet();
		acc.put("name", account.getRecordIdentifier());
		StandardSubpanel accSub = sugar.targetlists.recordView.subpanels.get("Accounts");
		accSub.scrollIntoViewIfNeeded(false);
		accSub.expandSubpanel();
		accSub.scrollIntoViewIfNeeded(false);
		accSub.verify(1, acc, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}