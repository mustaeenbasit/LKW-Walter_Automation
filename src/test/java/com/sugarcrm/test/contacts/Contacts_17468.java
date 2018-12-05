package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Contacts_17468 extends SugarTest {
	ContactRecord myContact;
	LeadRecord relatedLead;
	OpportunityRecord relatedOpportunity;
	CaseRecord relatedCase;

	public void setup() throws Exception {
		myContact = (ContactRecord)sugar().contacts.api.create();

		// Create related records
		relatedLead = (LeadRecord)sugar().leads.api.create();
		relatedOpportunity = (OpportunityRecord)sugar().opportunities.api.create();
		relatedCase = (CaseRecord)sugar().cases.api.create();

		sugar().login();
	}

	/** Verify if the user selects in Related: "All" value,
	 *  module can be collapsed and expended by arrow in Contact sub panel
	 *  @throws Exception
	 */
	@Test
	public void Contacts_17468_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myContact.navToRecord();

		// Link related Case to Contacts
		StandardSubpanel casesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		casesSubpanel.linkExistingRecord(relatedCase);

		// Expand Cases subpanel and check related record
		VoodooControl caseRow = new VoodooControl("div", "css","div[data-voodoo-name=Cases] table tbody tr:nth-of-type(1) .fld_name.list div");
		caseRow.assertEquals(relatedCase.get("name"), true);

		// Collapse Cases subpanel and check that row with related record is not visible
		casesSubpanel.toggleSubpanel();
		caseRow.assertVisible(false);

		// Link related Lead to Contacts
		StandardSubpanel leadsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecord(relatedLead);
		VoodooControl leadRow = new VoodooControl("div", "css","div[data-voodoo-name=Leads] table tbody tr:nth-of-type(1) .fld_full_name.list div");

		// Expand Leads subpanel and check related record
		leadRow.assertContains(relatedLead.getRecordIdentifier(), true);

		// Collapse Leads subpanel and check that row with related record is not visible
		leadsSubpanel.toggleSubpanel();
		leadRow.assertVisible(false);

		// Link related Opportunities to Contacts
		StandardSubpanel opportunitiesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opportunitiesSubpanel.linkExistingRecord(relatedOpportunity);
		VoodooControl opportunityRow = new VoodooControl("div", "css","div[data-voodoo-name=Opportunities] table tbody tr:nth-of-type(1) .fld_name.list div");

		// Expand Opportunities subpanel and check related record
		opportunityRow.assertEquals(relatedOpportunity.getRecordIdentifier(), true);

		// Collapse Opportunities subpanel and check that row with related record is not visible
		opportunitiesSubpanel.toggleSubpanel();
		opportunityRow.assertVisible(false);

		// TODO: Create Reports module object (BWC, custom). Refer to Bug VOOD-643
		// Click on "collapsable arrow" in Direct Reports subpanel
		// TODO: Create Documents module object (BWC). Refer to Bug VOOD-419
		// Click on "collapsable arrow" in Documents subpanel
		// TODO: Create Quotes module object (BWC). Refer to Bug VOOD-420
		// Click on "collapsable arrow" in Quotes subpanel
		// Click on "collapsable arrow" in Campaign Log subpanel

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
