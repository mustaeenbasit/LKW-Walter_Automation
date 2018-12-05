package com.sugarcrm.test.bugs;

import java.util.ArrayList;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Bugs_18601 extends SugarTest {
	BugRecord bugRecord;
	ArrayList<Record> myContacts = new ArrayList<>();

	public void setup() throws Exception {
		bugRecord = (BugRecord) sugar.bugs.api.create();
		myContacts = sugar.contacts.api.create(testData.get(testName));
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * Select multiple contacts records
	 *
	 * @throws Exception
	 */
	@Test
	public void Bugs_18601_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		bugRecord.navToRecord();
		StandardSubpanel contactsSubpanel = sugar.bugs.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactsSubpanel.clickLinkExisting();
		sugar.alerts.waitForLoadingExpiration();

		// Verify record checkboxes
		sugar.contacts.searchSelect.selectRecord(1);
		sugar.contacts.searchSelect.selectRecord(2);
		sugar.contacts.searchSelect.selectRecord(3);

		// Verify contact records on search drawer
		for (int j = 0; j < myContacts.size(); j++) {
			sugar.contacts.searchSelect.search(myContacts.get(j).get("lastName"));
			sugar.alerts.waitForLoadingExpiration();
			sugar.contacts.searchSelect.preview(1); // search record with preview, no Verify method found in lib, even getPreviewPaneField() not working 
			sugar.alerts.waitForLoadingExpiration();
		}

		sugar.contacts.searchSelect.getControl("cancel").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}