package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class EmailRecord extends BWCRecord {
	public EmailRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().emails;
	}
	
	/**
	 * Returns the Record's name by default but has to
	 * be overridden when the Identifier isn't a name but subject or title
	 * 
	 * @return - String of the records Identification (name, subject, title
	 *         etc.)
	 */
	@Override
	public String getRecordIdentifier() {
		return this.get("subject");
	}
	
	/**
	 * Navigate to this email in the Emails Module.
	 * <p>
	 * When used, you will be left in the Email Module with this email record details displayed.
	 * <p>
	 * Note:<br>
	 * This record must exist in the Email Module to use.
	 */
	@Override
	public void navToRecord() throws Exception {
		sugar().emails.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		
		new VoodooControl("td", "xpath", "//*[contains(@id,'emailtabs')]//*[contains(@id,'listViewDiv')]//*[contains(@id,'emailGrid')]//*[contains(@class,'yui-dt-bd')]//*[contains(@class,'yui-dt-data')]//tr[1]//*[text()[contains(.,'"+ this.getRecordIdentifier() +"')]]").click();
		VoodooUtils.focusDefault();
	}
}
