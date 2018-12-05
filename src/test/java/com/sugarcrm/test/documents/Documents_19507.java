package com.sugarcrm.test.documents;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.*;

public class Documents_19507 extends SugarTest {
    DataSource ds = new DataSource();
    VoodooControl studioLinkDocuments, studioCtrl;

    public void setup() throws Exception {
        ds = testData.get(testName);
        sugar().leads.api.create(ds);
        sugar().documents.api.create();
        sugar().login();

        // Go to AdminModuel
        sugar().navbar.navToAdminTools();
        // TODO: VOOD-542
        VoodooUtils.focusFrame("bwc-frame");
        studioCtrl = sugar().admin.adminTools.getControl("studio");
        studioCtrl.click();
        VoodooUtils.waitForReady();
        studioLinkDocuments = new VoodooControl("a", "id", "studiolink_Documents");
        studioLinkDocuments.click();

        // create a one to many relationship between Document and lead so that lead subpanel will be available in Document detail view
        new VoodooControl("a", "css", "#relationshipsBtn tr:nth-child(2) a").click();
        new VoodooControl("input", "css", "[name='addrelbtn']").click();
        new VoodooControl("select", "id", "relationship_type_field").set("One to Many");
        VoodooUtils.waitForReady(30000);
        new VoodooControl("select", "id", "rhs_mod_field").set(sugar().leads.moduleNamePlural);
        VoodooUtils.waitForReady();
        new VoodooControl("input", "css", "[name='saverelbtn']").click();
        VoodooUtils.waitForReady(30000);
        VoodooUtils.focusDefault();
    }

    /**
     * Document - Leads management_Verify that the "Start" pagination function in the "Leads" sub-panel works correctly.
     *
     * @throws Exception
     */
    @Test
    public void Documents_19507_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        sugar().documents.navToListView();
        // Go to Detail view of document record
        sugar().documents.listView.clickRecord(1);

        // Link record to leads subpanel
        // TODO: VOOD-1713
        VoodooUtils.focusFrame("bwc-frame");
        new VoodooControl("span", "css", "#whole_subpanel_documents_leads_1 .pagination .sugar_action_button .ab").click();
        new VoodooControl("a", "css", "#documents_leads_1_select_button").click();
        VoodooUtils.waitForReady();
        VoodooUtils.focusDefault();
        VoodooUtils.focusWindow(1);

        // TODO: VOOD-1193
        new VoodooControl("input", "id", "massall_top").click();
        new VoodooControl("input", "id", "MassUpdate_select_button").click();
        VoodooUtils.focusWindow(0);
        VoodooUtils.waitForReady();
        VoodooUtils.focusFrame("bwc-frame");
        // sort the records by name
        new VoodooControl("a", "css", "#list_subpanel_documents_leads_1  tr:nth-child(2) > th:nth-child(1) a").click();
        VoodooUtils.waitForReady();
        // TODO: VOOD-779
        VoodooControl endLinkCtrl = new VoodooControl("button", "css", "#whole_subpanel_documents_leads_1 [name = listViewEndButton]");
        VoodooControl startLinkCtrl = new VoodooControl("button", "css", "#whole_subpanel_documents_leads_1 [name = listViewStartButton]");

        // Click "End" link in the "Leads" sub-panel.
        endLinkCtrl.click();
        VoodooUtils.waitForReady();
        // Click "Start" link in the "Leads" sub-panel.
        startLinkCtrl.click();
        VoodooUtils.waitForReady();

        // Verify that The list view changes to the start page correctly.
        // TODO: VOOD-972
        Assert.assertEquals("Start link is visible when it should not.", true, startLinkCtrl.isDisabled());
        for (int i = 1; i <= 5; i++) {
            String fullNameStr = String.format("Mr. %s %s", ds.get(ds.size() - i).get("firstName"), ds.get(ds.size() - i).get("lastName"));
            new VoodooControl("td", "css", "#list_subpanel_documents_leads_1 tr:nth-child(" + (i + 2) + ") td:nth-child(1)").assertContains(fullNameStr, true);
        }
        VoodooUtils.focusDefault();

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}