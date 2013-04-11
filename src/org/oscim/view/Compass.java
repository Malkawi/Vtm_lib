

package org.oscim.view;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class Compass implements SensorEventListener {
	private static final String TAG = "Compass";

	private final SensorManager sensorManager;
	private final Sensor gsensor;
	private final Sensor msensor;
	private final float[] mGravity = new float[3];
	private final float[] mGeomagnetic = new float[3];
	public float azimuth = 0f;
	public float currectAzimuth = 0;
  public MapView map ;
	// compass arrow to rotate
	public ImageView arrowView = null;

	public Compass(Context context , MapView ma) {
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		map=ma;
	}

	public void start() {
		sensorManager.registerListener(this, gsensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, msensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		map.getMapPosition().setRotation(-currectAzimuth);
	}

	public void stop() {
		sensorManager.unregisterListener(this);
	}

	public void adjustArrow() {
		if (arrowView == null) {
			Log.i(TAG, "arrow view is not set");
			return;
		}

		Log.i(TAG, "will set rotation from " + currectAzimuth + " to "
				+ azimuth);

		Animation an = new RotateAnimation(-currectAzimuth, -azimuth,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		currectAzimuth = azimuth;

		an.setDuration(500);
		an.setRepeatCount(0);
		an.setFillAfter(true);

		arrowView.startAnimation(an);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		final float alpha = 0.97f;

		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				mGravity[0] = alpha * mGravity[0] + (1 - alpha)
						* event.values[0];
				mGravity[1] = alpha * mGravity[1] + (1 - alpha)
						* event.values[1];
				mGravity[2] = alpha * mGravity[2] + (1 - alpha)
						* event.values[2];

				// mGravity = event.values;

				// Log.e(TAG, Float.toString(mGravity[0]));
			}

			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				// mGeomagnetic = event.values;

				mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
						* event.values[0];
				mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
						* event.values[1];
				mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
						* event.values[2];
				// Log.e(TAG, Float.toString(event.values[0]));

			}

			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				// Log.d(TAG, "azimuth (rad): " + azimuth);
				azimuth = (float) Math.toDegrees(orientation[0]); // orientation
				azimuth = (azimuth + 360) % 360;
				// Log.d(TAG, "azimuth (deg): " + azimuth);
				adjustArrow();
			if(Math.abs(azimuth - angle)> 2){

				angle=azimuth;
				this.map.getMapPosition().setRotation(-this.azimuth);
				map.redrawMap(true);
			}
			}
		}


	}
public void rest (){


	currectAzimuth =0 ;
	azimuth=0;
	adjustArrow();
	this.map.getMapPosition().setRotation(-this.currectAzimuth);
	map.redrawMap(true);

}

	 private float angle ;
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {


	}
}
