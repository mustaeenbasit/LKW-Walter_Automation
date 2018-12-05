package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.sugar.views.RecordView;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Accounts_22035 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * User select “Cancel” when pop up confirm window
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22035_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		FieldSet customData = testData.get(testName).get(0);

		// Edit some fields in accounts record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		RecordView recordView = sugar().accounts.recordView;
		recordView.edit();
		recordView.showMore();
		VoodooControl nameEditField = recordView.getEditField("name");
		nameEditField.set(customData.get("name"));
		recordView.getEditField("description").set(customData.get("description"));
		recordView.getEditField("website").set(customData.get("website"));

		// Verify Pop-up is appeared with message: "Warning You have unsaved changes." and buttons: Cancel and Confirm
		sugar().meetings.navToListView();
		Alert warningAlert = sugar().alerts.getWarning();
		warningAlert.assertVisible(true);
		warningAlert.assertContains(customData.get("warning_message"), true);
		warningAlert.getControl("confirmAlert").assertVisible(true);
		warningAlert.cancelAlert(); // click event is already checking its visibility (Cancel button)

		// Verify Pop-up is disappeared and user stay on current page (Edit record view)
		warningAlert.assertVisible(false);
		recordView.assertVisible(true);
		nameEditField.assertVisible(true);
		recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}