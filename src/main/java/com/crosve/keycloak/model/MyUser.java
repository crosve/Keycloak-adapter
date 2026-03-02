package com.crosve.keycloak.model;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

//Import for getter and setters
import lombok.Data;
/**
 * @author Crosve Lucero, 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {
	//Class is implemented with camel case 

	//Keycloak data
	private int userID;
	
	private String email;
	private String username;
	private String password; 
	private String firstName; 
	private String lastName; 

	private boolean enabled; 
	private List<String> roles; 

	//360 Specific Data 
	private String birthday; 
    private String phoneNumber; 
	private String ethnicity; 
	private String gender; 
	private String state; 
	private String profilePictureURL; 
	private String termsAndConditions; 

	private String textMessaging; 

	private String confirmAge; 
	private int referredBy; 
	private String referrerLink; 
	private float  latitude; 
	private float longitude; 

	private boolean activated; 
	private String paymentInfo; 
	private String paymentType; 




	public MyUser(int userId, String firstName, String lastName, boolean enabled, List<String> roles ){
		this.userID = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enabled = enabled; 
		this.roles  = roles; 


	}




}