package com.sugarcrm.test.leads;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;
import org.junit.Ignore;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

public class Leads_21859 extends SugarTest {
    ArrayList<Record> leadsSet;
    DataSource ds = new DataSource();
    ArrayList<Integer> indexRows = new ArrayList<Integer>(Arrays.asList(10,20));

	public void setup() throws Exception {
        for (int i=1; i<25; i++){
            FieldSet fs = new FieldSet();
            if (i < 10){
                fs.put("lastName", "0" + String.valueOf(i));
            }
            else fs.put("lastName", String.valueOf(i));
            ds.add(fs);
        }
		leadsSet = sugar().leads.api.create(ds);
        sugar().login();
	}

	/**
	 * Test Case 21859: Verify that clicking more leads will return more leads in list view
	 * @throws Exception
	 */
	@Test
	public void Leads_21859_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

        sugar().leads.navToListView();
        sugar().leads.listView.sortBy("headerFullname", true);

        sugar().leads.listView.getControl("showMore").assertExists(true);
        sugar().leads.listView.getControl("showMore").click();

        for (Integer row : indexRows){
            sugar().leads.listView.showMore();
            sugar().leads.listView.previewRecord(row);
            sugar().previewPane.getPreviewPaneField("fullName").assertContains(row.toString(), true);
        }

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}