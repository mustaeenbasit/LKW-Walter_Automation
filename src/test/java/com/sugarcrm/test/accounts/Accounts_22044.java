package com.sugarcrm.test.accounts;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.util.ArrayList;

public class Accounts_22044 extends SugarTest {
    DataSource ds = new DataSource();
    ArrayList<Record> callRecords = new ArrayList<Record>();

    public void setup() throws Exception {
        ds = testData.get(testName);
        sugar().accounts.api.create();
        callRecords = sugar().calls.api.create(ds);
        sugar().login();
    }

    /**
     * User sort subpanel column of detail view and navigate to a special page of subpanel of detail view, leave then go back after refresh whole site
     *
     * @throws Exception
     */
    @Test
    public void Accounts_22044_execute() throws Exception {
        VoodooUtils.voodoo.log.info("Running " + testName + "...");

        // Go to a Accounts record detail page
        sugar().accounts.navToListView();
        sugar().accounts.listView.clickRecord(1);

        StandardSubpanel callsPanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
        // link the records in calls subpanel
        callsPanel.linkExistingRecords(callRecords);

        // Click "Subject" column title in "Calls" sub-panel to sort the records
        // Update sort status (Desc)
        callsPanel.sortBy("headerName", false);
        VoodooUtils.waitForReady();

        // TODO: VOOD-1424
        callsPanel.verify(1, ds.get(5), true);
        callsPanel.verify(2, ds.get(4), true);
        callsPanel.verify(3, ds.get(3), true);
        callsPanel.verify(4, ds.get(2), true);
        callsPanel.verify(5, ds.get(1), true);
        callsPanel.getControl("moreLink").assertVisible(true);
        callsPanel.showMore();
        callsPanel.verify(6, ds.get(0), true);
        callsPanel.getControl("moreLink").assertVisible(false);

        // refresh the  whole site
        VoodooUtils.refresh();
        // Verify that the records are still sorted in desc order in subpanel after refresh the site
        callsPanel.verify(1, ds.get(5), true);
        callsPanel.verify(2, ds.get(4), true);
        callsPanel.verify(3, ds.get(3), true);
        callsPanel.verify(4, ds.get(2), true);
        callsPanel.verify(5, ds.get(1), true);
        // Verify that "more Calls" link visible again after refresh the site
        callsPanel.getControl("moreLink").assertVisible(true);

        VoodooUtils.voodoo.log.info(testName + " complete.");
    }

    public void cleanup() throws Exception {}
}