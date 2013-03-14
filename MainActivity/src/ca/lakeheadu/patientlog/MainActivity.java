package ca.lakeheadu.patientlog;


import java.util.Calendar;
import java.util.List;

//import android.R.layout;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;



public class MainActivity extends Activity implements OnRatingBarChangeListener {
protected TextView lblRating;
public int numStars;
Context mContext;
AlarmManager am;
//Navigation Buttons are here
//-------------Home Page-------------------
public void goToHome(View v) {
	// TODO Auto-generated method stub
	setContentView(R.layout.home);	
}
//---------Question Page----------------
public void goToQuest(View v) {
	// TODO Auto-generated method stub
	setContentView(R.layout.activity_main);
	//Question Page Submit Button Press
	Button btnQSubmit =(Button) findViewById(R.id.btnQSubmit);
	final RatingBar myRatingBar = (RatingBar)findViewById(R.id.ratingBar1);
	lblRating = (TextView)findViewById(R.id.txtRDF);
	lblRating.setText("Please pick a star rating. 1 being worst and 5 being best.");
	
	//Set appropriate listener for change events
	myRatingBar.setOnRatingBarChangeListener(this);
	btnQSubmit.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			if(myRatingBar.getRating() != 0){
				//This is where SQL script should go
				DisplayToast("You have clicked the submit button on the question page!");
				myRatingBar.setEnabled(true);
				am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				setOneTimeAlarm();
			}
			else {
				//This is for when users either don't change the star rating or put it back to zero
				DisplayToast("Please pick a star rating");
			}
		}
	
//		private void DisplayToast(String msg) {
//			Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
//			
//		}
	});
}

public void DisplayToast(String msg) {
	Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
	
}

//------History Page---------
public void goToLog(View v) {
	// TODO Auto-generated method stub
	setContentView(R.layout.log);	
}

public void onStart(Bundle SavedInstanceState){
	super.onStart();
	setContentView(R.layout.home);
	
}

public void setOneTimeAlarm() {
	  Intent intent = new Intent(this, TimeAlarm.class);
	  PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
	    intent, PendingIntent.FLAG_ONE_SHOT);
	  // this will get the current date and time
		Calendar remindDate = Calendar.getInstance();
// this will set it for a week from now but for testing a week is too long		
//				remindDate.add(Calendar.DATE, 7);
// this will make sure it is noon when the notification goes off 		
//				remindDate.set(Calendar.HOUR_OF_DAY, 12);
				remindDate.add(Calendar.SECOND, 10); // this is for testing
	  am.set(AlarmManager.RTC_WAKEUP, remindDate.getTimeInMillis(), pendingIntent);
	  
	 }

    //@Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This if statement will tell us whether we are starting the program or
        // have come from the notification. If we come from the notification the 
        // program will start on the question screen. If it was not started from 
        // a notification it will start on the home screen.
        Bundle parameters = getIntent().getExtras();
        if(parameters != null)
        	setContentView(R.layout.activity_main);
        else
        	setContentView(R.layout.home);
        
        //Testing New Database
        //--------------------------------------------------------------------
        DatabaseHandler db = new DatabaseHandler(this);
     // Inserting Contacts
        //Log.d("Insert: ", "Inserting ..");
        //db.addPatientLog(new SqlPatientLog("Feb 26, 2013", 1, "Not feeling well"));
        //db.addPatientLog(new SqlPatientLog("Feb 25, 2013", 3, ""));
        //db.addPatientLog(new SqlPatientLog("Feb 24, 2013", 4, "Not bad today"));
        //db.addPatientLog(new SqlPatientLog("Feb 22, 2013", 5, "Really good today"));
 
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<SqlPatientLog> logs = db.getAllPatientLogs();       
 
        for (SqlPatientLog spl : logs) {
            String log = "Id: "+spl.getID()+" ,Date: " + spl.getDate() + " ,Rating: " + spl.getRating() + " ,Details: " + spl.getDetails();
                // Writing Patient Logs to log
        Log.d("Name: ", log);
    }

   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
//-------Variable text for slider bar on question page------- 
	@Override
	public void onRatingChanged(RatingBar ratingBar1, float arg1, boolean arg2) {
		RatingBar myRatingBar = (RatingBar)findViewById(R.id.ratingBar1);
		numStars = (int)myRatingBar.getRating();
		switch (numStars) {
		case 1:
			lblRating.setText("Terrible");
			break;
		case 2:
			lblRating.setText("Bad");
			break;	
		case 3:
			lblRating.setText("Average");
			break;
		case 4:
			lblRating.setText("Good");
			break;
		case 5:
			lblRating.setText("Amazing");
			break;
		default:
			lblRating.setText("Please pick a star rating. 1 being worst and 5 being best.");
			break;
		}
		
	}
	

}

