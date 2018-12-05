package com.sugarcrm.test.documents;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.*;

public class Documents_19493 extends SugarTest {
    DataSource ds = new DataSource();

    public void setup() throws Exception {
        ds = testData.get(testName);
        sugar().contacts.api.create(ds);
        sugar().documents.api.create();
        sugar().login();
    }

    /**
     * Documents - Contacts management_Verify that the "End" pagination function in the "Contacts" sub-panel works correctly.
     *
     * @throws Exception
     */
    @Test
    public void Documents_19493_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        sugar().documents.navToListView();
        // Go to Detail view of any Document record
        sugar().documents.listView.clickRecord(1);

        BWCSubpanel contactsSubPanel = sugar().documents.detailView.subpanels.get(sugar().contacts.moduleNamePlural);
        // link records to contacts subpanel
        contactsSubPanel.expandSubpanelActionsMenu();
        contactsSubPanel.subpanelAction("#documents_contacts_select_button");
        VoodooUtils.waitForReady();
        VoodooUtils.focusWindow(1);
        // TODO: VOOD-1193
        new VoodooControl("input", "id", "massall_top").click();
        new VoodooControl("input", "id", "MassUpdate_select_button").click();
        VoodooUtils.focusWindow(0);
        VoodooUtils.waitForReady();
        VoodooUtils.focusFrame("bwc-frame");
        // sort the records by name
        new VoodooControl("a", "css", "#list_subpanel_contacts  tr:nth-child(2) > th:nth-child(1) a").click();
        VoodooUtils.waitForReady();
        // TODO: VOOD-779
        VoodooControl endLinkCtrl = new VoodooControl("button", "css", "#whole_subpanel_contacts [name = listViewEndButton]");
        // Click "End" link in the "Contacts" sub-panel.
        endLinkCtrl.click();
        VoodooUtils.waitForReady();
        // Verify that the list view changes to the end page correctly.
        // TODO: VOOD-972
        new VoodooControl("td", "css", "#list_subpanel_contacts tr:nth-child(3)  td:nth-child(1)").assertContains("Mr. " + ds.get(1).get("firstName") + " " + ds.get(1).get("lastName"), true);
        new VoodooControl("td", "css", "#list_subpanel_contacts tr:nth-child(4)  td:nth-child(1)").assertContains("Mr. " + ds.get(0).get("firstName") + " " + ds.get(0).get("lastName"), true);
        Assert.assertEquals("End link is visible when it should not", true, endLinkCtrl.isDisabled());
        VoodooUtils.focusDefault();

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}