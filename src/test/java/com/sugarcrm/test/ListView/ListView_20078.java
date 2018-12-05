package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_20078 extends SugarTest {
    FieldSet filterData = new FieldSet();
    VoodooControl opportunitiesCtrl;

    public void setup() throws Exception {
        filterData = testData.get(testName).get(0);
        sugar().login();

        // TODO: VOOD-938
        opportunitiesCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
        VoodooControl fieldLayoutCtrl = new VoodooControl("td", "id", "fieldsBtn");
        VoodooControl addFieldCtrl = new VoodooControl("input", "css", "[name='addfieldbtn']");
        VoodooControl dataTypeDropdownCtrl = new VoodooControl("select", "css", "select#type");
        VoodooControl nameFieldCtrl = new VoodooControl("input", "id", "field_name_id");
        VoodooControl enableRangeCtrl = new VoodooControl("input", "css", "input[name='enable_range_search']");
        VoodooControl saveButtonCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
        VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");

        // Navigate to Admin > studio > Opportunities > Fields > Add a custom field type: integer and enabling range search
        sugar().navbar.navToAdminTools();
        VoodooUtils.focusFrame("bwc-frame");
        sugar().admin.adminTools.getControl("studio").click();
        VoodooUtils.waitForReady();
        opportunitiesCtrl.click();
        VoodooUtils.waitForReady();
        fieldLayoutCtrl.click();
        VoodooUtils.waitForReady();
        addFieldCtrl.click();
        VoodooUtils.waitForReady();
        dataTypeDropdownCtrl.set(filterData.get("dataType"));
        VoodooUtils.waitForReady();
        nameFieldCtrl.set(filterData.get("fieldName"));
        enableRangeCtrl.set(Boolean.toString(true));
        saveButtonCtrl.click();
        VoodooUtils.waitForReady();

        // Record View. Navigate back to Studio (Footer Pane) >  Opportunities > Layout
        sugar().admin.studio.clickStudio();
        opportunitiesCtrl.click();
        layoutCtrl.click();

        // Adding the above created custom integer field to search layout
        new VoodooControl("td", "id", "searchBtn").click();
        new VoodooControl("td", "id", "FilterSearchBtn").click();
        VoodooUtils.waitForReady();
        VoodooControl defaultFieldsColumn = new VoodooControl("li", "css", "[data-name='amount']");
        String customIntHook = String.format("li[data-name=%s_c]", filterData.get("fieldName"));
        new VoodooControl("li", "css", customIntHook).dragNDrop(defaultFieldsColumn);
        new VoodooControl("td", "id", "savebtn").click();
        VoodooUtils.waitForReady();
        VoodooUtils.focusDefault();
    }

    /**
     * Verify the set of range search operator values for integer type custom field
     *
     * @throws Exception
     */
    @Test
    public void ListView_20078_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        //Navigate to Opportunities > List view, open  create filter window
        sugar().opportunities.navToListView();
        sugar().opportunities.listView.openFilterDropdown();
        sugar().opportunities.listView.selectFilterCreateNew();

        // TODO: VOOD-1478
        new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("fieldLabel"));
        new VoodooControl("span", "css", ".detail.fld_filter_row_operator").click();
        VoodooSelect operatorValues = new VoodooSelect("div", "css", "[id='select2-drop']");

        // Asserting the range search operator values for integer type custom field
        for (int i = 1; i <= 8; i++) {
            operatorValues.assertContains(filterData.get("fieldOperator" + i), true);
        }
        // close the operator dropdown
        operatorValues.click();

        // Close the filter in list view of Opportunities module
        sugar().opportunities.listView.filterCreate.cancel();

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}