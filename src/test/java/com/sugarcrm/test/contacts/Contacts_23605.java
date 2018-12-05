package com.sugarcrm.test.contacts;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23605 extends SugarTest {
	DataSource bugsData = new DataSource();
	ArrayList<Record> bugRecords = new ArrayList<Record>();
	
	public void setup() throws Exception {
		bugsData = testData.get(testName);
		bugRecords = sugar().bugs.api.create(bugsData);
		sugar().contacts.api.create();
		sugar().login();
		
		// Enable the Bugs module and subpanels
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
	}

	/** Select bug_Verify that the selected bugs are displayed in "Bug" sub-panel of "Contact Detail View" page.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23605_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to contacts list view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		
		StandardSubpanel bugsSubPanel = (StandardSubpanel) sugar().contacts.recordView.subpanels
				.get(sugar().bugs.moduleNamePlural);
		
		// Linking bug records to Contact Record
		bugsSubPanel.linkExistingRecords(bugRecords);

		int size = bugsData.size();
		
		// Verifying bug records are displayed in bugs subpanel
		for (int i=1 ; i < size ; i++)
			bugsSubPanel.getDetailField(i, "name").assertEquals(bugsData.get(size - i)
					.get("name"), true);
			
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
