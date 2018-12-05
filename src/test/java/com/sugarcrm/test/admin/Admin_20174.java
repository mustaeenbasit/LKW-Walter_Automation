package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20174 extends SugarTest{
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Diagnostic links appear after executing SugarCRM log file Diagnostics
	 * @throws Exception
	 */
	@Test
	public void Admin_20174_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet links = new FieldSet();
		links = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "diagnostic").click();

		// TODO: VOOD-1172
		// Check-boxes that are checked by default and need to be unchecked .
		VoodooControl checkedCheckBox1 = new VoodooControl("input", "css", "input[name='configphp']");
		VoodooControl checkedCheckBox2 = new VoodooControl("input", "css", "input[name='custom_dir']");
		VoodooControl checkedCheckBox3 = new VoodooControl("input", "css", "input[name='phpinfo']");
		VoodooControl checkedCheckBox4 = new VoodooControl("input", "css", "input[name='mysql_dumps']");
		VoodooControl checkedCheckBox5 = new VoodooControl("input", "css", "input[name='mysql_schema']");
		VoodooControl checkedCheckBox6 = new VoodooControl("input", "css", "input[name='mysql_info']");
		VoodooControl checkedCheckBox7 = new VoodooControl("input", "css", "input[name='md5']");
		VoodooControl checkedCheckBox8 = new VoodooControl("input", "css", "input[name='beanlistbeanfiles']");
		VoodooControl checkedCheckBox9 = new VoodooControl("input", "css", "input[name='vardefs']");

		// Un-checking all the checked check-boxes except the "SugarCRM Log File" check-box. 
		checkedCheckBox1.assertAttribute("checked", "true");
		checkedCheckBox1.click();
		checkedCheckBox2.assertAttribute("checked", "true");
		checkedCheckBox2.click();
		checkedCheckBox3.assertAttribute("checked", "true");
		checkedCheckBox3.click();
		checkedCheckBox4.assertAttribute("checked", "true");
		checkedCheckBox4.click();
		checkedCheckBox5.assertAttribute("checked", "true");
		checkedCheckBox5.click();
		checkedCheckBox6.assertAttribute("checked", "true");
		checkedCheckBox6.click();
		checkedCheckBox7.assertAttribute("checked", "true");
		checkedCheckBox7.click();
		checkedCheckBox8.assertAttribute("checked", "true");
		checkedCheckBox8.click();
		checkedCheckBox9.assertAttribute("checked", "true");
		checkedCheckBox9.click();

		// Clicking on the "Execute Diagnostic" button
		new VoodooControl("input","css", "input[value='  Execute Diagnostic  ']").click();
		sugar().alerts.waitForLoadingExpiration();

		// Asserting the existence of link texts i.e "Download the Diagnostic file" and "Delete the Diagnostic file"
		new VoodooControl("table", "id", "contentTable").assertContains(links.get("downloadLink"), true);
		new VoodooControl("table", "id", "contentTable").assertContains(links.get("deleteLink"), true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}