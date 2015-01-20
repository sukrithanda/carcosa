package com.uoft.campusplannerapp;
 
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/*
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MovingImage extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MovingImage.class.getSimpleName();
	
	private MainThread thread;
	//List private Droid[] droid;
    List<Droid> droid = new LinkedList<Droid>();



	public MovingImage(Context context) {
		
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create droid and load bitmap
		//droid = new Droid(BitmapFactory.decodeResource(getResources(), R.drawable.navpointer), 50, 50);
		createDroid(50, 50);
		createDroid(100, 50);
		createDroid(200, 50);


		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		//detector = new ScaleGestureDetector(getContext(), new ScaleListener());

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}
	
	public Droid createDroid(int x, int y){
		Random randomGenerator = new Random();
	    int randomInt = randomGenerator.nextInt(2);
	    int icon;
		if (randomInt == 0)
	    	 icon = R.drawable.navpointer;
		else if (randomInt == 1)
	    	 icon = R.drawable.navpointer2;
		else
	    	 icon = R.drawable.navpointer2;
		
		Droid l = new Droid(BitmapFactory.decodeResource(getResources(), icon), x, y);
		droid.add(l);
		return l;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true, getResources());
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}
	
	//@Override
	/*public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// delegating event handling to the droid
			droid.handleActionDown((int)event.getX(), (int)event.getY());
			
			// check if in the lower part of the screen we exit
			if (event.getY() > getHeight() - 50) {
				thread.setRunning(false, getResources());
				((Activity)getContext()).finish();
			} else {
				Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
			}
		} if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// the gestures
			if (droid.isTouched()) {
				// the droid was picked up and is being dragged
				droid.setX((int)event.getX());
				droid.setY((int)event.getY());
			}
		} if (event.getAction() == MotionEvent.ACTION_UP) {
			// touch was released
			if (droid.isTouched()) {
				droid.setTouched(false);
			}
		}
		return true;
		//detector.onTouchEvent(event);
		//return true;
		
	}*/

	public void render(Canvas canvas) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen7);
		Bitmap scaled = Bitmap.createScaledBitmap(bitmap, this.getWidth(), this.getHeight(), true);

		canvas.drawBitmap(scaled, 0, 0, null);
		for (Droid l : droid){
			l.draw(canvas);
		}
	}

	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {
		// check collision with right wall if heading right
	/*	if (droid.getSpeed().getxDirection() == Speed.DIRECTION_RIGHT
				&& droid.getX() + droid.getBitmap().getWidth() / 2 >= getWidth()) {
			droid.getSpeed().toggleXDirection();
		}
		// check collision with left wall if heading left
		if (droid.getSpeed().getxDirection() == Speed.DIRECTION_LEFT
				&& droid.getX() - droid.getBitmap().getWidth() / 2 <= 0) {
			droid.getSpeed().toggleXDirection();
		}
		// check collision with bottom wall if heading down
		if (droid.getSpeed().getyDirection() == Speed.DIRECTION_DOWN
				&& droid.getY() + droid.getBitmap().getHeight() / 2 >= getHeight()) {
			droid.getSpeed().toggleYDirection();
		}
		// check collision with top wall if heading up
		if (droid.getSpeed().getyDirection() == Speed.DIRECTION_UP
				&& droid.getY() - droid.getBitmap().getHeight() / 2 <= 0) {
			droid.getSpeed().toggleYDirection();
		}
		// Update the lone droid
		droid.update();*/
		int i;
		for (i = 0; i >100; i++){
			for (Droid l : droid){
				l.setY(i);
			}
			//droid.setY(i);
		}
		
		for (Droid l : droid){
			l.update();
		}
		//droid.update();
		
		//Need to use this to call localizeme()
	}

}
