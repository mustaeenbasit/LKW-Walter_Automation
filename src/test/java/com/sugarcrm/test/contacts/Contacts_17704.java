package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vaibhav Singhal <vsinghal@sugarcrm.com>
 */
public class Contacts_17704 extends SugarTest {
	FieldSet customData;
	LeadRecord myLeads;
	ContactRecord myContact;
	StandardSubpanel leadSubpanel, directReportsSub;

	public void setup() throws Exception {
		myContact = (ContactRecord) sugar().contacts.api.create();
		myLeads = (LeadRecord)sugar().leads.api.create();
		customData = testData.get(testName).get(0);
		sugar().login();

		// Navigate to the contact record view and add a related direct report from DR subpanel
		myContact.navToRecord();
		directReportsSub = sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		directReportsSub.scrollIntoViewIfNeeded(false);
		directReportsSub.addRecord();
		sugar().contacts.createDrawer.getEditField("lastName").set(customData.get("lastName"));
		sugar().contacts.createDrawer.save();

		// Relate a lead to a contact
		leadSubpanel = sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSubpanel.scrollIntoViewIfNeeded(false);
		leadSubpanel.linkExistingRecord(myLeads);
	}

	/**
	 * Verify that a specific Related Module is selected, the Module Name is the very first Tag in the Filter bar.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_17704_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-468, VOOD-486
		VoodooControl allRecords = new VoodooControl("div", "css", "[data-id='all_records']");
		VoodooControl allRecordsDefCheck = allRecords.getChildElement("i", "css", ".select2-result-label .fa-check");

		// TODO: VOOD-815
		VoodooControl filterDropdown = new VoodooControl("a","css","span[data-voodoo-name='filter-filter-dropdown'] a");

		//Set the filter for leads
		sugar().contacts.recordView.setRelatedSubpanelFilter(sugar().leads.moduleNamePlural);

		//Verify Leads becomes first tag
		sugar().contacts.recordView.getControl("relatedSubpanelChoice").assertEquals(customData.get("leads"), true);

		//Verify filter all Leads and that its checked
		filterDropdown.click();
		allRecords.assertContains(customData.get("allLeads"), true);
		allRecordsDefCheck.assertVisible(true);
		allRecords.click();
		//Verify records in Leads
		leadSubpanel.getDetailField(1, "fullName").assertContains(sugar().leads.defaultData.get("fullName"), true);

		// Reset Filter
		sugar().contacts.recordView.setRelatedSubpanelFilter(customData.get("all"));

		//Set the filter for direct reports
		sugar().contacts.recordView.setRelatedSubpanelFilter(customData.get("directReports"));
		//Verify Direct report becomes first tag
		sugar().contacts.recordView.getControl("relatedSubpanelChoice").assertEquals(customData.get("directReports"), true);
		//Verify filter all Contacts and that its checked
		filterDropdown.click();
		allRecords.assertContains(customData.get("allContacts"), true);
		allRecordsDefCheck.assertVisible(true);
		allRecords.click();
		//Verify records in direct reports
		// TODO VOOD-609
        new VoodooControl("a", "css", "div[data-subpanel-link='direct_reports'] td[data-type='fullname'] a").assertContains(customData.get("lastName"), true);

        // Reset Filter
		sugar().contacts.recordView.setRelatedSubpanelFilter(customData.get("all"));

		//Set the filter for campaign logs
		sugar().contacts.recordView.setRelatedSubpanelFilter(customData.get("campaignLogs"));
		//Verify campaign logs becomes first tag
		sugar().contacts.recordView.getControl("relatedSubpanelChoice").assertEquals(customData.get("campaignLogs"), true);
		//Verify filter all Campaign Logs and that its checked
		filterDropdown.click();
		allRecords.assertContains(customData.get("allcampaignLogs"), true);
		allRecordsDefCheck.assertVisible(true);
		allRecords.click();
		//Verify 0 records in campaigns
		// TODO VOOD-1344
		new VoodooControl("li", "css", "div[data-subpanel-link='campaigns'] li").assertAttribute("class", "empty", true);

		// Reset Filter
		sugar().contacts.recordView.setRelatedSubpanelFilter(customData.get("all"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
