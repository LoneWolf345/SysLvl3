package com.StupidRat.SysLvl;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.ImageView;

/**
 * <p>Implement this interface if you want to receive events from the buttons of 
 * an actvity that derives from a LauncherActivity.</p>
 *
 * @author Martin Matysiak
 * @version 1.0
 */
interface LauncherButtonListener {
	/**
	 * Will be called when the user performs a click on a launcher button
	 * 
	 * @param launcherButton The LauncherButton object that triggered the event
	 */
	public void onClick(LauncherActivity.LauncherButton launcherButton);
	/**
	 * Will be called when the user performs a long press on a launcher button
	 * 
	 * @param launcherButton The LauncherButton object that triggered the event
	 */
	public boolean onLongClick(LauncherActivity.LauncherButton launcherButton);
}

/**
 * <p>A simple class that imitates the appearance of Google's "Car Home" application.
 * The user will see an extremely simple interface with 6 large buttons spread
 * across the screen.</p>
 * 
 * <p>The developer can modify text and icon of these buttons, but the general look
 * will stay the same. In contrast to the Car Home app, this activity supports
 * only one page of buttons.</p>
 * 
 * <p>When inherting this class, I advise to override the methods:</p>
 * <ul>
 * 	<li><pre>void onCreate(Bundle savedInstanceState)</pre> for making your own 
 *  preparations (remember to call super.onCreate(..)!). Additionally, make sure
 *  to not call setContentView(...) as it would destroy the launcher button
 *  layout.</li>
 * 	<li><pre>void onCreateLauncherButton(LauncherButton button)</pre> for modifying
 * 	the apparance of the buttons when they get created.</li>
 * 	<li><pre>void onClick(LauncherButton button)</pre> for handling click events.
 * 	<li><pre>void onLongClick(LauncherButton button)</pre> for handling longClick events.</li>
 * </ul> 
 * 
 * <b>Copyright (c) 2011 Martin Matysiak</b>
 * 
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:</p>
 * 
 * <p>The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.</p>
 * 
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.</p>
 * 
 * <p>If you like my software, I would appreciate if you send me a postcard.
 * You'll find my current address at http://martin-matysiak.de/en/sitenotice</p>
 * 
 * @author Martin Matysiak
 * @version 1.0
 */
public class LauncherActivity extends Activity implements LauncherButtonListener {
	
	/**
	 * <p>This is a wrapper class which provides an access to the very few methods
	 * needed for a button in the LauncherActivity. Everything else, including 
	 * the modification of the genral appearance of the button (apart from text 
	 * and icon) will be kept private.</p>
	 * 
	 * @author Martin Matysiak
	 * @version 1.0
	 */
	public class LauncherButton implements OnClickListener, OnLongClickListener {		
		private View fView;
		private int fPosition;
		private LauncherButtonListener fListener;
		
		/**
		 * Constructor of the class. By default, launcher buttons are in a 
		 * disabled state. You can change this by either calling .setEnabled(true)
		 * or by setting Text and/or Icon of the button.
		 * @param v The View which represents the LauncherButton (must use the layout
		 * launcher_button.xml)
		 * @param position The button's position in the launcher menu, an integer
		 * in the range 1..6 (positions are numbered from left to right, top to
		 * bottom)
		 */
		public LauncherButton(View v, int position) {
			fView = v;
			fPosition = position;
			
			// As long as no text or icon is set, the button shall be treated
			// as disabled
			fView.setEnabled(false);
		}
		
		/**
		 * <p>Returns the button's position in the launcher menu. The numbering is
		 * as follows:</p>
		 * 
		 * <p><b>Landscape:</b></p>
		 * <pre>
		 *   1 2 3<br />
		 *   4 5 6
		 * </pre>
		 * 
		 * <p><b>Portrait:</b></p>
		 * <pre>
		 * 1 2<br />
		 * 3 4<br />
		 * 5 6
		 * </pre>
		 * 
		 * @return An integer in the range 1..6, representing the button's
		 * position in the launcher menu
		 */
		public int getPosition() {
			return fPosition;
		}
		
