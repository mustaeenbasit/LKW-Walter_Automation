package com.sugarcrm.test.contacts;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_24073 extends SugarTest {
	FieldSet casesName;
	ArrayList<Record> caseRecords;
	StandardSubpanel casesSubpanel;

	public void setup() throws Exception {
		casesName = new FieldSet();

		// More than 5 records of Case for a particular Contact exists.
		caseRecords = new ArrayList<Record>();
		for(int i = 8; i > 0; i--) {
			casesName.put("name", testName + "_" + i);
			caseRecords.add(sugar().cases.api.create(casesName));
			casesName.clear();
		}

		// Login as admin user
		sugar().login();
		sugar().contacts.create();

		// Link all 8 record to the contact record created
		sugar().contacts.listView.clickRecord(1);
		casesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		casesSubpanel.linkExistingRecords(caseRecords);
	}

	/** Verify that 'More cases' link is working fine on "Cases" sub-panel of a Contact record detail view.
	 * @throws Exception
	 */
	@Test
	public void Contacts_24073_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to contacts record view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Click to expand Cases sub-panel.
		casesSubpanel.expandSubpanel();

		// Verify that the User will find 5 cases related to Contact in the list
		Assert.assertTrue("In Cases subpanel record count should be 5",casesSubpanel.countRows() == (caseRecords.size()/2)+1);
		for(int i = 1; i<= (caseRecords.size()/2)+1; i++) {
			casesName.put("name", testName + "_" + i);
			casesSubpanel.verify(i, casesName, true);
			casesName.clear();
		}

		// Click 'More cases' link to view remaining cases of that Contact.
		casesSubpanel.showMore();
		VoodooUtils.waitForReady();

		// Verify that the User will find another 3 cases related to Contact in the list.(Total 8 records)(As order of record is not fixed so verifying row count of the subpanel record)
		Assert.assertTrue("In Cases subpanel record count should be 8",casesSubpanel.countRows() == caseRecords.size());
		for(int i = 1; i<= caseRecords.size(); i++) {
			casesName.put("name", testName + "_" + i);
			casesSubpanel.verify(i, casesName, true);
			casesName.clear();
		}

		// 'More cases' link is disappears now
		casesSubpanel.getControl("moreLink").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
