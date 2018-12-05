package com.sugarcrm.test.admin;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.sugar.records.StandardRecord;
import com.sugarcrm.test.SugarTest;

public class Admin_20213 extends SugarTest {
	FieldSet customData;
	ArrayList<StandardRecord> StdRecs;
	ArrayList<StandardModule> StdMods;
	QuoteRecord myRec;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
		StdRecs = new ArrayList<StandardRecord>();
		StdMods = new ArrayList<StandardModule>();
		StdMods.add(sugar().contacts);
		StdMods.add(sugar().leads);
		StdMods.add(sugar().cases);
		StdMods.add(sugar().opportunities);
		StdMods.add(sugar().revLineItems);
		for (StandardModule mod : StdMods) {
			StdRecs.add((StandardRecord) mod.api.create());
		}
		myRec = (QuoteRecord) sugar().quotes.api.create();
	}

	/**
	 * Verify account module name on record view sync with updating module name
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20213_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().admin.renameModule(sugar().accounts, customData.get("singularLabel"), customData.get("pluralLabel"));

		// Verify account module name on record view sync with updating module name
		for (StandardRecord rec : StdRecs) {
			rec.navToRecord();
			new VoodooControl("div", "css", "div.record-label[data-name='account_name']").assertContains(customData.get("assert1"), true);
		}
		myRec.navToRecord();
		VoodooUtils.focusFrame("bwc-frame");	
		new VoodooControl("tr", "css", "table#LBL_QUOTE_INFORMATION tr:nth-of-type(5)").assertElementContains(customData.get("assert2"), true);
		new VoodooControl("tr", "css", "table#LBL_QUOTE_INFORMATION tr:nth-of-type(5)").assertElementContains(customData.get("assert3"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
