package com.sugarcrm.test.contracts;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19874 extends SugarTest {
	DataSource changeLogData;

	public void setup() throws Exception {
		changeLogData = testData.get(testName);
		FieldSet multiPurpose = new FieldSet();
		multiPurpose.put("name", changeLogData.get(2).get("newValue"));
		sugar.accounts.api.create(multiPurpose);
		multiPurpose.clear();
		sugar.accounts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);

		// Create a Contract record with custome data
		multiPurpose.put("date_start", changeLogData.get(3).get("oldValue"));
		multiPurpose.put("date_end", changeLogData.get(4).get("oldValue"));

		// TODO: VOOD-444
		sugar.contracts.create(multiPurpose);
	}

	/**
	 * Contracts Detail - Verify that the changes for contract records can be logged correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19874_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Click "Edit" button on detail view page
		sugar.contracts.detailView.edit();

		// Change the values for the following fields: End Date, Account Name, Start Date, Member Of, Team Id, Status, Assigned User Id
		String qauser = sugar.users.getQAUser().get("userName");
		VoodooUtils.focusFrame("bwc-frame");
		sugar.contracts.editView.getEditField("date_start").set(changeLogData.get(3).get("newValue"));
		sugar.contracts.editView.getEditField("date_end").set(changeLogData.get(4).get("newValue"));
		sugar.contracts.editView.getEditField("account_name").set(changeLogData.get(2).get("newValue"));
		sugar.contracts.editView.getEditField("teams").set(qauser);
		sugar.contracts.editView.getEditField("assignedTo").set(qauser);
		sugar.contracts.editView.getEditField("status").set(changeLogData.get(5).get("newValue"));
		VoodooUtils.focusDefault();

		// Click save button
		sugar.contracts.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Click "View Change Log" button
		// TODO: VOOD-738, 691, 578, VOOD-695
		sugar.contracts.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "id", "btn_view_change_log").click();
		VoodooUtils.focusWindow(1);

		// Verify that the changes logs are added with old value and new value in the pop-up window for Change Log.
		// Using XPath because order is not fix for the fields in change log window
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat updatedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for(int i = 0; i < changeLogData.size(); i++) {
			VoodooControl changesLogsRowCtrl = new VoodooControl("span", "XPATH", "/html/body/table/tbody/tr/td/table[2]/tbody/tr[contains(.,'" + changeLogData.get(i).get("field") + "')]");

			// TODO: TR-8849 : Change Log window is still showing default date format even after date format is changed in Locale Settings for BWC modules
			// Remove Line# 77-85 when TR-8849 will resolved and use only Line# 85 and 86 in for loop
			if (i == 3 || i == 4) {
				Date oldDate = sdf.parse(changeLogData.get(i).get("oldValue"));
				Date newDate = sdf.parse(changeLogData.get(i).get("newValue"));
				String oldDateString = updatedDateFormat.format(oldDate);
				String newDateString = updatedDateFormat.format(newDate);
				changesLogsRowCtrl.assertContains(oldDateString, true);
				changesLogsRowCtrl.assertContains(newDateString, true);
			} else {
				changesLogsRowCtrl.assertContains(changeLogData.get(i).get("oldValue"), true);
				changesLogsRowCtrl.assertContains(changeLogData.get(i).get("newValue"), true);
			}
		}
		VoodooUtils.closeWindow();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}