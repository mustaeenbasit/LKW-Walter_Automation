package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;
import java.util.ArrayList;

public class Cases_23292 extends SugarTest{
	FieldSet caseData = new FieldSet();
	ArrayList<CaseRecord> caseRecords = new ArrayList<CaseRecord>();
	int case_rows_number = 2;

	public void setup() throws Exception {
		caseData = sugar().cases.getDefaultData();
		sugar().accounts.api.create();
		sugar().login();
		int i=1;
		while(i <= case_rows_number){
			caseData.put("name",i + "_case");
			CaseRecord cs = (CaseRecord) sugar().cases.create(caseData);
			caseRecords.add(cs);
			i++;
		}
	}

	/*
	 *  Verify that case can be deleted when using "Delete" function in "Mass Update" panel.
	 */
	@Test
	public void Cases_23292_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		sugar().cases.navToListView();
		sugar().cases.listView.getControl("selectAllCheckbox").click();
		sugar().cases.listView.openActionDropdown();
		sugar().cases.listView.delete();
		sugar().cases.listView.confirmDelete();
		VoodooUtils.pause(2000);
		sugar().cases.listView.assertIsEmpty();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
