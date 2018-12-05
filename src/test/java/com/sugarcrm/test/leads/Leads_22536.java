package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Leads_22536 extends SugarTest {
	DataSource leadsDS = new DataSource();

	public void setup() throws Exception {
		leadsDS = testData.get(testName);
		sugar().leads.api.create(leadsDS);
		sugar().login();
	}

	/**
	 * Other_Verify that merge edit view can be correctly displayed
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_22536_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName + "_custom").get(0);

		// Merge lead records
		sugar().leads.navToListView();
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.openActionDropdown();

		// TODO: VOOD-657
		new VoodooControl("span", "css", ".fld_merge_button").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-721
		// Verify merge edit view (Header title with save and cancel buttons)
		new VoodooControl("div", "css", ".merge-duplicates-headerpane.fld_title div").assertEquals(customFS.get("mergeHeaderTitle"), true);
		new VoodooControl("a", "css",".merge-duplicates-headerpane.fld_cancel_button a").assertVisible(true);
		VoodooControl saveMergeBtn = new VoodooControl("a", "css",".merge-duplicates-headerpane.fld_save_button a");
		saveMergeBtn.assertVisible(true);

		// Verify Primary panel - (header title and by default radio buttons checked)
		new VoodooControl("span", "css", ".primary-edit-mode .primary-lbl span").assertEquals(customFS.get("primaryLabelValue"), true);
		new VoodooControl("input", "css", ".primary-edit-mode input[name=copy_first_name]").assertChecked(true);
		new VoodooControl("input", "css", ".primary-edit-mode input[name=first_name]").assertEquals(leadsDS.get(1).get("firstName"), true);
		new VoodooControl("input", "css", ".primary-edit-mode input[name=copy_last_name]").assertChecked(true);
		new VoodooControl("input", "css", ".primary-edit-mode input[name=last_name]").assertEquals(leadsDS.get(1).get("lastName"), true);
		new VoodooControl("input", "css", ".primary-edit-mode input[name=copy_account_name]").assertChecked(true);
		new VoodooControl("input", "css", ".primary-edit-mode input[name=account_name]").assertEquals(leadsDS.get(1).get("accountName"), true);
		new VoodooControl("input", "css", ".primary-edit-mode input[name=copy_status]").assertChecked(true);
		new VoodooControl("input", "css", ".primary-edit-mode input[name=status]").assertEquals(leadsDS.get(1).get("status"), true);

		// Verifying the values in Merge Form on non-Primary Panel (By default radio buttons unchecked)
		VoodooControl nonPrimaryFirstNameRadioBtn = new VoodooControl("input", "css", ".col:nth-child(2) input[name=copy_first_name]");
		nonPrimaryFirstNameRadioBtn.assertChecked(false);
		new VoodooControl("div", "css", ".col:nth-child(2) .fld_first_name div").assertContains(leadsDS.get(0).get("firstName"), true);
		new VoodooControl("input", "css", ".col:nth-child(2) input[name=copy_last_name]").assertChecked(false);
		new VoodooControl("div", "css", ".col:nth-child(2) .fld_last_name div").assertContains(leadsDS.get(0).get("lastName"), true);
		new VoodooControl("input", "css", ".col:nth-child(2) input[name=copy_status]").assertChecked(false);
		new VoodooControl("div", "css", ".col:nth-child(2) .fld_status div").assertContains(leadsDS.get(0).get("status"), true);
		VoodooControl nonPrimaryAccountNameRadioBtn = new VoodooControl("input", "css", ".col:nth-child(2) input[name=copy_account_name]");
		nonPrimaryAccountNameRadioBtn.assertChecked(false);
		new VoodooControl("div", "css", ".col:nth-child(2) .fld_account_name div").assertContains(leadsDS.get(0).get("accountName"), true);

		// Modifying first name and account name values in non-primary panel and save
		nonPrimaryFirstNameRadioBtn.click();
		nonPrimaryAccountNameRadioBtn.click();
		saveMergeBtn.click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify record with new values after merge
		Assert.assertTrue("Records are not merged and count not equals One", sugar().leads.listView.countRows() == 1);
		sugar().leads.listView.verifyField(1, "status", leadsDS.get(1).get("status"));
		sugar().leads.listView.verifyField(1, "fullName", customFS.get("fullName"));
		sugar().leads.listView.verifyField(1, "accountName", leadsDS.get(0).get("accountName"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}