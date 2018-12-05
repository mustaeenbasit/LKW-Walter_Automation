package com.sugarcrm.test.processauthor;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class ProcessAuthor_30750 extends SugarTest {
    public void setup() throws Exception {
        sugar().login();
    }

    /**
     * Verify "Start and End Date" field is editable in process approval form
     *
     * @throws Exception
     */
    @Test
    public void ProcessAuthor_30750_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        // Import Process Definition
        sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

        sugar().processDefinitions.navToListView();
        sugar().processDefinitions.listView.openRowActionDropdown(1);

        // Enable the imported Process Definition
        sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
        sugar().alerts.getWarning().confirmAlert();

        // Create new meeting confirming that Start Date and End Date are both set.
        sugar().calls.create();

        // Go to Process -> show Process.
        sugar().processes.navToListView();
        sugar().processes.listView.editRecord(1);
        // In approval form, click on "Edit" button.
        sugar().processes.recordView.edit();
        //  Verify that "Start and End Date" field is editable (not read only field).
        sugar().calls.recordView.getEditField("date_start_date").assertAttribute("type","text",true);
        sugar().calls.recordView.getEditField("date_end_date").assertAttribute("type","text",true);

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}