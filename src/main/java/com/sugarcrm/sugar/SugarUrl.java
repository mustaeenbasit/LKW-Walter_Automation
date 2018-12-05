package com.sugarcrm.sugar;

import com.sugarcrm.candybean.configuration.Configuration;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class that provides a wrapper for manipulating Grimoire specific URLs
 * to provide consistency and maintainability. The usage of this class can be
 * found in SugarUrlTest.
 * @author Eric Tam <etam@sugarcrm.com>
 */
public class SugarUrl {
	private String baseUrl;
	private Configuration voodooConfig;
	private Map<String, String> parameters = new LinkedHashMap<>();

	public SugarUrl(String baseUrl) throws Exception {
		this.baseUrl = baseUrl;
		this.baseUrl = this.baseUrl.replaceAll("/$", ""); //Remove slash at the end
		this.voodooConfig = VoodooUtils.getVoodooConfig();
	}

	public SugarUrl() throws Exception {
		this(VoodooUtils.getGrimoireConfig().getValue("env.base_url", "http://localhost/sugar"));
	}

	/**
	 * Returns a url obtained from grimoire.config. This base url also accounts for
	 * parallelization which appends _$(THREADNUMBER) to the end.
	 * @return
	 */
	public String getBaseUrl() {
		return escapeUrlAndToString(constructBaseUrl());
	}

	/**
	 * Returns a base url with parameters
	 * @return
	 */
	public String getFullUrl() {
		return escapeUrlAndToString(constructBaseUrl().append(constructUrlParameters()));
	}

	/**
	 * Returns a base url with the REST relative path
	 * @return Base url with the REST relative path
	 */
	public String getRestUrl() {
		return escapeUrlAndToString(constructUrlWithRelativePath(getRestRelativeUrl()));
	}

	/**
	 * Returns a base url with the path to reset Sugar
	 * @return The URL to reset Sugar
	 */
	public String getRestoreUrl() {
		final String baseUrl = getBaseUrl();
		// Get the Fully Qualified Domain Name of the url
		// http://fqdn:xxx/sugar -> fdqn
		final String fqdn = baseUrl.split("/")[2].split(":")[0];
		return "http://" + fqdn + ":5000/api/v1.0/resetSugar";
	}

	/**
	 * Return a base url with the Portal relative path
	 * @return
	 */
	public String getPortalUrl() {
		String relativePath = VoodooUtils.getGrimoireConfig().getValue("env.portal_url", "portal");
		return escapeUrlAndToString(constructUrlWithRelativePath(relativePath));
	}

	/**
	 * Return the rest V10 relative path
	 * @return
	 */
	public String getRestRelativeUrl() {
		return VoodooUtils.getGrimoireConfig().getValue("restV10", "rest/v10");
	}

	/**
	 * Add the "coverageStartTest" parameter
	 * @param canonicaltestName A class represented in package format
	 * @return
	 */
	public SugarUrl enableSetupCodeCoverage(String canonicaltestName) {
		canonicaltestName = canonicaltestName.replaceAll("\\.", "/");
		String pathToTest = "src/test/java/" + canonicaltestName + ".java";
		addParameter("coverageStartTest", pathToTest);
		return this;
	}

	/**
	 * This allows users to add a parameter to the URL
	 * @param key
	 * @param value
	 * @return
	 */
	public SugarUrl addParameter(String key, String value) {
		parameters.put(key, value);
		return this;
	}

	private String getParallelAppender() {
		String parallelEnabled = voodooConfig.getValue("parallel.enabled");
		if("true".equals(parallelEnabled)) {
			String threadName = Thread.currentThread().getName();
			VoodooUtils.voodoo.log.info("Parallel is enabled. Appending '_" + threadName + "'");
			return "_" + threadName;
		}
		VoodooUtils.voodoo.log.info("Parallel is disabled.");
		return "";
	}

	private StringBuffer constructUrlWithRelativePath(String relativePath) {
		return constructBaseUrl().append("/").append(relativePath);
	}

	private String escapeUrlAndToString(StringBuffer buffer) {
		return buffer.append("/").toString();
	}

	private StringBuffer constructBaseUrl() {
		return new StringBuffer(baseUrl).append(getParallelAppender());
	}

	private String constructUrlParameters() {
		if(parameters.isEmpty()) {
			return "";
		}

		StringBuffer parameterString = new StringBuffer("?");

		Iterator i = parameters.entrySet().iterator();
		while(i.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry) i.next();
			parameterString.append(entry.getKey());
			parameterString.append("=");
			parameterString.append(entry.getValue());
			parameterString.append("&");
		}
		return parameterString.toString().replaceAll("&$", "");
	}
}
