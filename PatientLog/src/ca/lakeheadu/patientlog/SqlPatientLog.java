package ca.lakeheadu.patientlog;

public class SqlPatientLog {
	
	//private variables
    int _id;
    String _date;
    int _rating;
    String _details;
    
    // Empty constructor
    public SqlPatientLog(){
 
    }
    // constructor
    public SqlPatientLog(int id, String date, int rating, String details){
        this._id = id;
        this._date = date;
        this._rating = rating;
        this._details = details;
    }
    
    public SqlPatientLog(String date, int rating, String details)
    {
    	this._date = date;
    	this._rating = rating;
    	this._details = details;
    }
 
    // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting ID
    public void setID(int id){
        this._id = id;
    }
 
    // getting date
    public String getDate(){
        return this._date;
    }
 
    // setting date
    public void setDate(String date){
        this._date = date;
    }
 
    // getting rating
    public int getRating(){
        return this._rating;
    }
 
    // setting rating
    public void setRating(int rating){
        this._rating = rating;
    }
    
    //getting details
    public String getDetails(){
    	return this._details;
    }
    
    //setting details
    public void setDetails(String details){
    	this._details = details;
    }
    
}
