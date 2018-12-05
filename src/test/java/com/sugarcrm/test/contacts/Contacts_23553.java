package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23553 extends SugarTest {
	ContactRecord myContact;
	CaseRecord myCase;
	AccountRecord myAccount;
	StandardSubpanel caseSubpanel;
	DataSource customData;

	public void setup() throws Exception {
		customData = testData.get(testName);
		myContact = (ContactRecord) sugar().contacts.api.create();
		myCase = (CaseRecord) sugar().cases.api.create();
		myAccount = (AccountRecord) sugar().accounts.api.create();

		sugar().login();

		// Relate Case with Account
		FieldSet newData = new FieldSet();
		newData.put("relAccountName", myAccount.getRecordIdentifier());
		myCase.edit(newData);

		// Link Contact with existing record of Case
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		caseSubpanel = sugar().contacts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		caseSubpanel.linkExistingRecord(myCase);

		// Verify that case record is linked successfully
		newData.clear();
		newData.put("name", myCase.getRecordIdentifier());
		caseSubpanel.verify(1, newData, true);
	}

	/**
	 * Verify that related case can be edited from contact detail view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23553_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "edit" icon at the right edge of "Cases" sub-panel
		caseSubpanel.scrollIntoViewIfNeeded(false);
		caseSubpanel.editRecord(1);

		// Modify all the fields
		// TODO: VOOD-503 need lib support of all controls in subpanel inline edit form
		new VoodooControl("input", "css", ".fld_name.edit input").set(customData.get(0).get("name"));
		sugar().cases.getField("relAccountName").editControl.set(myAccount.getRecordIdentifier());
		sugar().cases.getField("status").editControl.set(customData.get(0).get("status"));
		VoodooSelect assigned = new VoodooSelect("span", "css", "span.fld_assigned_user_name.edit");
		assigned.scrollIntoViewIfNeeded(false);
		assigned.set(sugar().users.getQAUser().get("userName"));

		// Click "Save" button
		caseSubpanel.saveAction(1);

		FieldSet verifyData = new FieldSet();
		verifyData.put("name", customData.get(0).get("name"));
		verifyData.put("relAccountName", myAccount.getRecordIdentifier());
		verifyData.put("status", customData.get(0).get("status"));

		// Verify the case is displayed in "Cases" sub-panel as modified
		caseSubpanel.verify(1, verifyData, true);
		caseSubpanel.scrollIntoViewIfNeeded(false);
		caseSubpanel.assertContains(sugar().users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
