package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.sugar.modules.StandardModule;

/**
 * Models the Preview Pane for standard SugarCRM modules.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 * 
 */
public class PreviewPane extends View {

	protected static PreviewPane previewPane;

	public static PreviewPane getInstance() throws Exception {
		if (previewPane == null)
			previewPane = new PreviewPane();
		return previewPane;
	}

	// TODO the parentModule has been included pending VOOD-454 which will make
	// PreviewPane a part of AppModel
	public PreviewPane() throws Exception {
		// Common controls for the right hand side preview pane
		addControl("close", "a", "css", ".btn.btn-invisible.closeSubdetail");
		addControl("showMore", "button", "css", ".preview-data button[data-moreless='more']");
		addControl("showLess", "button", "css", ".preview-data button[data-moreless='less']");
		addControl("nextRecordLink", "i", "css", ".fa-chevron-right");
		addControl("previousRecordLink", "i", "css", ".fa-chevron-left");
	}
	
	public void setModule(Module module) {
		parentModule = module;
	}

	/**
	 * This method will click on the "x" icon to close the Preview Pane.
	 * 
	 * You must already have the preview pane open. This method will leave you
	 * in the listview with the preview pane closed.
	 * 
	 * @throws Exception
	 */
	public void closePreview() throws Exception {
		getControl("close").click();
		VoodooUtils.pause(500);
	}

	/**
	 * This method will click on the "show more" link in a preview pane.
	 * 
	 * You must already have the preview pane open. This method will leave you
	 * in the preview pane with "more" information shown.
	 * 
	 * @throws Exception
	 */
	public void showMore() throws Exception {
		if (getControl("showMore").queryVisible()) {
			getControl("showMore").click();
			VoodooUtils.pause(500);
		}
	}

	/**
	 * This method will click on the "show less" link in the preview pane.
	 * 
	 * You must already have the preview pane open and have already clicked on
	 * the "show more" link This method will leave you in the preview pane with
	 * "less" information shown.
	 * 
	 * @throws Exception
	 */
	public void showLess() throws Exception {
		if (getControl("showLess").queryVisible()) {
			getControl("showLess").click();
			VoodooUtils.pause(500);
		}
	}

	/**
	 * Clicks on the next ">" button at the top right corner of Preview pane.
	 * 
	 * You must already have the preview pane open and there must be more than 
	 * one record on the list view so next ">" button is enabled. This method will 
	 * leave you in the preview pane 
	 * 
	 * @throws Exception
	 */
	public void gotoNextRecord() throws Exception {
		getControl("nextRecordLink").click();
		VoodooUtils.pause(500); 
	}
	
	/**
	 * Clicks on the previous "<" button at the top right corner of Preview pane.
	 * 
	 * You must already have the preview pane open and there must be more than 
	 * one record on the list view so the previous "<" button is enabled. This method will 
	 * leave you in the preview pane 
	 * 
	 * @throws Exception
	 */
	public void gotoPreviousRecord() throws Exception {
		getControl("previousRecordLink").click();
		VoodooUtils.pause(500); 
	}
	
	/**
	 * Retrieve a reference to the detail mode version of a field on the Preview
	 * Pane.
	 * 
	 * @param fieldName
	 *            - The VoodooGrimoire name for the desired control.
	 * @return a reference to the control.
	 */
	public VoodooControl getPreviewPaneField(String fieldName) throws Exception {
		return ((StandardModule)parentModule).getField(fieldName).previewPaneControl;
	}
}
