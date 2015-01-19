/**
 * 
 */
package com.uoft.campusplannerapp;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;




/**
 * @author impaler
 *
 * The Main thread which contains the game loop. The thread must have access to 
 * the surface view and holder to trigger events every game tick.
 */
public class MainThread extends Thread {
	
	private static final String TAG = MainThread.class.getSimpleName();

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;
	// The actual view that handles inputs
	// and draws to the surface
	private MovingImage gamePanel;
	private Resources res; 

	// flag to hold game state 
	private boolean running;
	public void setRunning(boolean running, Resources res) {
		this.running = running;
		this.res = res;
	}

	public MainThread(SurfaceHolder surfaceHolder, MovingImage gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
		
	}

	@Override
	public void run() {
		
		// Resources res = getResources();
		
		Canvas canvas ;
		Log.d(TAG, "Starting game loop");
		while (running) {
			canvas = null;
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					// update game state 
					this.gamePanel.update();
					// render state to the screen
					// draws the canvas on the panel
					this.gamePanel.render(canvas);				
				}
			} finally {
				// in case of an exception the surface is not left in 
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}	// end finally
		}
	}

	private MainThread thread;
	
	
}
