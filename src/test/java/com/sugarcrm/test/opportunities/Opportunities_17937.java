package com.sugarcrm.test.opportunities;

import java.text.DecimalFormat;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_17937 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// 1 opp with multiple RLI's
		sugar().opportunities.create();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural).addRecord();
		sugar().revLineItems.createDrawer.showMore();
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(customData.get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").set(customData.get("bestCase"));
		sugar().revLineItems.createDrawer.getEditField("worstCase").set(customData.get("worstCase"));
		sugar().revLineItems.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify Opportunities fields are displayed in Edit view properly
	 *  
	 * @throws Exception
	 */
	@Test
	public void Opportunities_17937_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit record view
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.showLess(); // needed to execute actual test steps accordingly

		// name (required field)
		VoodooControl name = sugar().opportunities.recordView.getEditField("name");
		name.assertAttribute("class", "required", true);
		name.set(testName); // modified name

		// account name (search & select, required field)
		VoodooControl accountName = sugar().opportunities.recordView.getEditField("relAccountName");
		accountName.assertAttribute("class", "select2-choice", true); // select2 class ensure that search & select
		accountName.getChildElement("span", "css", ".select2-chosen").assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		// status (read-only field, prefilled with data)
		VoodooControl status = 	sugar().opportunities.recordView.getEditField("status");
		status.assertAttribute("class", "detail", true);
		status.assertEquals(customData.get("status"), true);

		// best case (read-only field, sum of RLI's best case amount)
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		VoodooControl bestCase = sugar().opportunities.recordView.getEditField("bestCase");
		Assert.assertTrue("Best case field is enabled.", bestCase.isDisabled());
		double bestVal = Integer.parseInt(sugar().opportunities.getDefaultData().get("likelyCase")) + Integer.parseInt(customData.get("bestCase"));
		String summationOfBest = String.format("%s%s", "$", formatter.format(bestVal));
		bestCase.assertAttribute("value", summationOfBest, true);

		// expected closed date (read-only field, latest date among RLIs)
		VoodooControl dateClosed = sugar().opportunities.recordView.getEditField("date_closed");
		Assert.assertTrue("Expected closed date field is enabled.", dateClosed.isDisabled());
		dateClosed.assertAttribute("value", sugar().opportunities.getDefaultData().get("date_closed"), true);

		// likely (read-only field, sum of RLI's liekly case amount)
		VoodooControl likelyCase = sugar().opportunities.recordView.getEditField("likelyCase");
		Assert.assertTrue("Likely case field is enabled.", likelyCase.isDisabled());
		double likelyVal = Integer.parseInt(sugar().opportunities.getDefaultData().get("likelyCase")) + Integer.parseInt(customData.get("likelyCase"));
		String summationOfLikely = String.format("%s%s", "$", formatter.format(likelyVal));
		likelyCase.assertAttribute("value", summationOfLikely, true);

		// worst case (read-only field, sum of RLI's worst case amount)
		VoodooControl worstCase = sugar().opportunities.recordView.getEditField("worstCase");
		Assert.assertTrue("Best case field is enabled.", worstCase.isDisabled());
		double worstVal = Integer.parseInt(sugar().opportunities.getDefaultData().get("likelyCase")) + Integer.parseInt(customData.get("worstCase"));
		String summationOfWorst = String.format("%s%s", "$", formatter.format(worstVal));
		worstCase.assertAttribute("value", summationOfWorst, true);

		sugar().opportunities.recordView.showMore();

		// next (input)
		VoodooControl nextStep = sugar().opportunities.recordView.getEditField("nextStep");
		nextStep.assertVisible(true);
		nextStep.set(customData.get("nextStep"));

		// type (dropdown, has 2 options)
		VoodooSelect type = (VoodooSelect)sugar().opportunities.recordView.getEditField("type");
		type.assertAttribute("class", "select2-choice", true); // select2 class ensure that dropdown
		type.click();

		// TODO: VOOD-1359
		VoodooSelect options = new VoodooSelect("ul", "css", "#select2-drop ul");
		options.assertContains(customData.get("type_1"), true);
		options.assertContains(customData.get("type_2"), true);

		// setting value
		type.selectWidget.getControl("searchBox").set(customData.get("type_2"));
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + customData.get("type_2") + "')]]").click();

		// Lead source (dropdown, and setting another value)
		VoodooSelect leadSource = (VoodooSelect)sugar().opportunities.recordView.getEditField("leadSource");
		leadSource.assertAttribute("class", "select2-choice", true);
		leadSource.set(customData.get("leadSource"));

		// Campaign (dropdown)
		sugar().opportunities.recordView.getEditField("relCampaign").assertAttribute("class", "select2-choice", true);

		// description
		sugar().opportunities.recordView.getEditField("description").assertVisible(true);

		// team (dropdown)
		sugar().opportunities.recordView.getEditField("relTeam").assertAttribute("class", "select2-choice", true);

		// Date created (read-only)
		sugar().opportunities.recordView.getEditField("date_entered_date").assertAttribute("class", "edit", false);

		// Date modified (read-only)
		sugar().opportunities.recordView.getEditField("date_modified_date").assertAttribute("class", "edit", false);

		// Save record with modified some values
		sugar().opportunities.recordView.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify detail values
		sugar().opportunities.recordView.getDetailField("name").assertEquals(testName, true);
		sugar().opportunities.recordView.getDetailField("nextStep").assertEquals(customData.get("nextStep"), true);
		sugar().opportunities.recordView.getDetailField("type").assertEquals(customData.get("type_2"), true);
		sugar().opportunities.recordView.getDetailField("leadSource").assertEquals(customData.get("leadSource"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}