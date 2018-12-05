package com.sugarcrm.test.contracts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;


public class Contracts_19852 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord) sugar.accounts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Contracts -  Time to Expire_Verify that the time to expire for a contract is  correct when the contract create time is after 20:00 o'clock
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19852_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		FieldSet contractsExpireTime = testData.get(testName).get(0);

		// Date before the current date and after the current date.
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DATE, 1);
		date = c1.getTime();
		String afterTheCurrentDate = sdf.format(date);
		c1.add(Calendar.DATE, -2);
		date = c1.getTime();
		String beforeTheCurrentDate = sdf.format(date);

		// Go to contracts module and click "Create Contract" link on navigation shortcuts.
		sugar.navbar.navToModule(sugar.contracts.moduleNamePlural);
		sugar.navbar.selectMenuItem(sugar.contracts, "createContract" );

		// Fill mandatory fields and set the contract start date before the current date and the contract end date after the current date
		VoodooUtils.focusFrame("bwc-frame");
		sugar.contracts.editView.getEditField("name").set(testName);
		sugar.contracts.editView.getEditField("date_start").set(beforeTheCurrentDate);
		sugar.contracts.editView.getEditField("date_end").set(afterTheCurrentDate);
		sugar.contracts.editView.getEditField("account_name").set(myAccount.getRecordIdentifier());
		VoodooUtils.focusDefault();

		// Click save button
		sugar.contracts.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the time to expire for the contract is correct
		sugar.contracts.detailView.getDetailField("contract_term").assertEquals(contractsExpireTime.get("timeToExpire"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}