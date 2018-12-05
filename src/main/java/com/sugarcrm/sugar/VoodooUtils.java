package com.sugarcrm.sugar;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.webdriver.WebDriverElement;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.automation.webdriver.WebDriverPause;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.sugar.views.Alerts;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides general utility functionality that is not part of the application
 * @author David Safar <dsafar@sugarcrm.com>
 */
public class VoodooUtils {
	public static Candybean voodoo;
	public static WebDriverInterface iface;
	private static final String currentWorkingPath = System.getProperty("user.dir");
	private static final String relativeResourcesPath = File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;
	private static Configuration grimoireConfig;

	public static void init() throws Exception {
		// Allow users to add configuration file through CLI using -Dvoodoo.conf=[path]
		String CLIvoodooPropsPath = System.getProperty("voodoo.conf");
		String candybeanPropsPath = currentWorkingPath + relativeResourcesPath + "candybean.properties";
		if (CLIvoodooPropsPath != null) {
			candybeanPropsPath = CLIvoodooPropsPath;
		}
		Configuration voodooConfig = new Configuration(new File(candybeanPropsPath));
		voodoo = Candybean.getInstance(voodooConfig);
		iface = voodoo.getAutomationInterfaceBuilder().build();

		String grimoirePropsPath = currentWorkingPath + relativeResourcesPath + "grimoire.properties";
		grimoireConfig = new Configuration(new File(grimoirePropsPath));
	}

	/**
	 * Gets the Grimoire Properties
	 * @return	a Configuration object representing the current Grimoire config.
	 */
	public static Configuration getGrimoireConfig() {
		return grimoireConfig;
	}

	/**
	 * Gets the Voodoo Properties
	 * @return	a Configuration object representing the current Voodoo config.
	 */
	public static Configuration getVoodooConfig(){
		return voodoo.config;
	}

	/**
	 * Launches the application on the currently defined platform
	 * @throws Exception
	 */
	public static void launchApp() throws Exception {
		voodoo.log.info("Launching app...");
		startInterface();

		iface.maximize();
		voodoo.log.info("App launched.");
	} // end launchApp()

	private static void startInterface() throws Exception {
		final int MAX_WAIT = 300000, CYCLE_LENGTH = 250;
		long iterationStartTime, iterationDuration;
		boolean browserNeeded = true;
		Exception lastException = null;
		String message = "";
		long totalTime = 0, startTime = System.currentTimeMillis();

		while(browserNeeded) {
			iterationStartTime = System.currentTimeMillis();

			try {
				iface.start();
				browserNeeded = false; // if we reach here, no exception was thrown.
				message = "Interface started. ";
			} catch (Exception e) {
				if(e.getMessage().contains("already started")) {
					iface.stop(); // reset for another try.
				} else {
					lastException = e;
				}
				message = "Exception caught while starting interface.  Retrying. ";
			}

			iterationDuration = System.currentTimeMillis() - iterationStartTime;
			if(iterationDuration < CYCLE_LENGTH)
				Thread.sleep(CYCLE_LENGTH - iterationDuration);
			totalTime = System.currentTimeMillis() - startTime;

			voodoo.log.info(message + iterationDuration + "ms this iteration, " + totalTime + "ms elapsed so far.");

			if(totalTime > MAX_WAIT) // when time runs out, throw the last exception.
			{
				throw lastException;
			}
		}
	}

	/**
	 * Closes the application (web browser, mobile app, etc.)
	 * @throws Exception
	 */
	public static void closeApp() throws Exception {
		iface.stop();
	}

	/**
	 * Loads a new URL.
	 * @throws Exception
	 */
	public static void go(String destination) throws Exception {
		iface.go(destination);
	}

	/**
	 * Returns the current page's URL as a String.
	 * @return	a String representation of the current page's URL.
	 * @throws Exception
	 */
	public static String getUrl() throws Exception {
		return iface.getURL();
	}

	/**
	 * Returns the title of the current page as a String.
	 * @return	a String representation of title of the current page
	 * @throws Exception
	 */
	public static String getTitle() throws Exception {
		return iface.wd.getTitle();
	}

	/**
	 * Navigates the page backward.
	 * @throws Exception
	 */
	public static void back() throws Exception {
		iface.backward();
	}

	/**
	 * Navigates the page forward.
	 * @throws Exception
	 */
	public static void forward() throws Exception {
		iface.forward();
	}

