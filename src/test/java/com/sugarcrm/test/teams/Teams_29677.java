package com.sugarcrm.test.teams;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_29677 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Teams section is working fine while adding more teams from "Records View" of any sidecar module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_29677_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to Accounts record view -> Click on Show more link
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();

		// Add one more Team in Teams section and click on "Save" button
		// TODO: VOOD-518
		sugar().accounts.recordView.edit();
		new VoodooControl("button", "css", ".fld_team_name button[name='add']").click();
		new VoodooSelect("a", "css", "span[data-voodoo-name='team_name'] div.control-group:nth-child(2) a.select2-choice").set(sugar().users.getQAUser().get("userName"));
		sugar().accounts.recordView.save();

		// Click on Teams section
		// TODO: VOOD-854
		new VoodooControl("span", "css", "span[data-voodoo-name='team_name']").hover();
		new VoodooControl("i", "css", "span[data-name='team_name'] .fa.fa-pencil").click();

		// Verify that By default, Only one Search bar should appear in Teams section and cursor should blink gently in the search text-box
		Assert.assertTrue("More than one Search bars are appearing" , new VoodooControl("div", "css", ".select2-drop-active").countWithClass() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}