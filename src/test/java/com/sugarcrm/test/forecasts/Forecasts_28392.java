package com.sugarcrm.test.forecasts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_28392 extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {		
		sugar().login();
		roleRecord = testData.get(testName).get(0);

		OpportunityRecord myOpp;
		RevLineItemRecord myRLI;
		AccountRecord myAcc;
		
		VoodooControl rliCtrl, setAction, saveButton, bestCaseCtrl; 
		myAcc = (AccountRecord) sugar().accounts.api.create();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myRLI = (RevLineItemRecord) sugar().revLineItems.api.create();
		
		// Enable default Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		VoodooUtils.waitForReady();

		// Go to admin -> Roles and create a new role
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580, VOOD-856
		rliCtrl = new VoodooControl("a", "xpath", "//table[@class='edit view']//td[.//a[contains(.,'Revenue Line Items')]]/a");
		bestCaseCtrl = new VoodooControl("div", "css", "div#best_caselink");
		setAction = new VoodooControl("select", "css", "select#flc_guidbest_case");
		saveButton = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		
		// Set Best Case field to "None" and Save
		rliCtrl.click();
		VoodooUtils.waitForReady();
		bestCaseCtrl.click();
		setAction.set("None");
		saveButton.click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();

		VoodooUtils.focusDefault();

		// Link myAcc to myOpp
		FieldSet fs = new FieldSet();
		fs.put("relAccountName", myAcc.getRecordIdentifier());
		fs.put("date_closed", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		myOpp.edit(fs);
		
		// Link myOpp, qauser to myRLI
		fs.clear();
		fs.put("relAssignedTo", sugar().users.getQAUser().get("userName"));
		fs.put("relOpportunityName", myOpp.getRecordIdentifier());
		fs.put("forecast", "Include");
		fs.put("date_closed", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		myRLI.edit(fs);
		
		sugar().logout();
	}

	/**
	 * Verify that Commit activity log doesn't display info for fields with no access
	 * 
	 * @throws Exception
	 */
	@Test
	public void Forecasts_28392_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log in as qauser
		sugar().login(sugar().users.getQAUser());

		// Go to Forecasts module
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// TODO: VOOD-929
		new VoodooControl("span", "css", ".module-title").assertContains(sugar().users.getQAUser().get("userName"), true);	

		// Verify that header row shows "No access" in Best column
		new VoodooControl("span", "css", "span.fld_best_case div.datapoint.pull-left span").assertContains(roleRecord.get("noAccess"), true);
		
		// Change likely field
		new VoodooControl("span", "css", "span.list.fld_likely_case.isEditable").scrollIntoViewIfNeeded(sugar().revLineItems.listView.getControl("horizontalScrollBar"), false);
		new VoodooControl("span", "css", "span.list.fld_likely_case.isEditable").click();
		new VoodooControl("input", "css", "span.edit.fld_likely_case.isEditable input").set(roleRecord.get("likely"));
		new VoodooControl("a", "css", "a[name='commit_button']").click();
		VoodooUtils.waitForReady(60000);

		// Verify that commit history log shows "No access" in Best column
		new VoodooControl("span", "css", "div.last-commit div.datapoints div.pull-right div.datapoint.pull-left:nth-child(2)").assertContains(roleRecord.get("noAccess"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}