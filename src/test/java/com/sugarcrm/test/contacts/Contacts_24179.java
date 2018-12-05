package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 *  @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Contacts_24179 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().contacts.api.create();
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Go to Contacts record view, create a new opportunity from Opportunities subpanel
	 * @throws Exception
	 */
	@Test
	public void Contacts_24179_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Add Opportunity
		StandardSubpanel oppSubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		oppSubpanel.addRecord();
		FieldSet newFS = new FieldSet();
		newFS.put("name", sugar().opportunities.getDefaultData().get("name"));
		newFS.put("description", sugar().opportunities.getDefaultData().get("description"));
		newFS.put("relAccountName", myAccount.getRecordIdentifier());
		sugar().opportunities.createDrawer.showMore();
		sugar().opportunities.createDrawer.setFields(newFS);

		// TODO: VOOD-1359
		// Add new RLI row
		new VoodooControl("a", "css", ".fieldset.edit .addBtn").click();
		String secondRLIRecord = "[data-voodoo-name='subpanel-for-opportunities-create'] tbody tr";
		new VoodooControl("input", "css", secondRLIRecord+" .fld_name.edit input").set(customFS.get("rli_name"));
		new VoodooControl("input", "css", secondRLIRecord+" .fld_date_closed.edit input").set(customFS.get("date_expected_closed_date"));
		new VoodooControl("input", "css", secondRLIRecord+" .fld_likely_case.edit input").set(customFS.get("likely"));
		sugar().opportunities.createDrawer.save(); // Save Opportunity

		// Verify that created opportunity is available in Opportunities subpanel
		oppSubpanel.expandSubpanel();
		newFS.clear();
		newFS.put("name", sugar().opportunities.getDefaultData().get("name"));
		oppSubpanel.verify(1, newFS, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
