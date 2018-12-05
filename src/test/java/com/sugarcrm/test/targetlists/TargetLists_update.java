package com.sugarcrm.test.targetlists;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.test.SugarTest;

public class TargetLists_update extends SugarTest{
	TargetListRecord myTargetList;

	public void setup() throws Exception {
		myTargetList = (TargetListRecord)sugar().targetlists.api.create();
		sugar().login();
	}

	@Test
	public void TargetLists_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("targetlistName", "Edited sample");
		newData.put("description", "Edited description");
		
		// Edit the target list using the UI.
		myTargetList.edit(newData);
		
		// Verify the target list was edited.
		myTargetList.verify(newData);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
