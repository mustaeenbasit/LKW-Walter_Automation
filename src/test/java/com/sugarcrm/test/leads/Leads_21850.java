package com.sugarcrm.test.leads;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 
import com.sugarcrm.candybean.datasource.FieldSet;

public class Leads_21850 extends SugarTest {
	
	public void setup() throws Exception {
		// Creating Lead
		sugar().leads.api.create(testData.get(testName+"_1").get(0));
		
		// Logging in as admin
		sugar().login();	
	}
	
	/**
	 * Verify that the selected leads can be updated by changing all the fields in "Mass Update" sub-panel.
	 * @throws Exception
	 */	
	@Test
	public void Leads_21850_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 		

		FieldSet updateInput = testData.get(testName).get(0);
		
		// Verify the values before mass update
		sugar().leads.navToListView();	
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.getDetailField("relTeam").assertEquals(updateInput.get("value1"), false);
		
		// TODO : VOOD-917 "Do not call" checkbox to be added in LeadsModuleFields.csv	
		VoodooControl doNotCall = new VoodooControl("input","css",".fld_do_not_call.detail input");
		assertTrue("This checkbox should not be checked!", (false == Boolean.parseBoolean(doNotCall.getAttribute("checked"))));
		sugar().leads.recordView.getDetailField("leadSource").assertEquals(updateInput.get("value3"), false);	
		sugar().leads.recordView.getDetailField("status").assertExists(false);	
		sugar().leads.recordView.getDetailField("relAssignedTo").assertEquals(updateInput.get("value5"), false);
		
		FieldSet massUpdateData = new FieldSet();
		massUpdateData.put(updateInput.get("field1"), updateInput.get("value1"));
		massUpdateData.put(updateInput.get("field2"), updateInput.get("value2"));
		massUpdateData.put(updateInput.get("field3"), updateInput.get("value3"));
		massUpdateData.put(updateInput.get("field4"), updateInput.get("value4"));
		massUpdateData.put(updateInput.get("field5"), updateInput.get("value5"));		
		
		sugar().leads.navToListView();
		sugar().leads.listView.checkRecord(1);
		sugar().leads.massUpdate.performMassUpdate(massUpdateData);
		
		// Verify the values after mass update
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.getDetailField("relTeam").assertContains(updateInput.get("value1"), true);		
		assertTrue("This checkbox should be checked!", (true == Boolean.parseBoolean(doNotCall.getAttribute("checked"))));
		sugar().leads.recordView.getDetailField("leadSource").assertEquals(updateInput.get("value3"), true);
		sugar().leads.recordView.getDetailField("status").assertEquals(updateInput.get("value4"), true);	
		sugar().leads.recordView.getDetailField("relAssignedTo").assertEquals(updateInput.get("value5"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}
		
	public void cleanup() throws Exception {}
}
	
