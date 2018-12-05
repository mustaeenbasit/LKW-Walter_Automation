package com.sugarcrm.test.calls;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_28926 extends SugarTest {
	StandardSubpanel callSP;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		
		// Creating multiple call records for linking with accounts.
		DataSource ds = testData.get(testName);
		ArrayList<Record> callRecords = new ArrayList<Record>();
		callRecords = sugar().calls.api.create(ds);
		sugar().login();
		
		// Linking Existing Calls in Subpanel in Accounts Record View
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		callSP = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callSP.linkExistingRecords(callRecords);
	}

	/**
	 * Verify that Data is saved correctly when user updates same filed twice using inline edit of Calls sub panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_28926_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create two Voodoo controls for calls sub panel edit field and detail field.
		VoodooControl callSPGetEditField = callSP.getEditField(1, "name");
		VoodooControl callSPGetDetailField = callSP.getDetailField(1, "name");
		
		// Update call record using inline edit of calls sub panel and verify saved value.
		String callsDefaultName = sugar().calls.getDefaultData().get("name");
		callSP.editRecord(1);
		callSPGetEditField.set(callsDefaultName);
		callSP.saveAction(1);
		callSPGetDetailField.assertEquals(callsDefaultName, true);
		
		//  Again update same call record using inline edit of calls sub panel and verify saved value.
		callSP.editRecord(1);
		callSPGetEditField.set(testName);
		callSP.saveAction(1);
		callSPGetDetailField.assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}