package com.example.foodadviser;

public interface Config {

	
	// CONSTANTS
	static final String YOUR_SERVER_URL =  "http://192.168.0.123/foodadviser/gcm/register.php";
	// YOUR_SERVER_URL : Server url where you have placed your server files
    // Google project id
    static final String GOOGLE_SENDER_ID = "949602824560";  // Place here your Google project id

    /**
     * Tag used on log messages.
     */
    static final String TAG = "Food adviser";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.example.foodadviser.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "New Food";
		
	
}
