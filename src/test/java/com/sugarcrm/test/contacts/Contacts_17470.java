package com.sugarcrm.test.contacts;
import java.util.ArrayList;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_17470 extends SugarTest{
	ArrayList<String> filterName = new ArrayList<String>();

	public void setup() throws Exception {
		sugar().contacts.api.create();
		filterName.add("All");
		// TODO: VOOD-1344 Once resolved, below line should be fixed by 'sugar().campaigns.moduleNamePlural' as this available for 'Leads & Targets Module'
		filterName.add("Campaign Log");
		filterName.add(sugar().leads.moduleNamePlural);

		sugar().login();
	}

	/**
	 *  Verify that the default filter is All in Contact sub panel
	 *  @throws Exception
	 */
	@Test
	public void Contacts_17470_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Verify filter name choice according to search
		for (int i = 1; i <filterName.size(); i++) {
			sugar().contacts.recordView.setRelatedSubpanelFilter(filterName.get(i));
			sugar().contacts.recordView.getControl("relatedSubpanelChoice").assertEquals(filterName.get(i), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