		/**
		 * Sets the text which will be shown
		 * @param text A String that, due to space limitations, should be at most
		 * 20 to 25 characters long.
		 */
		public void setText(String text) {
			((TextView)fView.findViewById(R.id.label)).setText(text);
			fView.setEnabled(true);
		}
		
		/**
		 * Sets the icon of the LauncherButton
		 * @param d The drawable that shall be shown
		 */
		public void setIcon(Drawable d) {
			((ImageView)fView.findViewById(R.id.image)).setImageDrawable(d);
			fView.setEnabled(true);
		}
		
		/**
		 * Sets the icon of the LauncherButton
		 * @param resID A resourceID of a drawable that shall be shown
		 */
		public void setIcon(int resID) {
			((ImageView)fView.findViewById(R.id.image)).setImageResource(resID);
			fView.setEnabled(true);
		}
		
		/**
		 * Registers the given listener for receiving Click and LongClick events.
		 * The previous listener (if any) will stop receiving events!
		 * 
		 * @param l An instance of a class that implements the LauncherButtonListener 
		 * interface
		 */
		public void setButtonListener(LauncherButtonListener l) {
			fView.setOnClickListener(this);
			fView.setOnLongClickListener(this);
			fListener = l;
		}
		
		/**
		 * Changes the current state of the button.
		 * 
		 * @param enabled If true, there will be a visual feedback when pressing
		 * the button and the registered listener (if any) will receive the
		 * appropriate event. If false, the button won't produce any feedback at
		 * all.
		 */
		public void setEnabled(boolean enabled) {
			fView.setEnabled(enabled);
		}

		/**
		 * Although this method has to be public, you should not use it. It is 
		 * a listener for the underlying View that will then call the listener
		 * of this LauncherButton class 
		 * 
		 * @param v The underlying View object which contains the actual button
		 */
		@Override
		public void onClick(View v) {
			if (fListener != null) {
				fListener.onClick(this);
			}
		}

		/**
		 * Although this method has to be public, you should not use it. It is 
		 * a listener for the underlying View that will then call the listener
		 * of this LauncherButton class
		 * 
		 * @param v The underlying View object which contains the actual button
		 */
		@Override
		public boolean onLongClick(View v) {
			if (fListener != null) {
				return fListener.onLongClick(this);
			}
			return false;
		}
	}
	
	/**
	 * A simple map from the integer position of a button to its View resourceID
	 */
	private static final int[] icon_mapping = {
		-1, R.id.icon_1, R.id.icon_2, R.id.icon_3, R.id.icon_4, R.id.icon_5, R.id.icon_6
	};
	
	/**
	 * Will be called when the activity is created. Sets the basic look and feel
	 * of a launcher menu
	 * 
	 * @param savedInstanceState Not used in this class.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Set the proper theme (must be done _before_ calling super.onCreate() !)
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);
		
		// Generate launcher icons
		for (int p = 1; p <= 6; p++) {
			LauncherButton button = new LauncherButton(findViewById(icon_mapping[p]), p);
			
			button.setButtonListener(this);
			
			// Notify the child class of the creation
			onCreateLauncherButton(button);
		}
	}
	
	/**
	 * Used to set the appearance of a launcher button (i.e. Text and Icon). Should
	 * be overwritten by the inheriting class
	 * 
	 * @param launcherButton A launcherButton object which just got created. Use
	 * button.getPosition() to determine which button it is.
	 */
	public void onCreateLauncherButton(LauncherActivity.LauncherButton launcherButton) {
		// do nothing with it, this method should be overwritten
	}

	/**
	 * A listener for click events. Should be overwritten by the inheriting class
	 * 
	 * @param launcherButton The LauncherButton object that triggered the event
	 */
	@Override
	public void onClick(LauncherButton launcherButton) {
		// do nothing		
	}

	/**
	 * A listener for longClick events. Should be overwritten by the inheriting 
	 * class
	 * 
	 * @param launcherButton The LauncherButton object that triggered the event
	 */
	@Override
	public boolean onLongClick(LauncherButton launcherButton) {
		// do nothing
		return false;
	}
}