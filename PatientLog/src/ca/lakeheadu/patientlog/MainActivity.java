package ca.lakeheadu.patientlog;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.series.XYSeries;
import com.androidplot.xy.*;
import com.androidplot.Plot;
import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


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



//-------------Graph Page------------------
@SuppressWarnings("deprecation")
public void goToGraph(View v) {
	//DatabaseHandler db = new DatabaseHandler(this);
	
	
	//List<SqlPatientLog> logs = db.getAllPatientLogs(); 
	setContentView(R.layout.graph);
	
	XYPlot mySimpleXYPlot;


        // initialize our XYPlot reference:
        mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);

        // Create a couple arrays of y-values to plot:
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.CANADA);
        //Calendar cal = Calendar.getInstance();
       
        Number[] ratings = {1,3,8,19,56};
        Number[] dates = {3,7,8,10,34};
        
       
   
        // Turn the above arrays into XYSeries':
        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(dates),
                Arrays.asList(ratings),
                "Patient Ratings");                             // Set the display title of the series

        mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        mySimpleXYPlot.getGraphWidget().getGridLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().getGridLinePaint().setPathEffect(new DashPathEffect(new float[]{1,1}, 1));
        mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);

        mySimpleXYPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
        mySimpleXYPlot.getBorderPaint().setStrokeWidth(1);
        mySimpleXYPlot.getBorderPaint().setAntiAlias(false);
        mySimpleXYPlot.getBorderPaint().setColor(Color.WHITE);

        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        

        // setup our line fill paint to be a slightly transparent gradient:
        Paint lineFill = new Paint();
        lineFill.setAlpha(200);
        lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.WHITE, Color.GREEN, Shader.TileMode.MIRROR));

        LineAndPointFormatter formatter  = new LineAndPointFormatter(Color.rgb(0, 0,0), Color.BLUE, Color.RED);
        formatter.setFillPaint(lineFill);
        mySimpleXYPlot.getGraphWidget().setPaddingRight(2);
        mySimpleXYPlot.addSeries(series2, formatter);

        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        mySimpleXYPlot.disableAllMarkup();
    
}

//------------Admin Page------------------
public void goToAdmin(View v) {
	final DatabaseHandler db = new DatabaseHandler(this);
	setContentView(R.layout.admin);
	
	Button btndropTable = (Button)findViewById(R.id.btndropTable);
	Button btnFillTable = (Button)findViewById(R.id.btnFillTable);
	btndropTable.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v){
			db.dropTable();
			Toast.makeText(getBaseContext(), "Table dropped and re-created", Toast.LENGTH_SHORT).show();
		}
	});
	
	btnFillTable.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v){
			db.addPatientLog(new SqlPatientLog("Feb 25, 2013", 1, "Not feeling well"));
	        db.addPatientLog(new SqlPatientLog("Feb 26, 2013", 3, ""));
	        db.addPatientLog(new SqlPatientLog("Feb 27, 2013", 4, "Not bad today"));
	        db.addPatientLog(new SqlPatientLog("Feb 28, 2013", 5, "Really good today"));
	        db.addPatientLog(new SqlPatientLog("Mar 12, 2013", 3, "Average Day"));
	        Toast.makeText(getBaseContext(), "Dummy data added to table", Toast.LENGTH_SHORT).show();
		}
	});
	
	
}


//---------Question Page----------------
public void goToQuest(View v) {
	// TODO Auto-generated method stub
	setContentView(R.layout.activity_main);
	final DatabaseHandler db = new DatabaseHandler(this);
	
	//Question Page Submit Button Press
	Button btnQSubmit =(Button) findViewById(R.id.btnQSubmit);
	final RatingBar myRatingBar = (RatingBar)findViewById(R.id.ratingBar1);
	lblRating = (TextView)findViewById(R.id.txtRDF);
	lblRating.setText("Please pick a star rating. 1 being worst and 5 being best.");
	final EditText et = (EditText) findViewById(R.id.editText1);
	
	
		
	
	//Set appropriate listener for change events
	myRatingBar.setOnRatingBarChangeListener(this);
	btnQSubmit.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			if(myRatingBar.getRating() != 0){
				//This is where SQL script should go
				
				myRatingBar.setEnabled(true);
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.CANADA);
				Calendar cal = Calendar.getInstance();
				Date today = cal.getTime();
				
				String myDate = sdf.format(today);
				int rating = (int)myRatingBar.getRating();
				String details = et.getText().toString();
							
				db.addPatientLog(new SqlPatientLog(myDate,rating,details));
				
				am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				setOneTimeAlarm();
								
				DisplayToast("Your log has been successfully updated!");
				goToHome(v);
			}
			else {
				//This is for when users either don't change the star rating or put it back to zero
				DisplayToast("Please pick a star rating");
			}
		
		}
	
		public void setOneTimeAlarm() {
			  Intent intent = new Intent(MainActivity.this, TimeAlarm.class);
			  PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
			  // this will get the current date and time
				Calendar remindDate = Calendar.getInstance();
		// this will set it for a week from now but for testing a week is too long		
//						remindDate.add(Calendar.DATE, 7);
		// this will make sure it is noon when the notification goes off 		
//						remindDate.set(Calendar.HOUR_OF_DAY, 12);
						remindDate.add(Calendar.SECOND, 10); // this is for testing
			  am.set(AlarmManager.RTC_WAKEUP, remindDate.getTimeInMillis(), pendingIntent);

			 }
		
		private void DisplayToast(String msg) {
			Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
			
		}
	});
	
}
//------History Page---------

