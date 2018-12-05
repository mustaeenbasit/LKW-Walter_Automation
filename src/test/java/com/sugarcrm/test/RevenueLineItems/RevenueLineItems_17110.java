package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_17110 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().revLineItems.api.create();
		sugar().login();

		// Create role => RLI -> Likely Field (read only)
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'Revenue Line Items')]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "likely_caselink").click();
		new VoodooControl("select", "id", "flc_guidlikely_case").set(roleRecord.get("roleName"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Shouldn't show pencil icon next to the currency field if user doesn't have edit permission of the field
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17110_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);

		// TODO: VOOD-854
		// Verifying pencil edit is not available for likely case and read only for record view editview as well
		sugar().revLineItems.recordView.getDetailField("likelyCase").hover();
		new VoodooControl("span", "css", "div.record-cell[data-name=likely_case] span.record-edit-link-wrapper i.fa-pencil").assertVisible(false);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("likelyCase").assertExists(false);
		sugar().revLineItems.recordView.cancel();
		
		// Verify likely case is read only field while inline editing from listview
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.editRecord(1);
		sugar().revLineItems.listView.getEditField(1, "bestCase").scrollIntoView();
	
		// TODO: likely case value not displaying on control, however field is read only
		sugar().revLineItems.listView.getEditField(1, "likelyCase").assertExists(false);
		sugar().revLineItems.listView.cancelRecord(1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}