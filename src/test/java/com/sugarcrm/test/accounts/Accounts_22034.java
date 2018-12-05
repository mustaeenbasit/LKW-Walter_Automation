package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Accounts_22034 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * User selects “Confirm” in pop-up confirm window
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22034_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		FieldSet customData = testData.get(testName).get(0);
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("name").set(customData.get("name"));
		sugar().accounts.recordView.getEditField("description").set(customData.get("description"));
		sugar().accounts.recordView.getEditField("website").set(customData.get("website"));

		// Verify Pop-up is appeared with message: "Warning You have unsaved changes." and buttons: Cancel and Confirm
		sugar().calls.navToListView();
		sugar().alerts.getWarning().assertVisible(true);
		sugar().alerts.getWarning().assertContains(customData.get("warning_message"), true);
		sugar().alerts.getWarning().getControl("cancelAlert").assertVisible(true);
		sugar().alerts.getWarning().confirmAlert(); // click event is already checking its visibility (Confirm button)

		// Verify Pop-up is disappeared, another page is opened.
		sugar().alerts.getWarning().assertVisible(false);
		sugar().calls.listView.getControl("moduleTitle").assertEquals(sugar().calls.moduleNamePlural, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}