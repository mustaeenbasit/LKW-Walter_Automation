package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;

public class Cases_23272 extends SugarTest {
	CaseRecord myCase;
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().accounts.api.create();

		//TODO VOOD-630: Couldn't create case with api.create() - using manual creation
		sugar().login();
		myCase = (CaseRecord)sugar().cases.create();
	}

	/**
	 * @author Dmitry Todarev <dtodarev@sugarcrm.com>
	 * Test Case 23272: Create Case_Verify that case can be duplicated when using "Duplicate" function in "Case" detail view.
	 */
	@Test
	public void Cases_23272_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		String subj = (String)ds.get(0).get("subj");

		// Go to case DetailView.
		myCase.navToRecord();

		// Click "Copy" button in "Case" detail view.
		sugar().cases.recordView.copy();

		// Update the Subject and Click "Save" button
		sugar().cases.createDrawer.getEditField("name").set(subj);

		sugar().cases.createDrawer.save();

		// Verify Subject is changed
		sugar().cases.recordView.getDetailField("name").assertElementContains(subj, true);
	}

	public void cleanup() throws Exception {}
}
