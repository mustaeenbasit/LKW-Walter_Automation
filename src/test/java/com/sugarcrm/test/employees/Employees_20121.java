package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20121 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();	
		VoodooUtils.waitForReady();
		for(int i=0;i<ds.size();i++) {
			new VoodooControl("i", "css", "li[data-module='Employees'] i.fa-caret-down").click();
			new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("input", "id", "last_name").set(ds.get(i).get("lastName"));
			new VoodooControl("input", "id", "first_name").set(ds.get(i).get("firstName"));
			new VoodooControl("input", "id", "Users0emailAddress0").set(ds.get(i).get("email"));
			new VoodooControl("select", "id", "employee_status").set(ds.get(i).get("status"));
			new VoodooControl("input", "id", "SAVE_HEADER").click();
			VoodooUtils.focusDefault();
		}
		new VoodooControl("li", "css", "li[data-module='Employees']").click();
		VoodooUtils.focusFrame("bwc-frame");
	}

	/**
	 * Employee basic search works fine
	 * @throws Exception
	 */
	@Test
	public void Employees_20121_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// TODO VOOD-1041
		// search by name and active check box is not checked
		new VoodooControl("input", "id", "search_name_basic").set(ds.get(0).get("name"));
		new VoodooControl("input", "id", "search_form_submit").click();
		int j=3;
		for(int i=ds.size()-1;i>=0;i--) {			
			if(ds.get(i).get("name").equals(ds.get(0).get("name"))){
				new VoodooControl("a", "css", "table.list.view tr:nth-child("+j+") td:nth-child(3)").assertElementContains(ds.get(i).get("name"),true);
				new VoodooControl("a", "css", "table.list.view tr:nth-child("+j+") td:nth-child(7)").assertElementContains(ds.get(i).get("email"),true);
				new VoodooControl("a", "css", "table.list.view tr:nth-child("+j+") td:nth-child(9)").assertElementContains(ds.get(i).get("status"),true);
				j++;
			}	
			else
				new VoodooControl("a", "css", "table.list.view").assertElementContains(ds.get(i).get("name"),false);	
		}
		// check no more rows in list beside the matched records
		new VoodooControl("a", "css", "table.list.view tr.pagination:nth-child("+j+")").assertExists(true);

		// search by name and active check box is checked
		new VoodooControl("input", "id", "open_only_active_users_basic").set("true");		
		new VoodooControl("input", "id", "search_form_submit").click();
		int k=3;
		for(int i=ds.size()-1;i>=0;i--) {			
			if(ds.get(i).get("name").equals(ds.get(0).get("name"))&&ds.get(i).get("status").equals("Active")){
				new VoodooControl("a", "css", "table.list.view tr:nth-child("+k+") td:nth-child(3)").assertElementContains(ds.get(i).get("name"),true);
				new VoodooControl("a", "css", "table.list.view tr:nth-child("+k+") td:nth-child(7)").assertElementContains(ds.get(i).get("email"),true);
				new VoodooControl("a", "css", "table.list.view tr:nth-child("+k+") td:nth-child(9)").assertElementContains(ds.get(i).get("status"),true);
				k++;
			}
		}
		// check no more rows in list beside the matched records
		new VoodooControl("a", "css", "table.list.view tr.pagination:nth-child("+k+")").assertExists(true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}