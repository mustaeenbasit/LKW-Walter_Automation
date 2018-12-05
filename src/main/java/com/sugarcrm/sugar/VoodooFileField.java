package com.sugarcrm.sugar;

import java.nio.file.Paths;

/**
 * Exposes functionality for interacting directly with SugarCRM file fields which accept file
 * system paths to files to be uploaded.
 * 
 * @author Mazen Louis <mlouis@sugarcrm.com>
 */
public class VoodooFileField extends VoodooControl {
	public VoodooFileField(String tagIn, String strategyNameIn, String hookStringIn)
		throws Exception {
		super(tagIn, strategyNameIn, hookStringIn);
	}

	/**
	 * Set the text of the file field.
	 * <p>
	 * This method will manipulate the given path to make it an absolute path based on environment.
	 * <p>
	 * NOTE: The string path for the file to be loaded should be from the projects root folder,
	 * e.g. src/test/resources/data/LoadThis.csv
	 *
	 * @param 	toSet	String value to be set (from project root src/)
	 * @throws	Exception
	 */
	public void set(String toSet) throws Exception {
		VoodooUtils.voodoo.log.info("Setting " + this + " to " + toSet);
		waitForElement().sendString(Paths.get(toSet).toAbsolutePath().toString(), true);
	}
}