package com.sugarcrm.sugar.views;

public class StudioTreeView extends View {
	protected static StudioTreeView view;
	
	private StudioTreeView() throws Exception {
		addControl("toggleTreePane", "div", "css", getHookString() + " #collapse_tree");
		addControl("toggleHelpPane", "div", "css", getHookString() + " #collapse_help");			
	}
	
	public static StudioTreeView getInstance() throws Exception {
		if (view == null)
			view = new StudioTreeView();
		return view;
	}
	
	/**
	 * Click the arrow toggle icon to expose/hide this views Left hand side Tree pane.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used, the left hand side Tree pane will be either exposed or hidden.
	 * 
	 * @return StudioTreeView instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioTreeView toggleTreePane() throws Exception {
		getControl("toggleTreePane").click();
		return this;
	}
	
	/**
	 * Click the arrow toggle icon to expose/hide this views Right hand side Help pane.
	 * <p>
	 * Must be in a Studio view to use.<br>
	 * When used, the right hand side Help pane will be either exposed or hidden.
	 * 
	 * @return StudioTreeView instance to enable chaining of methods wherever suitable
	 * @throws Exception
	 */
	public StudioTreeView toggleHelpPane() throws Exception {
		getControl("toggleHelpPane").click();
		return this;
	}
}