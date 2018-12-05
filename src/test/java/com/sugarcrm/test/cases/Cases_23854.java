package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Cases_23854 extends SugarTest {
	ContactRecord myContact;
	StandardSubpanel casesSubpanel;
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myContact= (ContactRecord)sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Clear all entered search field by click “Clear” button on pop-up select window after search action
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23854_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contact Record View
		myContact.navToRecord();
		casesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().cases.moduleNamePlural);

		// Click “Create” button in Cases subpanel
		casesSubpanel.addRecord();
		// Show More
		sugar().cases.createDrawer.showMore();
		// TODO: VOOD-518 & VOOD-1161 need lib support for select team on cases module
		VoodooControl teamfieldCtrl = new VoodooControl("a", "css", ".fld_team_name.edit .select2-choice");
		teamfieldCtrl.click();
		// Enter search string in Team field
		VoodooControl searchCtrl = new VoodooControl("input", "css", "#select2-drop div input");
		searchCtrl.set(sugar().users.getQAUser().get("userName").substring(0, 1));
		sugar().alerts.waitForLoadingExpiration();

		// Verify the team has been found as soon as you typed the string
		VoodooControl searchResultCtrl = new VoodooControl("ul", "css", "#select2-drop [role='listbox']");
		searchResultCtrl.assertContains(sugar().users.getQAUser().get("userName"), true);

		searchResultCtrl.click();
		teamfieldCtrl.click();
		// Clear the Search String
		searchCtrl.set("");

		// Verify The team link was replaced with "Search and Select" when you cleared the search input field
		VoodooControl searchResultCtrl1 = new VoodooControl("div", "css", "#select2-drop ul:nth-child(3) li div");
		searchResultCtrl1.assertContains(customData.get("text"), true);
		searchResultCtrl1.click();
		sugar().alerts.waitForLoadingExpiration();
		sugar().teams.searchSelect.cancel();
		sugar().cases.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
