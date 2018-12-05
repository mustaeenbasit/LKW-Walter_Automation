package com.sugarcrm.test.accounts;

import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.RecordView;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17286 extends SugarTest {
	VoodooControl visibleSubpanelWhenFilterApplied;
	String contactModuleNamePlural = "";
	String leadModuleNamePlural = "";
	RecordView accountRecordView;

	public void setup() throws Exception {
		// Create an Account record with related records such as contacts, opportunities, quotes, and leads.
		sugar().accounts.api.create();

		// Create a Contact and a lead
		ContactRecord linkContact = (ContactRecord) sugar().contacts.api.create();
		LeadRecord linkLead = (LeadRecord) sugar().leads.api.create();

		// Log-In as an admin
		sugar().login();

		// Create an Opportunity related to account created above
		// TODO: VOOD-444
		sugar().opportunities.create();

		// Create a quote record related to account created above
		// TODO: VOOD-444
		sugar().quotes.create();
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.clickRecord(1);

		contactModuleNamePlural = sugar().contacts.moduleNamePlural;
		leadModuleNamePlural = sugar().leads.moduleNamePlural;
		accountRecordView = sugar().accounts.recordView;

		// Link the Contact and Lead records created above to the account.
		accountRecordView.subpanels.get(contactModuleNamePlural).linkExistingRecord(linkContact);
		accountRecordView.subpanels.get(leadModuleNamePlural).linkExistingRecord(linkLead);
		// TODO: VOOD-2049 - Support needed to count the number of Subpanels in a module's Record view when relatedSubpanel filter is Set/Not Set
		visibleSubpanelWhenFilterApplied = new VoodooControl("div", "css", ".subpanels-layout :not(.hide).filtered");
	}

	// Method to set the relatedSubpanel filter and verify the subpanel displayed in the account's record view
	private void setAndVerifyRelatedModuleSubpanel(String moduleName) throws Exception {
		accountRecordView.setRelatedSubpanelFilter(moduleName);
		accountRecordView.subpanels.get(moduleName).assertVisible(true);
		assertTrue("Subpanel count is not equal to 1 when it should!", visibleSubpanelWhenFilterApplied.countWithClass() == 1);
	}

	/**
	 * Verify relationships dropdown
	 * @throws Exception
	 */
	@Test
	public void Accounts_17286_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource relatedModules = testData.get(testName);
		VoodooSelect relatedModuleDropDown = (VoodooSelect) accountRecordView.getControl("relatedSubpanelFilter");

		// Click the "Related" drop down and Verify that the drop down lists all the available related modules.
		relatedModuleDropDown.click();
		int relatedModulesCsvSize = relatedModules.size();

		// TODO: VOOD-629 - Add support for accessing and manipulating individual components of a VoodooSelect.
		for(int i = 1; i < relatedModulesCsvSize; i ++) {
			new VoodooControl("li", "css", ".search-related-dropdown .select2-results li:nth-child(" + (i + 1) + ")").assertEquals(relatedModules.get(i).get("moduleSubpanelNamesRelatedFilter"), true);
		}

		// In the Related drop down, select "All" option.
		// TODO: CB-252 - Need CB support to simulate input of any keyboard key i.e tab, esc etc.
		relatedModuleDropDown.selectWidget.getControl("searchBox").set(relatedModules.get(0).get("moduleSubpanelNamesRelatedFilter") + '\uE007');

		// Verify that all the related modules subpanels are displayed, and match what is listed on the "Related" dropdown
		HashMap<String, StandardSubpanel> subpanelMap = accountRecordView.subpanels;
		DataSource subpanelNames = testData.get(testName + "_subpanels");

		for(int j = 0; j < subpanelNames.size(); j ++) {
			StandardSubpanel sp = subpanelMap.get(subpanelNames.get(j).get("subpanelNames"));
			sp.getControl("subpanelName").assertEquals(relatedModules.get(j+1).get("moduleSubpanelNamesRelatedFilter"), true);
		}

		// Assert that Quotes (Ship To) subpanel is displayed
		String quoteShipToModuleNamePlural = relatedModules.get(relatedModulesCsvSize - 2).get("moduleSubpanelNamesRelatedFilter");
		// TODO: VOOD-2019 - Change in Quotes subpanel in Account & Contacts module records view
		VoodooControl shipToQuoteSubpanel = new VoodooControl("h4", "css", "div[data-subpanel-link='quotes_shipto'].filtered.layout_Quotes h4");
		shipToQuoteSubpanel.assertEquals(quoteShipToModuleNamePlural, true);

		// Assert that Campaign Log subpanel is displayed
		// TODO: VOOD-1344 - Need library support for 'Campaign Log' subpanel in Record view of any module
		new VoodooControl("div", "css", ".filtered.layout_CampaignLog h4").
		assertEquals(relatedModules.get(relatedModulesCsvSize - 1).get("moduleSubpanelNamesRelatedFilter"), true);

		String quoteBillToModuleNamePlural = relatedModules.get(13).get("moduleSubpanelNamesRelatedFilter");

		// Apply Contacts filter and assert that only Contacts subpanel is displayed
		setAndVerifyRelatedModuleSubpanel(contactModuleNamePlural);

		// Apply Opportunities filter and assert that only Opportunities subpanel is displayed
		setAndVerifyRelatedModuleSubpanel(sugar().opportunities.moduleNamePlural);

		// Apply Leads filter and assert that only Leads subpanel is displayed
		setAndVerifyRelatedModuleSubpanel(leadModuleNamePlural);

		// Apply Quotes (Bill To) filter and assert that only Quotes (Bill To) subpanel is displayed
		accountRecordView.setRelatedSubpanelFilter(quoteBillToModuleNamePlural);
		subpanelMap.get(sugar().quotes.moduleNamePlural).getControl("subpanelName").assertEquals(quoteBillToModuleNamePlural, true);
		int subpanelCount = visibleSubpanelWhenFilterApplied.countWithClass();
		assertTrue("Subpanel count is not equal to 1 when it should!", subpanelCount == 1);

		// Apply Quotes (Ship To) filter and assert that only Quotes (Ship To) subpanel is displayed
		accountRecordView.setRelatedSubpanelFilter(quoteShipToModuleNamePlural);
		shipToQuoteSubpanel.assertEquals(quoteShipToModuleNamePlural, true);
		subpanelCount = visibleSubpanelWhenFilterApplied.countWithClass();
		assertTrue("Subpanel count is not equal to 1 when it should!", subpanelCount == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}