package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28331 extends SugarTest {
    DataSource customData = new DataSource();
    DataSource filterData = new DataSource();

    public void setup() throws Exception {
        customData = testData.get(testName);
        filterData = testData.get(testName + "_filterData");
        sugar().accounts.api.create();
        sugar().opportunities.api.create(customData);
        sugar().login();
        sugar().opportunities.create(); // Need Opportunities with likely data
    }

    /**
     * Verify that Opportunities can be searched using Filter condition
     *
     * @throws Exception
     */
    @Test
    public void Opportunities_28331_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        // Click "Opportunities" tab on navigation bar
        sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
        sugar().opportunities.listView.sortBy("headerName", false);

        // Set 1 Opportunity record "Favorite"
        sugar().opportunities.listView.toggleFavorite(1);

        // Click on Create filter
        sugar().opportunities.listView.openFilterDropdown();
        sugar().opportunities.listView.selectFilterCreateNew();

        // Set filter condition "Opportunity Name","exactly matches",Opp1
        sugar().opportunities.listView.filterCreate.setFilterFields("name", filterData.get(0).get("fieldName"), filterData.get(0).get("Condition"), filterData.get(0).get("Value"), 1);

        // Verify All the opportunities matching the input search condition are displayed in "Opportunities" list view
        sugar().opportunities.listView.verifyField(1, "name", filterData.get(0).get("Value"));

        // Set filter condition "Account Name","is","Aperture Laboratories"
        sugar().opportunities.listView.filterCreate.setFilterFields("relAccountName", filterData.get(1).get("fieldName"), filterData.get(1).get("Condition"), filterData.get(1).get("Value"), 1);
        
        // Verify All the opportunities matching the input search condition are displayed in "Opportunities" list view
        sugar().opportunities.listView.verifyField(1, "relAccountName", sugar().opportunities.getDefaultData().get("relAccountName"));

        // Set filter condition "Likely","is greater than",100
        sugar().opportunities.listView.filterCreate.setFilterFields("likelyCase", filterData.get(2).get("fieldName"), filterData.get(2).get("Condition"), filterData.get(2).get("Value"), 1);

        // Verify All the opportunities matching the input search condition are displayed in "Opportunities" list view
        sugar().opportunities.listView.verifyField(1, "name", sugar().opportunities.getDefaultData().get("name"));

        // Cancel the filter
        sugar().opportunities.listView.filterCreate.cancel();
        
        // Set filter my favorites
        sugar().opportunities.listView.openFilterDropdown();
        sugar().opportunities.listView.selectFilterMyFavorites();
        VoodooUtils.waitForReady();

        // Verify My Favorites
        sugar().opportunities.listView.verifyField(1, "name", customData.get(1).get("name"));

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

	public void cleanup() throws Exception {}

}