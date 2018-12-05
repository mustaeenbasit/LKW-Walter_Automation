package com.sugarcrm.sugar;

/**
 * Exposes functionality for interacting directly with tag field in
 * SugarCRM.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class VoodooTag extends VoodooSelect {
	public VoodooTag(String tagIn, String strategyNameIn, String hookStringIn)
			throws Exception {
		super(tagIn, strategyNameIn, hookStringIn);
	}

	/**
	 * Set the text of a tag widget.
	 * @param toSet	text of the tag to be set
	 * @throws Exception 
	 */
	public void set(String toSet) throws Exception {
		VoodooUtils.voodoo.log.info("Setting " + this + " to " + toSet);

		click(); // expose the tag search input
		new VoodooControl("input", "css", ".fld_tag.edit .select2-input").waitForElement().sendString(toSet, true);
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + toSet + "')]]").click();
	}
}
