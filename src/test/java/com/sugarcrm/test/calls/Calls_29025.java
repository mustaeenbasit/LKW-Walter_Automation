package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_29025 extends SugarTest {
	FieldSet callData;

	public void setup() throws Exception {
		callData = testData.get(testName).get(0);
		String currentDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");

		// Create a Call record with 1 hr duration 
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		fs.put("date_start_time", callData.get("date_start_time"));
		fs.put("date_end_time", callData.get("date_end_time"));
		fs.put("date_end_date", currentDate);
		fs.put("date_start_date", currentDate);
		sugar.calls.api.create(fs);
		sugar.login();
	}

	/**
	 * Verify Call start/end date-times should reflect correctly in create drawer inline time line for each invitee
	 * @throws Exception
	 */
	@Test
	public void Calls_29025_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to calls record view
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);

		// Verifying Each span of timeblock ( Verifying for Call created timeslot the span contains class 'busy')
		// TODO: VOOD-1699
		for(int i = 1; i < 10; i++) {
			for(int j = 1; j < 5; j++) {
				if(i == 5) {
					new VoodooControl("div", "css", ".participant .times  div:nth-child("+ i +") span:nth-child("+ j +")").
					assertAttribute("class", callData.get("bookedSlot"), true);
					new VoodooControl("div", "css", ".participants-schedule div:nth-child(5).participant .times  div:nth-child("+ i +") span:nth-child("+ j +")").
					assertAttribute("class", callData.get("bookedSlot"), true);
				}
				else {
					new VoodooControl("div", "css", ".participant .times  div:nth-child("+ i +") span:nth-child("+ j +")").
					assertAttribute("class", callData.get("bookedSlot"), false);	
					new VoodooControl("div", "css", ".participants-schedule div:nth-child(5).participant .times  div:nth-child("+ i +") span:nth-child("+ j +")").
					assertAttribute("class", callData.get("bookedSlot"), false);
				}
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}