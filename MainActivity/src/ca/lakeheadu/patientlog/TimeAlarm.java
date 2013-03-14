package ca.lakeheadu.patientlog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* This class is waiting to receive the alarm that we have set up. When the alarm goes off
	this class will be called and a notification will be displayed. If the notification is 
	clicked then the program will start again.
*/
public class TimeAlarm extends BroadcastReceiver {
	
	NotificationManager nm;

	@Override
	public void onReceive(Context context, Intent intent) {
	  nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//	this is how we set what class to call when the notification is selected
	  Intent i = new Intent(context, MainActivity.class);
// added the extra info so that the notification will start on the question screen	
	 String question = "question";
	i.putExtra("whichlayout", question);
// the intent has to go into a pending intent because it will not happen right away	 
		  PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

		  Notification notif = new Notification.Builder(context)
			 .setContentTitle("Health Log")
		     .setContentText("You haven't recorded your health rating in a while").setSmallIcon(R.drawable.ic_launcher)
		     .setContentIntent(pendingIntent).build();
// this flag causes the notification to clear itself automatically when selected		  
		  notif.flags |= Notification.FLAG_AUTO_CANCEL;
		  nm.notify(1, notif);
		  
		  
	  
		 }
		}
