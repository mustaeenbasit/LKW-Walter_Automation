package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23456 extends SugarTest {
	AccountRecord acc1;
	CaseRecord case1, case2;
	NoteRecord note1;
	FieldSet fs = new FieldSet();

	public void setup() throws Exception {
		acc1 = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
		fs.put("name", "ABC");
		fs.put("relAccountName", acc1.getRecordIdentifier());
		case1 = (CaseRecord) sugar().cases.create(fs);
		fs.put("name", "XYZ");
		case2 = (CaseRecord) sugar().cases.create(fs);

		fs.clear();
		fs.put("subject", "TestNote23456");
		fs.put("relRelatedToModule", "Case");
		fs.put("relRelatedToValue", case1.getRecordIdentifier());
		note1 = (NoteRecord)sugar().notes.create(fs);
	}

	/**
	 * Test Case 23456: Verify that you are presented with related Cases when
	 * editing a Note's "Related to:" field
	 */
	@Test
	public void Cases_23456_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		note1.navToRecord();
		sugar().notes.recordView.edit();
		fs.clear();
		fs.put("relRelatedToValue", case2.getRecordIdentifier());
		sugar().notes.createDrawer.setFields(fs);
		sugar().notes.createDrawer.getEditField("relRelatedToValue").assertContains(fs.get("relRelatedToValue"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
