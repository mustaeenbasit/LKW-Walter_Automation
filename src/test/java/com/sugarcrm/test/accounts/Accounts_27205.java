package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_27205 extends SugarTest {
	VoodooControl firstSelectCtrl,secondSelectCtrl,clickOnCreatedFilter,saveFilterCtrl;
	DataSource customData,accountData;

	public void setup() throws Exception {
		customData = testData.get(testName);
		accountData = testData.get(testName+"_accData");
		sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 * Verify ad-hoc filter.
	 * @throws Exception
	 */
	@Test
	public void Accounts_27205_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1879
		clickOnCreatedFilter = new VoodooControl("span", "css", ".filter-view.search.layout_Accounts span div .choice-filter-label");
		firstSelectCtrl = new VoodooSelect("a", "css", ".fld_filter_row_name.detail div a");
		saveFilterCtrl = new VoodooControl("a", "css", ".btn.btn-primary.save_button");
		secondSelectCtrl = new VoodooSelect("a", "css", ".fld_filter_row_operator.detail div a");
		VoodooControl filterCityText = new VoodooControl("input", "css", ".fld_address_city.detail input");
		VoodooControl addCtrl = new VoodooControl("button", "css", "[data-action='add']");
		VoodooControl typeFieldCtrl = new VoodooSelect("a", "css", "div.filter-definition-container div:nth-child(2) div > div:nth-child(1) a");
		VoodooControl typeOpCtrl =  new VoodooSelect("a", "css", "div.filter-definition-container div:nth-child(2) div > div:nth-child(2) a");
		VoodooControl accTypeCtrl = new VoodooControl("input", "css", ".fld_account_type.detail input");
		VoodooControl industryFieldCtrl = new VoodooSelect("a", "css", "div.filter-definition-container div:nth-child(3) div > div:nth-child(1) a");
		VoodooControl industryOpCtrl = new VoodooSelect("a", "css", "div.filter-definition-container div:nth-child(3) div > div:nth-child(2) a");
		VoodooControl industryCtrl = new VoodooControl("input", "css", ".fld_industry input");

		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();

		// Select Create for filter.
		sugar().accounts.listView.selectFilterCreateNew();

		// Enter filter name
		new VoodooControl("input", "css", ".filter-options.extend .filter-header input.inherit-width").set(customData.get(0).get("filterName"));
		
		// Select a field
		firstSelectCtrl.set(customData.get(0).get("field"));

		// Click "Save"
		saveFilterCtrl.click();

		// Verify user can save a filter without defining the operators or values of my search criteria.
		sugar().alerts.getSuccess().closeAlert();

		// Update the filter with operator and values
		clickOnCreatedFilter.click();
		secondSelectCtrl.set(customData.get(0).get("operator"));
		filterCityText.set(customData.get(0).get("value"));
		
		// Click "Save"
		saveFilterCtrl.click();

		sugar().alerts.waitForLoadingExpiration();
		
		// Change the values of this saved filter
		clickOnCreatedFilter.click();
		secondSelectCtrl.set(customData.get(1).get("operator"));
		filterCityText.set(customData.get(1).get("value"));

		// Click "Save"
		saveFilterCtrl.click();
		
		sugar().alerts.waitForLoadingExpiration();

		// Now again edit the created filter
		clickOnCreatedFilter.click();
		
		// Verify updated search results are displayed
		sugar().accounts.listView.verifyField(1, "billingAddressCity", customData.get(1).get("value"));
		sugar().accounts.listView.verifyField(2, "billingAddressCity", customData.get(1).get("value"));

		// Navigate away
		sugar().contacts.navToListView();

		// Navigate back & Verify filter criteria is preserved
		sugar().accounts.navToListView();
		
		clickOnCreatedFilter.click();
		firstSelectCtrl.assertContains(customData.get(1).get("field"), true);
		secondSelectCtrl.assertContains(customData.get(1).get("operator"), true);
		filterCityText.assertContains(customData.get(1).get("value"), true);

		// Verify search should ONLY trigger for fields that have a filter criteria
		// Set "Name starts with Aperture"  
		clickOnCreatedFilter.click();
		firstSelectCtrl.set(customData.get(2).get("field"));
		secondSelectCtrl.set(customData.get(2).get("operator"));
		new VoodooControl("input", "css", ".fld_name.detail input").set(customData.get(2).get("value"));

		// Type <is blank>
		addCtrl.click();
		typeFieldCtrl.set(customData.get(3).get("field"));
		typeOpCtrl.set(customData.get(3).get("operator"));
		accTypeCtrl.set(customData.get(3).get("value"));

		// Industry <is blank>
		addCtrl.click();
		industryFieldCtrl.set(customData.get(4).get("field"));
		industryOpCtrl.set(customData.get(4).get("operator"));
		industryCtrl.set(customData.get(4).get("value"));
		saveFilterCtrl.click();

		// Verify search should return results for all accounts where name starts with "Aperture"
		sugar().accounts.listView.verifyField(1, "name", customData.get(2).get("value"));
		sugar().accounts.listView.verifyField(2, "name", customData.get(2).get("value"));

		// Verify  For blank fields, the search should ignore them
		// Set "Name starts with < blank >"  
		clickOnCreatedFilter.click();
		firstSelectCtrl.set(customData.get(2).get("field"));
		secondSelectCtrl.set(customData.get(2).get("operator"));
		new VoodooControl("input", "css", ".fld_name.detail input").set("");

		// Type <is blank>
		new VoodooControl("button", "css", "[data-action='add']").click();
		typeFieldCtrl.set(customData.get(3).get("field"));
		typeOpCtrl.set(customData.get(3).get("operator"));
		accTypeCtrl.set(customData.get(3).get("value"));

		// Industry <is blank>
		new VoodooControl("button", "css", "[data-action='add']").click();
		industryFieldCtrl.set(customData.get(4).get("field"));
		industryOpCtrl.set(customData.get(4).get("operator"));
		industryCtrl.set(customData.get(4).get("value"));
		saveFilterCtrl.click();

		// Verify search should return results for all accounts
		for(int i = 0; i < accountData.size(); i++) {
			sugar().accounts.listView.assertContains(accountData.get(i).get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}