public void goToLog(View v) {
	// TODO Auto-generated method stub
	setContentView(R.layout.log);	
	
	//Testing New Database
    //--------------------------------------------------------------------
    final DatabaseHandler db = new DatabaseHandler(this);
 // Inserting Contacts
    //Log.d("Insert: ", "Inserting ..");
    //db.addPatientLog(new SqlPatientLog("Feb 26, 2013", 1, "Not feeling well"));
    //db.addPatientLog(new SqlPatientLog("Feb 25, 2013", 3, ""));
    //db.addPatientLog(new SqlPatientLog("Feb 24, 2013", 4, "Not bad today"));
    //db.addPatientLog(new SqlPatientLog("Feb 22, 2013", 5, "Really good today"));

    // Reading all contacts
   
    final List<SqlPatientLog> logs = db.getAllPatientLogs();       
    TableLayout t1;
	t1 = (TableLayout) findViewById(R.id.tableLayout1);
	
	TableRow tr_head = new TableRow(this);
	tr_head.setId(10);
	tr_head.setBackgroundColor(Color.GRAY);
	tr_head.setLayoutParams(new LayoutParams(
	LayoutParams.MATCH_PARENT,
	LayoutParams.WRAP_CONTENT));
	
	TextView label_date = new TextView(this);
	label_date.setId(20);
	label_date.setText("Date");
	label_date.setPadding(5, 5, 5, 5);
	label_date.setTextColor(Color.WHITE);
	tr_head.addView(label_date);
	
	TextView label_rating = new TextView(this);
	label_rating.setId(21);
	label_rating.setText("Rating");
	label_rating.setPadding(5, 5, 5, 5);
	label_rating.setTextColor(Color.WHITE);
	tr_head.addView(label_rating);
	
	TextView label_description = new TextView(this);
	label_description.setId(22);
	label_description.setText("Description");
	label_description.setPadding(5, 5, 5, 5);
	label_description.setTextColor(Color.WHITE);
	tr_head.addView(label_description);
	
	t1.addView(tr_head, new TableLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT));
	
	int count = 1;
    for (SqlPatientLog spl : logs) {
    	
    	
    	TableRow tr = new TableRow(this);
    	
    	if (count % 2 == 0 )
    	{
    		tr.setBackgroundColor(Color.LTGRAY);
    	}
    	
    	tr.setId(100 + count);
    	tr.setLayoutParams(new LayoutParams(
    			LayoutParams.MATCH_PARENT,
    			LayoutParams.WRAP_CONTENT));
    	
    	TextView labelDATE = new TextView(this);
    	labelDATE.setId(200 + count);
    	labelDATE.setText(spl.getDate());
    	labelDATE.setPadding(5, 5, 5, 5);
    	labelDATE.setTextColor(Color.BLACK);
    	tr.addView(labelDATE);
    	
    	TextView labelRATING = new TextView(this);
    	labelRATING.setGravity(Gravity.CENTER);
    	labelRATING.setId(200 + count);
    	labelRATING.setText(Integer.toString(spl.getRating()));
    	labelRATING.setPadding(5, 5, 5, 5);
    	labelRATING.setTextColor(Color.BLACK);
    	tr.addView(labelRATING);
    	
    	TextView labelDESCRIPTION = new TextView(this);
    	labelDESCRIPTION.setId(200 + count);
    	labelDESCRIPTION.setText(spl.getDetails());
    	labelDESCRIPTION.setPadding(5, 5, 5, 5);
    	labelDESCRIPTION.setTextColor(Color.BLACK);
    	tr.addView(labelDESCRIPTION);
    	
    	t1.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
    	
       
        
        count++;
    }
	
    Button btnExport =(Button) findViewById(R.id.btnExport);
	btnExport.setOnClickListener(new View.OnClickListener() {
		
		
		public void onClick(View v) 
		{
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File (sdCard.getAbsolutePath() + "/PatientLog");
			dir.mkdirs();
			File file = new File(dir, "log.csv");
			try {
			FileOutputStream f = new FileOutputStream(file);
			} catch (FileNotFoundException e1) {
				 //TODO Auto-generated catch block
				e1.printStackTrace();
			}
			CSVWriter writer = null;
			try {
				writer = new CSVWriter(new FileWriter(file), '\t');
				 // feed in your array (or convert your data to an array)
				List<String[]> logs = db.getLogString();

			     writer.writeAll(logs);

				try {
					writer.close();
					Toast.makeText(getBaseContext(), "The log has been successfully exported!", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			
			
		}
	});
    	
};
public void onStart(Bundle SavedInstanceState){
	super.onStart();
	setContentView(R.layout.home);
}
    //@Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // This if statement will tell us whether we are starting the program or
        // have come from the notification. If we come from the notification the 
        // program will start on the question screen. If it was not started from 
        // a notification it will start on the home screen.
        Bundle parameters = getIntent().getExtras();
        if(parameters != null)
        	goToQuest(null);
        else
        	setContentView(R.layout.home);
   }
        
   public void goToClose(View v) {

		setContentView(R.layout.activity_main);
		Toast.makeText(getBaseContext(), "Closing", Toast.LENGTH_SHORT).show();
		finish();

		System.exit(0);
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

