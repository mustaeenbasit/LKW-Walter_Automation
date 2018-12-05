package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_28288 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().leads.api.create();
		sugar().login();

		// An Opportunity record exist with associated Account record associated, therefore created Opportunity record from UI
		sugar().opportunities.create();
	}

	/**
	 * Verify that Opportunity duplicates is reset when resetting Account and creating a new one
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_28288_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to lead record
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// Click to convert the Lead
		// TODO: VOOD-695
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// Define Controls
		// TODO: VOOD-585
		VoodooControl accountVoodooNameCtrl = new VoodooControl("div", "css", "div[data-module='" + sugar().accounts.moduleNamePlural + "']");
		VoodooControl accountNameCtrl = new VoodooControl("input", "css", accountVoodooNameCtrl.getHookString() + " .fld_name.edit input");
		VoodooControl createAccountBtnCtrl = new VoodooControl("a", "css", accountVoodooNameCtrl.getHookString() + " a[name='associate_button']");

		// Verify that the User will be directly moved to Account pane
		accountNameCtrl.assertVisible(true);
		accountNameCtrl.assertContains("", true);

		// In Account pane, click on Search link, search for the account record
		// TODO: VOOD-585
		new VoodooControl("a", "css", accountVoodooNameCtrl.getHookString() + " .toggle-link.create").click();
		new VoodooControl("input", "css", accountVoodooNameCtrl.getHookString() + " .search-name").set(sugar().accounts.getDefaultData().get("name").substring(0, 4));
		VoodooUtils.waitForReady();

		// Verify that the first account will be selected by default or select another account from the list
		Assert.assertTrue(new VoodooControl("input", "css", "input[name='Accounts_select']").isChecked());

		// Click 'Select Account' button after selecting any of the account
		createAccountBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the user must see Opportunities associated with selected account in the Opportunity pane
		new VoodooControl("div", "css", "div[data-module='" + sugar().opportunities.moduleNamePlural + "'] .list.fld_name div").assertEquals(sugar().opportunities.getDefaultData().get("name"), true);

		// Click 'Reset' link of account pane and click 'Ignore and Create New' link
		new VoodooControl("a", "css", accountVoodooNameCtrl.getHookString() + " a[name='reset_button']").click();
		new VoodooControl("a", "css", accountVoodooNameCtrl.getHookString() + " .toggle-link.dupecheck").click();

		// Give any Account name, better is unique
		accountNameCtrl.set(testName);

		// Click 'Create Account' button after that
		createAccountBtnCtrl.click();

		// Verify that the User must see a blank Opportunity pane instead of any opportunities listing up
		new VoodooControl("div", "css", "div[data-module='" + sugar().opportunities.moduleNamePlural + "'] .fld_name.edit input").assertContains("", true);

		// Click on 'Save and Convert' button
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}