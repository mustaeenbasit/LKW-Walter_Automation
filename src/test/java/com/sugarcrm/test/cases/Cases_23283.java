package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Cases_23283 extends SugarTest {
	CaseRecord myCase;

	public void setup() throws Exception {
		myCase = (CaseRecord)sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Verify that related contact can be created from case record view
	 * @throws Exception
	 */
	@Test
	public void Cases_23283_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Case record view
		myCase.navToRecord();

		// Create a contact record under Contact subpanel
		StandardSubpanel contactsSubpanel = sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.addRecord();
		sugar().contacts.createDrawer.showMore();
		sugar().contacts.createDrawer.setFields(sugar().contacts.getDefaultData());
		sugar().contacts.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		sugar().cases.recordView.showDataView();

		// Verify the Contact record is created and displayed in sub panel
		// TODO: VOOD-1100
		FieldSet contactFullName = new FieldSet();
		contactFullName.put("fullName", sugar().contacts.getDefaultData().get("fullName"));
		contactsSubpanel.verify(1, contactFullName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