	/**
	 * Returns a WebDriverPause that provides a dynamic wait utility that accepts
	 * a variety of conditions
	 *
	 * @return
	 */
	public static WebDriverPause getPause() {
		return iface.getPause();
	}

	/**
	 * Gets a set of unique window identifiers and return
	 */
	public static List <String> getWindowHandles() {
		List <String> windowHandles = null;
		//TODO: Implement getWindowHandles
		return windowHandles;
	}//end getWindowHandles()

	/**
	 * Switches to the passed in window by its unique handle
	 */
	public static void switchToWindow(String windowHandle) {
		//TODO: Implement switchToWindow
	}//end switchToWindow()

	/**
	 * Closes the current browser window and shifts focus to the next (if present).
	 *
	 * @throws	EmptyStackException	if no more windows remain.
	 * @throws Exception
	 */
	public static void closeWindow() throws EmptyStackException, Exception {
		try {
			iface.closeWindow();
		} catch(EmptyStackException e)	{
			voodoo.log.info("Last window closed.");
		}
	}//end closeWindow()

	/**
	 * Takes a lower-case prefix and appends an existing camel-cased string,
	 * preserving camel-case for the resulting string (i.e. capitalizes the
	 * first letter of the existing string).
	 * @param prefix	the lower-case string for the beginning of the result
	 * @param body	the existing camel-cased string to capitalize and append
	 * @return	the resulting camel-cased string
	 */
	public static String prependCamelCase(String prefix, String body) {
		return prefix + capitalize(body);
	}

	/**
	 * Transforms a C-style string (lower-case, words separated by _s) into a
	 * Java-style (camel-cased, no _s) string.
	 * @param toCase	the string to convert to camel case
	 * @return	the camel-cased string
	 */
	public static String camelCase(String toCase) {
		String[] parts = toCase.split("_");
		String camelCaseString = "";
		for (String part : parts){
			camelCaseString = camelCaseString + capitalize(part);
		}
		return camelCaseString;
	}

	/**
	 * Capitalizes the first letter of the passed string.
	 * @param s	the string to capitalize
	 * @return the capitalized string
	 */
	static String capitalize(String s) {
		return s.substring(0, 1).toUpperCase() +
				s.substring(1).toLowerCase();
	}

	/**
	 * Switches focus to default content.
	 *
	 * @throws Exception
	 */
	public static void focusDefault() throws Exception {
		iface.focusDefault();
	}

	/**
	 * Switches focus to the IFrame identified by the given zero-based index
	 *
	 * @param index the serial, zero-based index of the iframe to focus
	 * @throws Exception
	 */
	public static void focusFrame(int index) throws Exception {
		iface.focusFrame(index);
	}

	/**
	 * Switches focus to the IFrame identified by the given name or ID string
	 *
	 * @param nameOrId the name or ID identifying the targeted IFrame
	 * @throws Exception
	 */
	public static void focusFrame(String nameOrId) throws Exception {
		iface.focusFrame(nameOrId);
	}

	/**
	 * Switches focus to the IFrame identified by the given {@link WebDriverElement}
	 *
	 * @param control The WebDriverElement representing a focus-targeted IFrame
	 * @throws Exception
	 */
	public static void focusFrame(WebDriverElement control) throws Exception {
		iface.focusFrame(control);
	}

	/**
	 * Focus a browser window by its index.
	 *
	 * <p>The order of browser windows is somewhat arbitrary and not
	 * guaranteed, although window creation time ordering seems to be
	 * the most common.</p>
	 *
	 * @param index  the window index
	 * @throws Exception	 if the specified window cannot be found
	 */
	public static void focusWindow(int index) throws Exception {
		Exception toThrow = null;
		long startTime = System.currentTimeMillis(), currentTime = 0;
		while(currentTime < 15000) {
			try {
				currentTime = System.currentTimeMillis() - startTime;
				iface.focusWindow(index);
				pause(250);
				break;
			} catch (CandybeanException e) {
				toThrow = e;
			}
		}
		if(currentTime > 15000 && toThrow != null) {
			throw new Exception("Could not focus on window with index: " + index, toThrow);
		}
	}

