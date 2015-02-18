package com.uoft.campusplannerapp;
 
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.uoft.campusplannerapp.Location;
import com.uoft.campusplannerapp.DatabaseHandler;
import com.uoft.campusplannerapp.R;

/*
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MovingImage extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MovingImage.class.getSimpleName();
	
	private MainThread thread;
	//private Droid droid;
	List<Droid> droid = new LinkedList<Droid>();
	private Context ctx;
	private Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen7);
	private DatabaseHandler db;

	public MovingImage(Context context) {
		super(context);
		ctx = context;
		db = new DatabaseHandler(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		Location Loc = new Location();
		Loc.GetLocation();
		setBitMap(Loc.getFloor());

		// create droid and load bitmap
		createDroid(Loc.getX(), Loc.getY());
		
		
		// create the game loop thread
		thread = new MainThread(getHolder(), this);
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
		try {
			thread.start();
		} catch (Exception e){
			thread.setRunning(false, getResources());
			Intent i = new Intent(ctx.getApplicationContext(), FriendActivity.class);
        	ctx.startActivity(i);
		}
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

	public Boolean render(Canvas canvas) {
		Bitmap scaled = Bitmap.createScaledBitmap(bitmap, this.getWidth(), this.getHeight(), true);
		if (canvas == null){
			return false;
		}
		canvas.drawBitmap(scaled, 0, 0, null);
		for (Droid l : droid) {
			l.draw(canvas);
		}
		return true;
	}

	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	private void setBitMap(int floor) {
		switch (floor){
		case 1:
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen1);
			break;
		case 2:
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen2);
			break;
		case 3:
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen3);
			break;
		case 4:
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen4);
			break;
		case 5:
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen5);
			break;
		case 6:
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen6);
			break;
		case 7:
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen7);
			break;
		case 8:
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen8);
			break;
		}
	}
	public void update() {
		int i;
		for (i = 0; i >100; i++){
			for (Droid l : droid){
				l.setY(i);
			}
		}
		List<Location> locs = db.getLocations();
		if (locs == null) return;
		for (Location l: locs){
			if (l.getPlot()){
				setBitMap(l.getFloor());
				droid.get(0).setX(l.getX());
				droid.get(0).setY(l.getY());
			}
			for (Droid d :droid) {
				d.update();
			}
		}
	}

}
