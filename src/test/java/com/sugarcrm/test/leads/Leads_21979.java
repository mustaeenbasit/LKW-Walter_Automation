package com.sugarcrm.test.leads;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Leads_21979 extends SugarTest {
	LeadRecord lead1;
	DataSource specChars = new DataSource();
	List<String> fieldsToSpecSet = Arrays.asList("lastName");
	String specLine = "";
	FieldSet filterData = new FieldSet();

	public void setup() throws Exception {
		filterData = testData.get(testName).get(0);
		specChars = testData.get("env_specialchars");
		// TODO: As specChars is being used for filling out lastName which incidentally is being handled by Javascript
		//       in set() method and Javascript is not kind to all special chars ('"\ specially) of env_specialchars, 
		//       this sanitizing has become necessary. Remove these below lines after Javascript handling is fixed in set().
		specChars.get(0).put("punctuation", filterData.get("punctuation"));
		specChars.get(0).remove("extended");
		specChars.get(0).remove("mixed");

		lead1 = (LeadRecord) sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Test Case 21979: Verify editing the search condition will return correct result
	 */
	@Test
	public void Leads_21979_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet leadData = new FieldSet();
		//Create a custom filter
		sugar().leads.navToListView();
		sugar().leads.listView.openFilterDropdown();
		sugar().leads.listView.selectFilterCreateNew();
		int n = specChars.size();
		int i = 0;
		while(i < n){
			for(Map.Entry entry : specChars.get(i).entrySet() ) {
				specLine = entry.getValue().toString();
				for (String field : fieldsToSpecSet ){
						leadData.put(field, specLine);
					}
				lead1.edit(leadData);
				sugar().leads.navToListView();
				sugar().leads.listView.filterCreate.setFilterFields("lastName", filterData.get("filterFieldName"), filterData.get("operator"),specLine , 1);
				sugar().alerts.waitForLoadingExpiration();
				sugar().leads.listView.previewRecord(1);
				}
			leadData.clear();
			i++;
		}
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}