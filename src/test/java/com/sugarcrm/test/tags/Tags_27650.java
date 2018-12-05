package com.sugarcrm.test.tags;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import java.util.List;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

public class Tags_27650 extends SugarTest {
    DataSource tagsData = new DataSource();

    public void setup() throws Exception {
        tagsData = testData.get(testName);
        sugar().accounts.api.create();
        sugar().tags.api.create(tagsData);
        sugar().login();
    }

    /**
     * Verify Tag list should be in alphabetical order in record view
     *
     * @throws Exception
     */
    @Test
    public void Tags_27650_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        // Navigates to Account module
        sugar().accounts.navToListView();
        // Navigate to account record view
        sugar().accounts.listView.clickRecord(1);
        // edit the record
        sugar().accounts.recordView.edit();

        // create a list of tags
        ArrayList<String> tagNameList = new ArrayList<String>();
        for (FieldSet fs : tagsData) {
            tagNameList.add(fs.get("name"));
        }
        // Sort the tag list Alphabetically
        java.util.Collections.sort(tagNameList);

        // TODO: VOOD-1775
        VoodooControl tagField = new VoodooControl("input", "css", ".fld_tag.edit input[type='text']");
        // Enter "s" in the tags field
        tagField.set(tagNameList.get(1).substring(0, 1));

        // TODO: VOOD-1463
        for (int i = 1; i <= tagNameList.size(); i++) {
            String tagFieldValue = String.format("#select2-drop li:nth-child(%s) div", i + 1);
            // Verify that tag list should be in alphabetical order
            new VoodooControl("div", "css", tagFieldValue).assertEquals(tagNameList.get(i-1), true);
        }

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}