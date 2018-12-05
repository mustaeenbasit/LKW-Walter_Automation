package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.RecordsModule;

public class ManufacturersEditView extends BWCEditView {

	public ManufacturersEditView(RecordsModule parentModule) throws Exception {
		super(parentModule);
		addControl("save", "input", "id", "btn_save");
		addControl("saveAndCreateNew", "input", "css", "[name='EditView'] [value='Save & Create New']");
	}

	/**
	 * Click the Save button.
	 *
	 * You must already be on the RecordView in edit mode to use this method.
	 * Added record will be available in ListView on the same page if successful, remains on the RecordView otherwise.
	 *
	 * @throws Exception
	 */
	@Override
	public void save() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Click the "Save And Create New" button.
	 *
	 * You must already be on the RecordView in edit mode to use this method.
	 * Added record will be available in ListView on the same page and control remains on the RecordView in edit mode.
	 *
	 * @throws Exception
	 */
	public void saveAndCreateNew() throws Exception {
		VoodooUtils.focusFrame("bwc-frame");
		getControl("saveAndCreateNew").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}
}