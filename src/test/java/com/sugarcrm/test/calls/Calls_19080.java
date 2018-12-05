package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_19080 extends SugarTest {
	CallRecord myCall, dupCall;
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName);
		myCall = (CallRecord) sugar().calls.api.create(ds.get(0));
		sugar().login();

		myCall.navToRecord();
	}

	/**
	 * Verify that duplicate call is scheduled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_19080_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().calls.recordView.copy();
		sugar().calls.createDrawer.save();

		dupCall = new CallRecord(myCall.deepClone());
		dupCall.putAll(ds.get(1));
		dupCall.verify();	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}