package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17141 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Display the save/cancel button as soon as the detail view inline edit action is triggered
	 * @throws Exception
	 */
	@Test
	public void Accounts_17141_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();

		// Verify Save button does not exist.
		sugar().accounts.recordView.getControl("saveButton").assertVisible(false);
		FieldSet customData = testData.get(testName).get(0);

		// Click a pencil edit on office phone field.
		new VoodooControl ("span", "css", "div.record-cell[data-name="+customData.get("name")+"]").hover();
		new VoodooControl ("a", "css", "a[data-name="+customData.get("name")+"]").click();

		// Just want to make sure the editbox appear.
		sugar().accounts.recordView.getEditField("workPhone").set(customData.get("value"));

		// Verify action button is displayed.
		sugar().accounts.recordView.getControl("saveButton").assertVisible(true);
		sugar().accounts.recordView.getControl("cancelButton").assertVisible(true);

		// Verify the edit button parent is not displayed
		new VoodooControl("span", "css", "div.headerpane span.actions.btn-group").assertVisible(false);

		// Verify the above element does contain edit button
		new VoodooControl("span", "css", "div.headerpane span.actions.btn-group span.fld_edit_button").assertVisible(false);
		sugar().accounts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}