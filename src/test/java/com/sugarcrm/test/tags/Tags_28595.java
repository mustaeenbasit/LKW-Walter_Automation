package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.test.SugarTest;

public class Tags_28595 extends SugarTest {
	public void setup() throws Exception {
		sugar().tags.api.create();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that the correct fields are shown in Tags module List View: for non-Admin users (Admin user: covered in self test).
	 * @throws Exception
	 */
	@Test
	public void Tags_28595_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource tagHeadersDS =  testData.get(testName);

		// Navigate to Tags list view and verify correct fields are shown.
		sugar().tags.navToListView();
		for (int i = 0; i < sugar().tags.listView.getHeaders().size(); i++) {
			sugar().tags.listView.getControl(VoodooUtils.prependCamelCase("header", 
					VoodooUtils.camelCase(sugar().tags.listView.getHeaders().get(i)))).assertEquals(tagHeadersDS.get(i).get("headerfield"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}