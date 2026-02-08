package finance;


	import java.io.Serializable;
	import java.time.LocalDate;


    //We initiate every variable we need for a guest, and call the constructor after
	public class Guest implements Serializable {

	    private String name;

	    private String phone;

	    private String email;

	    private LocalDate checkInDate;

	    private LocalDate checkOutDate;



	    public Guest(String name, String phone, String email,LocalDate checkIn,LocalDate checkOut) {

	        this.name = name;

	        this.phone = phone;

	        this.email = email;

	        this.checkInDate = checkIn;

	        this.checkOutDate = checkOut;

	    }



	    @Override

	    public String toString() {

	        return "Guest: " + name + " | Contact: " + phone + " | Stay: " + checkInDate + " to " + checkOutDate;

	    }
	    
	    //Getters and setters
	    
	    public String getName() {
	    	return name;
	    }
	    
	    public void setName(String name) {
	    	this.name=name;
	    }
	    
	    public String getPhone() {
	    	return phone;
	    }
	    
	    public void setPhone(String phone) {
	    	this.phone=phone;
	    }
	    
	    public String getEmail() {
	    	return email;
	    }
	    
	    public void setEmail(String email) {
	    	this.email=email;
	    }
	    
	    
	    public LocalDate getCheckInDate() {
	    	return checkInDate;
	    }
	    
	    public void setCheckInDate(LocalDate date) {
	    	this.checkInDate=date;
	    }
	    
	    public LocalDate getCheckOutDate() {
	    	return checkOutDate;
	    }
	    
	    public void setCheckOutDate(LocalDate date) {
	    	this.checkOutDate=date;
	    }
	    

	}

