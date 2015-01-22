package com.uoft.campusplannerapp;
 
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;


import com.uoft.campusplannerapp.LocateDevice;

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
	private ScaleGestureDetector detector;
	private Context ctx;
	private Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen7);
	private Handler handler; 

	public MovingImage(Context context) {
		super(context);
		ctx = context;
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		LocateDevice Loc = new LocateDevice();
		Loc.GetLocation();
		setBitMap(Loc.getFloor());

		// create droid and load bitmap
		//droid = new Droid(BitmapFactory.decodeResource(getResources(), R.drawable.navpointer), Loc.getX(), Loc.getY());
		createDroid(Loc.getX(), Loc.getY());
		
		
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
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			// delegating event handling to the droid
//			droid.handleActionDown((int)event.getX(), (int)event.getY());
//			
//			// check if in the lower part of the screen we exit
//			if (event.getY() > getHeight() - 50) {
//				thread.setRunning(false, getResources());
//				((Activity)getContext()).finish();
//			} else {
//				Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
//			}
//		} if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			// the gestures
//			if (droid.isTouched()) {
//				// the droid was picked up and is being dragged
//				droid.setX((int)event.getX());
//				droid.setY((int)event.getY());
//			}
//		} if (event.getAction() == MotionEvent.ACTION_UP) {
//			// touch was released
//			if (droid.isTouched()) {
//				droid.setTouched(false);
//			}
//		}
//		return true;
//		//detector.onTouchEvent(event);
//		//return true;
//		
//	}

	public Boolean render(Canvas canvas) {
		//Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bahen7);
		Bitmap scaled = Bitmap.createScaledBitmap(bitmap, this.getWidth(), this.getHeight(), true);
		//canvas = new Canvas(bitmap.copy(Bitmap.Config.ARGB_8888, true));
		if (canvas == null){
			return false;
		}
		canvas.drawBitmap(scaled, 0, 0, null);
		//canvas.drawColor(Color.BLACK);
		//droid.draw(canvas);
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
		}
		SharedPreferences pref = ctx.getSharedPreferences("Locations", Context.MODE_PRIVATE);
		String set = pref.getString("set", "false");
		if (set.equals("true")){
		String result = pref.getString("result", null);
			if (result != null){
				String[] params = result.split(Pattern.quote("&"));
				try {
					String friend = params[0].split(Pattern.quote("="))[1];
					String bldg = params[1].split(Pattern.quote("="))[1];
					String floor = params[2].split(Pattern.quote("="))[1];
					String x = params[3].split(Pattern.quote("="))[1];
					String y = params[4].split(Pattern.quote("="))[1];
					
					setBitMap(Integer.parseInt(floor));
					int ix = Integer.parseInt(x);
					int iy = Integer.parseInt(y);
					droid.get(0).setX(ix);
					droid.get(0).setY(iy);
					Editor edit = pref.edit();
					edit.putString("set", "false");
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (Droid l :droid) {
					l.update();
				}
			}
		}
		//Need to use this to call localizeme()
	}

}
