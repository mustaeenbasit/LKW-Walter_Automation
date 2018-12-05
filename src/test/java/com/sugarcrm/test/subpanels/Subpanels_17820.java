package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17820 extends SugarTest {
	FieldSet caseSubjData = new FieldSet();
	ContactRecord myContact;
	CaseRecord relatedCase;
	VoodooSelect setRecordsFilter, setSubpanelFilter;
	VoodooControl filteredSubpanel;
	StandardSubpanel casesSubpanel;

	public void setup() throws Exception {
		casesSubpanel = new StandardSubpanel(sugar.cases);
		caseSubjData.put("name", sugar.cases.getDefaultData().get("name"));
		// TODO: VOOD-486
		setRecordsFilter = new VoodooSelect("a", "css",
				"span[data-voodoo-name='filter-filter-dropdown'] div a");
		filteredSubpanel = new VoodooControl("div", "css",
				"div[data-voodoo-name=Contacts] span[data-voodoo-name=filter-module-dropdown] div.choice-related");
		setSubpanelFilter = new VoodooSelect( "a", "css",
				"div[data-voodoo-name=Contacts] span[data-voodoo-name=filter-module-dropdown] a.select2-choice");

		sugar.login();
		myContact = (ContactRecord)sugar.contacts.api.create();
		relatedCase = (CaseRecord)sugar.cases.api.create();

		// Add a case to a contact
		myContact.navToRecord();
		casesSubpanel.clickLinkExisting();
		sugar.alerts.waitForLoadingExpiration();
		sugar.cases.searchSelect.search(relatedCase.getRecordIdentifier());
		
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("input", "css", ".toggle-all[name='check']").set("true");
		new VoodooControl("a", "css", "a[name='link_button']").click();
		sugar.alerts.getSuccess().closeAlert();
	}
	
	/**
	 * Test Case 17820: Verify that user can filter records based on entered text strings
	 */
	@Test
	public void Subpanels_17820_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Select the Cases module in subpanels list
		// TODO: VOOD-468
		setSubpanelFilter.set("Cases");
		sugar.alerts.waitForLoadingExpiration();
		// TODO: VOOD-468
		filteredSubpanel.assertEquals("Cases", true);

		// Input a case name into subpanels search
		// TODO: VOOD-468
		new VoodooControl("input", "css", "div[data-voodoo-name=filter-quicksearch] input")
				.set(relatedCase.getRecordIdentifier());
		sugar.alerts.waitForLoadingExpiration();

		// Verify that search works
		casesSubpanel.verify(1, caseSubjData, true);

		// Filter on My Favorites to clear the list, as the record is not marked as a favorite
		// TODO: VOOD-486
		setRecordsFilter.set("My Favorites");
		sugar.alerts.waitForLoadingExpiration();
		// TODO: VOOD-735
		casesSubpanel.assertContains(caseSubjData.get("name"), false);

		// Filter on My cases
		// TODO: VOOD-486
		setRecordsFilter.set("My Cases");
		sugar.alerts.waitForLoadingExpiration();
		casesSubpanel.verify(1, caseSubjData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
