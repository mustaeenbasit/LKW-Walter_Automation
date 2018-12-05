package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23268 extends SugarTest {
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}
	
	/**
	 * Verify "Member of Organization" record is not populated in "Member of" field in it's parent's detailview
	 * @throws Exception
	 */
	@Test
	public void Accounts_23268_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		myAccount.navToRecord();
		StandardSubpanel accSub = new StandardSubpanel(sugar().accounts);
		accSub.addRecord();		
		sugar().accounts.createDrawer.getEditField("name").set(ds.get(0).get("name1"));
		sugar().contacts.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		myAccount.verify();
		accSub.editRecord(1);
		//VOOD-503
		new VoodooControl("input", "css", "span.fld_name.edit input").set(ds.get(0).get("name2"));
		accSub.saveAction(1);		
		myAccount.verify();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}