package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_27203 extends SugarTest {
	DataSource modulesData;
	public void setup() throws Exception {
		modulesData = testData.get(testName);
		int size = modulesData.size();
		
		// Logging in as admin
		sugar.login();
		sugar.accounts.navToListView();
		
		// Verifying that account columns are ordered as per default settings 
		for(int i =0 ; i < size ; i++)
			new VoodooControl("th", "css", ".reorderable-columns tr:nth-child(1) th:nth-child("+ (i+2) +")").assertContains(modulesData.get(i).get("accDefault"), true);

		sugar.contacts.navToListView();
		// Verifying that contact columns are ordered as per default settings 
		for(int i =0 ; i < size ; i++)
			new VoodooControl("th", "css", ".reorderable-columns tr:nth-child(1) th:nth-child("+ (i+2) +")").assertContains(modulesData.get(i).get("conDefault"), true);

		sugar.leads.navToListView();
		// Verifying that contact columns are ordered as per default settings 
		for(int i =0 ; i < size ; i++)
			new VoodooControl("th", "css", ".reorderable-columns tr:nth-child(1) th:nth-child("+ (i+2) +")").assertContains(modulesData.get(i).get("leadDefault"), true);
		
	}
	/**
	*  Verify column ordering is sticky per user per module.
	*  @throws Exception
	*/
	@Test
	public void ListView_27203_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		VoodooControl firstColumn, secondColumn, thirdColumn;
		
 		// TODO: VOOD-1496
 		// Controls for Column headers
		firstColumn = new VoodooControl("th", "css", ".reorderable-columns tr:nth-child(1) th:nth-child(2)");
		secondColumn = new VoodooControl("th", "css", ".reorderable-columns tr:nth-child(1) th:nth-child(3)");
		thirdColumn = new VoodooControl("th", "css", ".reorderable-columns tr:nth-child(1) th:nth-child(4)");
		
		VoodooControl accNameHeaderDrag = new VoodooControl("span", "css", ".sorting.orderByname .ui-draggable span");
		VoodooControl nameHeaderDrag = new VoodooControl("span", "css", ".sorting.orderByfull_name .ui-draggable span");
		
		VoodooControl accCityHeaderDrop = new VoodooControl("div", "css", ".orderBybilling_address_city .th-droppable-placeholder");
		VoodooControl accNameHeaderDrop = new VoodooControl("div", "css", ".orderByaccount_name .th-droppable-placeholder");
		VoodooControl conEmailHeaderDrop = new VoodooControl("div", "css", ".orderByemail .th-droppable-placeholder");
		
		sugar.accounts.navToListView();
		// Moving Name column to the second place
		accNameHeaderDrag.dragNDrop(accCityHeaderDrop);
		
		// TODO: VOOD-984 
		VoodooUtils.pause(2000);
		
		// Verify that 'City' column is displayed as First Column
		firstColumn.assertEquals(modulesData.get(1).get("accDefault"), true);
		// Verify that 'Name' column is displayed as Second Column
		secondColumn.assertEquals(modulesData.get(0).get("accDefault"), true);
		
		sugar.contacts.navToListView();
		// Moving Name column to the second place
		nameHeaderDrag.dragNDrop(accNameHeaderDrop);
		
		secondColumn.hover();
		
		// Verify that 'Title' column is displayed as First Column
		firstColumn.assertEquals(modulesData.get(1).get("conDefault"), true);
		// Verify that 'Name' column is displayed as Second Column
		secondColumn.assertEquals(modulesData.get(0).get("conDefault"), true);
		
		sugar.logout();
		
		// Logging in as 'qauser'
		sugar.login(sugar.users.getQAUser());
		sugar.contacts.navToListView();
		// Moving Name column to the third place (from first)
		nameHeaderDrag.dragNDrop(conEmailHeaderDrop);

		// Verify that 'Title' column is displayed as First Column
		firstColumn.assertEquals(modulesData.get(1).get("conDefault"), true);
		// Verify that 'Account Name' column is displayed as Second Column
		secondColumn.assertEquals(modulesData.get(2).get("conDefault"), true);
		// Verify that 'Name' column is displayed as Third Column
		thirdColumn.assertEquals(modulesData.get(0).get("conDefault"), true);

		sugar.leads.navToListView();
		// Moving Name column to the second place
		nameHeaderDrag.dragNDrop(accNameHeaderDrop);		

		// Verify that 'Status' column is displayed as First Column
		firstColumn.assertEquals(modulesData.get(1).get("leadDefault"), true);	
		// Verify that 'Name' column is displayed as Second Column
		secondColumn.assertEquals(modulesData.get(0).get("leadDefault"), true);

		sugar.logout();
		
		// Logging in back as admin
		sugar.login();
		sugar.accounts.navToListView();
		// Verify that 'City' column is displayed as First Column
		firstColumn.assertEquals(modulesData.get(1).get("accDefault"), true);
		// Verify that 'Name' column is displayed as Second Column
		secondColumn.assertEquals(modulesData.get(0).get("accDefault"), true);
		// Verify that 'Billing Country' column is displayed as Third Column
		thirdColumn.assertEquals(modulesData.get(2).get("accDefault"), true);
		
		sugar.contacts.navToListView();	
		// Verify that 'Title' column is displayed as First Column
		firstColumn.assertEquals(modulesData.get(1).get("conDefault"), true);
		// Verify that 'Name' column is displayed as Second Column
		secondColumn.assertEquals(modulesData.get(0).get("conDefault"), true);
		// Verify that 'Account Name' column is displayed as Third Column
		thirdColumn.assertEquals(modulesData.get(2).get("conDefault"), true);		
		
		sugar.leads.navToListView();
		// Verify that 'Name' column is displayed as First Column
		firstColumn.assertEquals(modulesData.get(0).get("leadDefault"), true);	
		// Verify that 'Status' column is displayed as Second Column
		secondColumn.assertEquals(modulesData.get(1).get("leadDefault"), true);
		// Verify that 'Account Name' column is displayed as Third Column
		thirdColumn.assertEquals(modulesData.get(2).get("leadDefault"), true);
				
		sugar.logout();
		
		// Logging in back as 'qauser'
		sugar.login(sugar.users.getQAUser());
		
		sugar.accounts.navToListView();
		// Verify that 'Name' column is displayed as First Column
		firstColumn.assertEquals(modulesData.get(0).get("accDefault"), true);	
		// Verify that 'Status' column is displayed as Second Column
		secondColumn.assertEquals(modulesData.get(1).get("accDefault"), true);
		// Verify that 'Account Name' column is displayed as Third Column
		thirdColumn.assertEquals(modulesData.get(2).get("accDefault"), true);
		
		sugar.contacts.navToListView();
		// Verify that 'Title' column is displayed as First Column
		firstColumn.assertEquals(modulesData.get(1).get("conDefault"), true);
		// Verify that 'Account Name' column is displayed as Second Column
		secondColumn.assertEquals(modulesData.get(2).get("conDefault"), true);
		// Verify that 'Name' column is displayed as Third Column
		thirdColumn.assertEquals(modulesData.get(0).get("conDefault"), true);

		sugar.leads.navToListView();
		// Verify that 'Status' column is displayed as First Column
		firstColumn.assertEquals(modulesData.get(1).get("leadDefault"), true);	
		// Verify that 'Name' column is displayed as Second Column
		secondColumn.assertEquals(modulesData.get(0).get("leadDefault"), true);
		// Verify that 'Account Name' column is displayed as Third Column
		thirdColumn.assertEquals(modulesData.get(2).get("leadDefault"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}