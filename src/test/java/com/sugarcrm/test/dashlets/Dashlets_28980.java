package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Dashlets_28980 extends SugarTest {
	UserRecord myUserRecord;

	public void setup() throws Exception {
		sugar.login(); // Login as Admin
		
		// Create new user and set ReportTo QAUser
		FieldSet reportToUser = new FieldSet();
		reportToUser.put("reportsTo", sugar.users.qaUser.get("lastName"));
		myUserRecord = (UserRecord) sugar.users.create(reportToUser);
		
		// Logout as Admin and Login as QAUser
		sugar.logout();
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * Verify that My records/Group Records menu item appear in default dataset dropdown in "Top 10 ..." dashlet settings   
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_28980_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Dashboard > Top 10 Sales Opportunities
		// TODO: VOOD-960 & VOOD-1376
		new VoodooControl("i", "css", ".dashlets.row-fluid li.span8 ul li:nth-child(2) ul.dashlet-cell.rows.row-fluid .fa.fa-cog").click();
		VoodooControl editCtrl = new VoodooControl("a", "css", ".span8 ul li:nth-child(2) .dashlet-container [data-dashletaction='editClicked']");
		editCtrl.waitForVisible();
		editCtrl.click();
		VoodooUtils.waitForReady();
		DataSource customDS = testData.get(testName);
		
		// Click on default data-set drop-down
		new VoodooControl("span", "css", ".edit.fld_visibility .select2-container.select2 a .select2-arrow").click();
		VoodooControl dropDownCtrl = new VoodooSelect("ul", "css", "#select2-drop ul.select2-results");
		dropDownCtrl.waitForVisible();
		for(int i = 0; i < customDS.size(); i++)
			// Verify that in Default data-set drop-down : My Records & Group Records should appear.
			dropDownCtrl.assertContains(customDS.get(i).get("default_dataset"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}