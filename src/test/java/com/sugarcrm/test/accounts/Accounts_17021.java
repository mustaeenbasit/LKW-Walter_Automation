package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Accounts_17021 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		sugar().accounts.api.create();
	}
	
	/**
	 * Inline edit textfield type field on list view
	 * @throws Exception
	 */
	@Test
	public void Accounts_17021_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet data = new FieldSet();		
		
		sugar().accounts.navToListView();
		
		for (int i=0; i<ds.size()-1; i++) {
			data.put("name", ds.get(i).get("name"));
			sugar().accounts.listView.updateRecord(1, data);
			sugar().accounts.listView.verifyField(1, "name", ds.get(i).get("result"));
		}

		// Account name is empty
		data.put("name", ds.get(ds.size()-1).get("name"));	
		sugar().accounts.listView.updateRecord(1, data);
		// TODO: VOOD-824. Lib support to identify the exclamation sign icon in inline edit
		new VoodooControl("div", "css", ".fld_name div.error").assertVisible(true);
		sugar().accounts.listView.cancelRecord(1);
		VoodooUtils.waitForReady();
		sugar().accounts.listView.verifyField(1, "name", ds.get(ds.size()-1).get("result"));
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
