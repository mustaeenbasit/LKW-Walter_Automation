package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21857 extends SugarTest {
	FieldSet leadNameData;

	public void setup() throws Exception {
		leadNameData = new FieldSet();
		for(int i=1; i<=3;i++) {
			leadNameData.put("lastName", testName +"_"+i);
			sugar().leads.api.create(leadNameData);
		}
		sugar().login();
	}

	@Test
	public void Leads_21857_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Entering the String in the Listview quick Search Filter
		sugar().leads.navToListView();
		sugar().leads.listView.setSearchString(testName +"_3");

		// Asserting only the Record Entered, is listed
		sugar().leads.listView.verifyField(1, "fullName", testName+"_3");
		Assert.assertTrue("Records in listview are not equal to ONE",sugar().leads.listView.countRows() == 1);

		// After clearing the Quick Search Filter asserting all records are listed
		sugar().leads.listView.clearSearch();
		Assert.assertTrue("Records in listview are not equals to THREE",sugar().leads.listView.countRows() == 3);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