	/**
	 * Focus a browser window by its window title or URL.
	 *
	 * <p>If more than one window has the same title or URL, the first
	 * encountered is the one that is focused.</p>
	 *
	 * @param titleOrUrl the exact window title or URL to be matched
	 * @throws Exception if the specified window cannot be found
	 */
	public static void focusWindow(String titleOrUrl) throws Exception {
		Exception toThrow = null;
		long startTime = System.currentTimeMillis(), currentTime = 0;
		while(currentTime < 15000) {
			try {
				currentTime = System.currentTimeMillis() - startTime;
				iface.focusWindow(titleOrUrl);
				pause(250);
				break;
			} catch (CandybeanException e) {
				toThrow = e;
			}
		}
		if(currentTime > 15000 && toThrow != null) {
			throw new Exception("Could not focus on window with title or URL: " + titleOrUrl, toThrow);
		}
	}

	/**
	 * Pause the test for the specified duration.
	 *
	 * @param ms  duration of pause in milliseconds
	 * @throws Exception if the underlying {@link Thread#sleep} is interrupted
	 */
	public static void pause(long ms) throws Exception {
		Float waitScale = Float.parseFloat(grimoireConfig.getValue("wait_scale", "1.0"));
		if (waitScale < 0.f) waitScale  = 1.f;
		iface.pause((long)(ms*waitScale));
	}

	/**
	 * Checks for presence of a JavaScript dialog box.
	 *
	 * @throws Exception
	 * @return	true if a dialog is present; false if not
	 */
	public static boolean isDialogVisible() throws Exception {
		try {
			return iface.isDialogVisible();
		} catch (WebDriverException e) {
			if(e.getMessage().contains("no such session")) {
				voodoo.log.info("Last window closed.");
				return false;
			} else {
				throw e;
			}
		}
	}

	/**
	 * Click &quot;OK&quot; on a JavaScript dialog box.
	 *
	 * @throws Exception	 if no dialog box is present
	 */
	public static void acceptDialog() throws Exception {
		waitForDialog();
		iface.acceptDialog();
	}

	/**
	 * Dismisses a JavaScript dialog box.
	 *
	 * @throws Exception	 if no dialog box is present
	 */
	public static void dismissDialog() throws Exception {
		waitForDialog();
		iface.dismissDialog();
	}

	/**
	 * Returns true if the interface visibly contains the given string in any non-visible=false element.
	 *
	 * @param s The target string searched for in the interface
	 * @param caseSensitive	Whether or not the search is case sensitive
	 * @return Returns true if the interface visibly contains the given string
	 * @throws Exception
	 */
	public static boolean contains(String s, boolean caseSensitive) throws Exception {
		return iface.contains(s, caseSensitive);
	}

	/**
	 * Refresh the current webdriver attached browser window
	 * @throws Exception
	 */
	public static void refresh() throws Exception {
		// TODO: JIRA Ticket filed to add this functionality to Core so Library doesn't use the "wd" driver object from Core directly
		// VOOD-309
		iface.wd.navigate().refresh();
	}

	/**
	 * Waits up to 60s for an alert dialog box to not be visible.
	 * @throws Exception if no Alert Dialog exists or ms seconds have elapsed
	 * and the Alert Dialog still exists.
	 */
	public static void waitForAlertExpiration() throws Exception {
		// TODO: This should be reduced to 15000, see VOOD-1378
		waitForReady(60000);
	}

	/**
	 * Waits for an alert dialog box to not be visible.
	 * @param	ms	Timeout in milliseconds.
	 * @throws Exception if no Alert Dialog exists or ms seconds have elapsed
	 * and the Alert Dialog still exists.
	 */
	public static void waitForAlertExpiration(int ms) throws Exception {
		waitForReady(ms);
	}

	/**
	 * Waits up to 15s for a Javascript dialog box to be visible
	 * @throws Exception if no Dialog becomes visible or ms seconds have elapsed
	 */
	public static void waitForDialog() throws Exception {
		waitForDialog(15000);
	}

	/**
	 * Waits for a Javascript dialog box to be visible
	 * @param	ms	Timeout in milliseconds.
	 * @throws Exception if no Dialog becomes visible or ms seconds have elapsed
	 */
	public static void waitForDialog(int ms) throws Exception {
		long totalTime = 0;
		long iterationStartTime, iterationDuration = 0;
		Exception dialogException = null;

		while (totalTime < ms) {
			iterationStartTime = System.currentTimeMillis();

			try {
				if (isDialogVisible()) {
					return;
				}
			} catch (Exception e) {
				dialogException = e;
			}

			VoodooUtils.pause(250);
			iterationDuration = System.currentTimeMillis() - iterationStartTime;
			totalTime = totalTime + iterationDuration;
		}

		throw new TimeoutException("The Browser Dialog box did not show up in " + (ms / 1000) + " seconds. ", dialogException);
	}

