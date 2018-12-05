package com.sugarcrm.test.tags;

import com.sugarcrm.candybean.datasource.DataSource;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;


public class Tags_27914 extends SugarTest {
    DataSource data = new DataSource();

    public void setup() throws Exception {
        data = testData.get(testName);
        sugar().accounts.api.create(data.get(0));
        sugar().tags.api.create();
        sugar().login();
    }

    /**
     * Verify the input tags can be shown as existing or new tags
     *
     * @throws Exception
     */
    @Test
    public void Tags_27914_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        // Navigating to accounts and creating a new account record with tag
        sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
        sugar().accounts.createDrawer.getEditField("name").set(data.get(1).get("name"));
        sugar().accounts.createDrawer.getEditField("tags").set(data.get(1).get("tags"));
        sugar().accounts.createDrawer.save();

        // Navigate to one account record
        sugar().accounts.listView.clickRecord(2);
        sugar().accounts.recordView.edit();
        // TODO: VOOD-1985
        VoodooControl tagField = new VoodooControl("input", "css", ".fld_tag.edit input[type='text']");
        VoodooControl tagOptionField = new VoodooControl("ul", "css", "#select2-drop .select2-results");

        // Type in some tags value in the Tags field
        tagField.set(testName);
        VoodooUtils.waitForReady();
        // verify that the drop down will display what typed with "(new tag)" to let user know that it is a new tag in the system
        tagOptionField.assertElementContains(testName + " (New Tag)", true);

        // Type in a substring of existing tag
        tagField.set(data.get(0).get("tags").substring(0, 1));
        VoodooUtils.waitForReady();
        // Verify that It will display tags in the system that include a substring of what was typed
        tagOptionField.assertElementContains(data.get(0).get("tags"), true);
        tagOptionField.assertElementContains(data.get(1).get("tags"), true);

        // Type in a existing tag
        tagField.set(data.get(1).get("tags"));
        VoodooUtils.waitForReady();
        // Verify that It will display existing tags that are exact matches
        tagOptionField.assertElementContains(data.get(1).get("tags"), true);

        VoodooUtils.voodoo.log.info(testName + "complete.");
    }

    public void cleanup() throws Exception {}
}