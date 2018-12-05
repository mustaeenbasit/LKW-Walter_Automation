package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21591 extends SugarTest {
	FieldSet contactFieldSet1, contactFieldSet2;
	DataSource ds;
	
	public void setup() throws Exception {
		sugar.login();
		
		ds = testData.get(testName);
		contactFieldSet1 = new FieldSet();
		contactFieldSet1.put("title",ds.get(0).get("title"));		
		contactFieldSet2 = new FieldSet();
		contactFieldSet2.put("title",ds.get(1).get("title"));
		
		sugar.contacts.api.create(contactFieldSet1);
		sugar.contacts.api.create(contactFieldSet2);		
	}

	/**
	 * Verify that page dashlets sort records on column titles
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21591_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		//TODO: VOOD-1017
		new VoodooControl("th", "css", "ul.dashlet-cell.rows.row-fluid.layout_Home table thead tr th:nth-child(4) span").click();
		new VoodooControl("div", "css", "tr:nth-child(1) td:nth-child(4) span div").assertEquals("sugar test2", true);		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}