	/**
	 * Gets Current Date and Time
	 * @param dateFormat format to be used when forming the date/time stamp e.g. "yyyy/MM/dd HH:mm"
	 * @return String representation of Date and Time e.g. "2013/07/10 16:45"
	 * @throws Exception
	 */
	public static String getCurrentTimeStamp(String dateFormat) throws Exception {
		SimpleDateFormat sdfDate = new SimpleDateFormat(dateFormat);
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	/**
	 * Convert a datetime expressed as multiple strings to ISO-8601 format.
	 * @param	date	formatted as "mm/dd/yyyy"
	 * @param	hours	formatted as "hh"
	 * @param	minutes	formatted as "mm"
	 * @param	meridiem	formatted as "a"
	 * @return	a datetime formatted as "yyyy-MM-dd'T'HH:mm:ssX"
	 * @throws	ParseException if the beginning of the internal date format string cannot be parsed.
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">Java date formats </a>
	 */
	public static String formatDateIso8601(String date, String hours, String minutes, String meridiem) throws ParseException {
		SimpleDateFormat oldFormat = new SimpleDateFormat("MM/dd/yyyy hh:mma", Locale.ENGLISH);
		SimpleDateFormat restFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH);
		Date toConvert = oldFormat.parse(date + " " + hours + ":" + minutes + meridiem);
		String convertedDate = restFormat.format(toConvert);
		return convertedDate;
	}

	/**
	 * Clears Local Storage
	 * @throws Exception
	 */
	public static void clearLocalStorage() throws Exception {
		executeJS("window.sessionStorage.clear();");
		executeJS("window.localStorage.clear();");
	}

	/**
	 * Run Javascript code snippet
	 *
	 * @param	jsCodeSnippet String containing arbitrary Javascript code. This is to be used
	 *          sparingly and only in rare cases where webElement.executeJavascript() do not apply
	 *
	 * @throws Exception
	 */
	public static Object executeJS(String jsCodeSnippet) throws Exception {
		voodoo.log.info("Executing JS.." + jsCodeSnippet);

		return iface.executeJavascript(jsCodeSnippet, "");
	}

	/**
	 * Wait until there are no more page requests and animations for up to maxWait seconds
	 *
	 * Waits for the javascript call to pass true to the given callback which occurs
	 * when the page is finished loading.
	 *
	 * The javascript function is in the VoodooSugarInterface repo and is located in
	 * the instance in custom/include/javascript/voodoo.js
	 *
	 * @param maxWait Maximum wait timeout in MS
	 * @throws TimeoutException
	 */

	public static void waitForReady(long maxWait) throws Exception {
		long totalTime = 0;
		long iterationStartTime, iterationDuration = 0;
		
		Alerts alert = new Alerts();

		while (totalTime < maxWait) {
			iterationStartTime = System.currentTimeMillis();

			if (!alert.getProcess().queryVisible()) {
				return;
			}
			
			VoodooUtils.pause(100);
			iterationDuration = System.currentTimeMillis() - iterationStartTime;
			totalTime = totalTime + iterationDuration;
		}
		VoodooUtils.voodoo.log.warning("waitForLoadingexpiration: Alert still visible. Waited: "+
				totalTime+"ms");
		throw new UnexpiredAlertException();
	}

	/**
	 * Wait for the page to be ready for up to 30 seconds
	 * @throws Exception
	 * @see #waitForReady(long)
	 */
	public static void waitForReady() throws Exception {
		waitForReady(15000);
	}

	public static void takeScreenshot() throws CandybeanException {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = sdf.format(now);
		String testname = "";

		StackTraceElement[] stackTraceElements = new Exception().getStackTrace();
		for(StackTraceElement line : stackTraceElements)
		{
			String lineString = line.toString();
			String patternString = "com\\.sugarcrm\\.test\\..*\\.(.*)\\.(.*)\\(..+\\..+\\.*\\)";
			if(lineString.startsWith("com.sugarcrm.test")) {
				Pattern p = Pattern.compile(patternString);
				Matcher m = p.matcher(lineString);
				if(m.find()) {
					testname = m.group(1) + "." + m.group(2);
					break;
				}
			}
		}

		iface.screenshot(new File("./log/" + dateString + "-" + testname + ".png"));
	}
}
