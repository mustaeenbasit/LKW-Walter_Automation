package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ruchi Bhatnagar <rbhatnagar@sugarcrm.com>
 */
public class Meetings_29815 extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that label should not be missing for Meetings within a newly created role.
	 *  
	 * @throws Exception
	 */
	@Test
	public void Meetings_29815_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		roleRecord = testData.get("env_role_setup").get(0);
		// Create role 
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		// Select "Meetings"
		// TODO: VOOD-856
		new VoodooControl("a", "css", ".edit tr:nth-child(13) a").click();

		// Count number of Rows in the fieldPermission table
		int rowCount = new VoodooControl("tr", "css", "#ACLEditView table:nth-of-type(2) tr").count();

		// Count number of Columns in the fieldPermission table
		int columnsInRow[] = new int[rowCount];
		for(int i=0; i<rowCount; i++) {
			int columnCount = new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child("+(i+1)+") "+" td").count();
			columnsInRow[i] = columnCount;
		}

		// Verify all the fields in fields permission table has labels
		for(int matrixRow=1; matrixRow<=rowCount; matrixRow++) {
			for(int matrixCol=1; matrixCol<=columnsInRow[(matrixRow-1)]; matrixCol+=2) {
				new VoodooControl("td", "css", "#ACLEditView table:nth-child(15) tbody tr:nth-child("+matrixRow+") td:nth-child("+matrixCol+")").assertVisible(true);
			}
		}
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}