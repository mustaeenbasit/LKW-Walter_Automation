package com.sugarcrm.test.RevenueLineItems;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18031 extends SugarTest {

	public void setup() throws Exception {
		// create an account record
		sugar().accounts.api.create();
		sugar().login();
		// create opportunities record via ui to link Account and rli record
		sugar().opportunities.create();
		sugar().logout();
	}

	/**
	 * Verify that Expected Close Date field is present and can be updated in RLI list view 
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18031_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String currDate = sdf.format(dt);

		// login as a qauser
		sugar().login(sugar().users.getQAUser());

		// Go to Revenue Line Items list view
		sugar().revLineItems.navToListView();
		VoodooControl dateCloseCtrl = sugar().revLineItems.listView.getDetailField(1, "date_closed");
		dateCloseCtrl.scrollHorizontallyHome();

		// Verify Expected close date field is in the list of the available fields in the list view.
		dateCloseCtrl.assertEquals(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"), true);

		// Click edit button to edit RLI record in the list view 
		sugar().revLineItems.listView.editRecord(1);
		// Change Expected Close Date and click Save.
		sugar().revLineItems.listView.getEditField(1, "date_closed").set(currDate);
		// save the record
		sugar().revLineItems.listView.saveRecord(1);
		dateCloseCtrl.scrollHorizontallyHome();
		// Verify that Date is updated successfully
		dateCloseCtrl.assertEquals(currDate, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}