package com.sugarcrm.test.RevenueLineItems;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.util.Map;

public class RevenueLineItems_28246 extends SugarTest {

    public void setup() throws Exception {
        sugar().accounts.api.create();
        sugar().login();

        // TODO: VOOD-1986
        sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
        sugar().forecasts.setup.toggleRangeSettings();
        sugar().forecasts.setup.getControl("rangeThree").click();
        sugar().forecasts.setup.saveSettings();
        VoodooUtils.waitForReady(30000);
        
        // Go to admin page //Hack: Direct navigation to RLI module is not working so using this to change the focus)
     	// TODO: VOOD-1666
     	sugar().navbar.navToAdminTools();
    }

    /**
     * Verify that forecast field is auto updated when sales stage of Opp/RLI is changed
     *
     * @throws Exception
     */
    @Test
    public void RevenueLineItems_28246_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        sugar().opportunities.navToListView();
        sugar().opportunities.listView.create();
        FieldSet oppRecord = sugar().opportunities.getDefaultData();
        sugar().opportunities.createDrawer.getEditField("name").set(oppRecord.get("name"));
        sugar().opportunities.createDrawer.getEditField("relAccountName").set(oppRecord.get("relAccountName"));
        sugar().opportunities.createDrawer.getEditField("rli_name").set(oppRecord.get("rli_name"));
        sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(oppRecord.get("rli_expected_closed_date"));
        sugar().opportunities.createDrawer.getEditField("rli_likely").set(oppRecord.get("rli_likely"));

        FieldSet salesStage = testData.get(testName).get(0);
        VoodooSelect salesStageField = (VoodooSelect) sugar().opportunities.createDrawer.getEditField("rli_stage");
        VoodooSelect forecastField = (VoodooSelect) sugar().opportunities.createDrawer.getEditField("forecast");

        // Try different values for sales stage and notice "Forecast" field
        for (Map.Entry<String, String> entry : salesStage.entrySet()) {
            salesStageField.set(entry.getKey());
            // Verify that Value of "Forecast" field changes depending on the selected sales stage
            forecastField.assertEquals(entry.getValue(), true);
        }

        // Change value of "forecast" field
        for (String value : salesStage.values()) {
            forecastField.set(value);
            // Verify that  Selected value of Forecast field can be manually updated
            forecastField.assertEquals(value, true);
        }

        sugar().opportunities.createDrawer.save();
        // verify that opportunity record saved successfully
        sugar().opportunities.listView.verifyField(1, "name", oppRecord.get("name"));

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}