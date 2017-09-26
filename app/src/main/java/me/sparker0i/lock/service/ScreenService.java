package me.sparker0i.lock.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import me.sparker0i.lock.receiver.ScreenReceiver;

public class ScreenService extends Service{

	private ScreenReceiver receiver = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		receiver = new ScreenReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);
		filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
		registerReceiver(receiver , filter);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		if(intent !=null & intent.getAction() == null & receiver == null) {
			receiver = new ScreenReceiver();
			IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
			registerReceiver(receiver, filter);
            filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
            registerReceiver(receiver , filter);
		}
		
		//Notification notification = new Notification(R.drawable.ic_launcher, "서비스 실행됨", System.currentTimeMillis());
		//notification.setLatestEventInfo(getApplicationContext(), "Screen Service", "Foreground로 실행됨", null);
		startForeground(1, new Notification());
		
		return START_REDELIVER_INTENT;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(receiver != null)
			unregisterReceiver(receiver);
	}
}
