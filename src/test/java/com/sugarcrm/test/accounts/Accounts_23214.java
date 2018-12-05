package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23214 extends SugarTest {
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}
	
	/**
	 * Verify that an account related to this account is created by in-line creating from "MEMBER ORGANIZATIONS" sub-panel
	 * @throws Exception
	 */
	@Test
	public void Accounts_23214_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		myAccount.navToRecord();
		StandardSubpanel accSub = sugar().accounts.recordView.subpanels.get("Accounts");
		accSub.addRecord();		
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("name").set(ds.get(0).get("name"));
		sugar().accounts.createDrawer.getEditField("billingAddressCity").set(ds.get(0).get("billingAddressCity"));
		sugar().accounts.createDrawer.getEditField("workPhone").set(ds.get(0).get("workPhone"));
		sugar().contacts.createDrawer.save();
		VoodooUtils.waitForAlertExpiration();
		//VOOD-609
		new VoodooControl("span", "css", "div.filtered.layout_Accounts tbody span.fld_name.list").assertEquals(ds.get(0).get("name"), true);
		new VoodooControl("span", "css", "div.filtered.layout_Accounts tbody span.fld_billing_address_city.list").assertEquals(ds.get(0).get("billingAddressCity"), true);
		new VoodooControl("span", "css", "div.filtered.layout_Accounts tbody span.fld_phone_office.list").assertEquals(ds.get(0).get("workPhone"), true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}