package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17069 extends SugarTest {
	AccountRecord testAcc, accToLink;
	DataSource ds;
	FieldSet fs, fsToLink;	
		
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		fs = ds.get(0);
		fsToLink = ds.get(1);
		testAcc = (AccountRecord)sugar().accounts.api.create();
		accToLink = (AccountRecord)sugar().accounts.api.create(fsToLink);
	}

	/**
	 * Test Case 17069: Verify user can save the inline edit changes to the fields in the record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17069_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		testAcc.navToRecord();
		sugar().accounts.recordView.showMore();
		
		// TODO: VOOD-854 - Need lib support for inline edit on Record view
		new VoodooControl ("span", "css", "span[data-voodoo-name='name']").hover();
		new VoodooControl ("i", "css", "span[data-name='name'] .fa.fa-pencil").click();
		sugar().accounts.recordView.getEditField("name").set(fs.get("name"));
		
		new VoodooControl ("span", "css", "span[data-voodoo-name='website']").hover();
		new VoodooControl ("i", "css", "span[data-name='website'] .fa.fa-pencil").click();
		sugar().accounts.createDrawer.getEditField("website").set(fs.get("website"));
		
		new VoodooControl ("span", "css", "span[data-voodoo-name='industry']").hover();
		new VoodooControl ("i", "css", "span[data-name='industry'] .fa.fa-pencil").click();
		new VoodooControl ("input", "css", "div#select2-drop input.select2-input.select2-focused").set(fs.get("industry"));
		new VoodooControl ("span", "css", "div#select2-drop span.select2-match").click();
		
		new VoodooControl ("span", "css", "span[data-voodoo-name='parent_name']").hover();
		new VoodooControl ("i", "css", "span[data-name='parent_name'] .fa.fa-pencil").click();
		new VoodooControl ("input", "css", "div#select2-drop input.select2-input.select2-focused").set(accToLink.getRecordIdentifier());
		new VoodooControl ("span", "css", "div#select2-drop span.select2-match").click();
		
		new VoodooControl ("span", "css", "span[data-voodoo-name='account_type']").hover();
		new VoodooControl ("i", "css", "span[data-name='account_type'] .fa.fa-pencil").click();
		new VoodooControl ("input", "css", "div#select2-drop input.select2-input.select2-focused").set(fs.get("type"));
		new VoodooControl ("span", "css", "div#select2-drop span.select2-match").click();
		
		new VoodooControl ("span", "css", "span[data-voodoo-name='phone_office']").hover();
		new VoodooControl ("i", "css", "span[data-name='phone_office'] .fa.fa-pencil").click();
		sugar().accounts.createDrawer.getEditField("workPhone").set(fs.get("workPhone"));
		
		new VoodooControl ("span", "css", "span[data-voodoo-name='phone_fax']").hover();
		new VoodooControl ("i", "css", "span[data-name='phone_fax'] .fa.fa-pencil").click();
		sugar().accounts.createDrawer.getEditField("fax").set(fs.get("fax"));
		
		new VoodooControl ("span", "css", "span[data-voodoo-name='ownership']").hover();
		new VoodooControl ("i", "css", "span[data-name='ownership'] .fa.fa-pencil").click();
		sugar().accounts.createDrawer.getEditField("ownership").set(fs.get("ownership"));
		
		new VoodooControl ("span", "css", "span[data-voodoo-name='description']").hover();
		new VoodooControl ("i", "css", "span[data-name='description'] .fa.fa-pencil").click();
		sugar().accounts.createDrawer.getEditField("description").set(fs.get("description"));
		
		sugar().accounts.recordView.save();

		testAcc.verify(fs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}