package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_28878 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that Hint text is displayed in Teams field
	 * @throws Exception
	 */
	@Test
	public void Accounts_28878_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();
		// Click on "+" to add team
		// TODO: VOOD-1005
		new VoodooControl("button", "css", ".fld_team_name.edit button.btn.first").click();

		// Verify Hint text is displayed as "Select team..." in the newly added dropdown field.
		new VoodooSelect("span", "css", ".fld_team_name.edit div:nth-child(2) a span.select2-chosen").assertContains(customData.get("hintText"), true);
		// Cancel
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}