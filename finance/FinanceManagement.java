package finance;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class FinanceManagement {
	
	private static final double VAT_RATE = 0.15; // 15%
    private static final double REFUND_RATE = 0.80; // 80% Refund

  
   //We first create this private method to calculate the nights, so we dont have to repeat the same calculations in
    //in createBooking and in cancelAndRefund
    //We create a function where the booking will happen, cost will be calculated, and an invoice will be printed
    //all in one function
  //Since the format in both guest and here is LocalDate for the times of checkin, we just run the
    //predefined function, which will calculate the days between checkin and checkout
  //We force payment for minimum 1 day, even if they stay less
    
    
    private long calculateNights(Guest guest) {
        long nights = ChronoUnit.DAYS.between(guest.getCheckInDate(), guest.getCheckOutDate());
        return (nights <= 0) ? 1 : nights; // If 0 or less, make it 1
    }
   

    
    
    //Now we create a method for the 3rd point of the project, to deal with bookings and payment and invoice
    public void createBooking(Guest guest, Room room, LocalDate checkInStr, LocalDate checkOutStr) {
       
    	
    	//We check whether the room we got as input is valid
    	
    	int rNum = room.getRoomNumber();
        
        //Throw if the room is not one of the official four
        if (rNum != 101 && rNum != 202 && rNum != 303 && rNum != 404) {
            throw new IllegalArgumentException("Room " + rNum + " is not a valid selection for this hotel.");
        }
    
    	
    	
    	
    	if (!room.isAvailable()) { //Because in the room code Ergi made isAvailable true, it will only
        	                       //be false if we have setAvailable to false ourselves. 
            System.out.println("Error: Room " + room.getRoomNumber() + " is currently occupied.");
            return;//We print this to show that roomNumber is currently occupied and since its the same object, it will
                   //print its own number and not another by accident
        }
        
  
        long nights = calculateNights(guest); //Here we just call the private helper method instead of writing it here
                                              //and then having to rewrite it at cancelAndRefund aswell.

        //We assign the room (Updating Ergi's setAvailable) 
        room.setAvailable(false);

        //Financial Calculations (we couldve made a calculate method for these 3, but as i said i wanted
        //to keep everything in 1 method so we could just call it once and get the invoice and everything
        double subtotal = room.getPricePerNight() * nights; 
        double vatAmount = subtotal * VAT_RATE;
        double total = subtotal + vatAmount;

        System.out.println("\n========================================");
        System.out.println("          HOTEL BOOKING INVOICE         ");
        System.out.println("========================================");
        System.out.println(guest.toString()); 
        System.out.println("Room:         " + room.getRoomNumber() + " [" + room.getRoomType() + "]");
        System.out.println("Nights:       " + nights);
        System.out.println("----------------------------------------");
        //We use printf to format the numbers correctly, so they only go 0.00 (2 past the point)
        System.out.printf("Subtotal:     $%.2f\n", subtotal);
        System.out.printf("VAT (15%%):    $%.2f\n", vatAmount);
        System.out.printf("TOTAL PRICE:  $%.2f\n", total);
        System.out.println("========================================\n");
    }

 
    
    //Here we start work on the 4th point of our project, the cancel/refund
    public void cancelAndRefund(Room room, Guest guest) {//We use the room and guest as parameters to make it automated
    	
    	long nights=calculateNights(guest);//Here again we call the helper so we dont add unnecessary calculations from scratch everywhere
        
        //We update room status in Registry
        room.setAvailable(true);

        //We calculate the refund
        double totalOriginal = (room.getPricePerNight() * nights) * (1 + VAT_RATE); //Calculations of total into one variable
        double refundAmount = totalOriginal * REFUND_RATE;//the total is now multiplied by refund rate for final value

       //We print the message, saying the room is free and you get this much refund
        System.out.println(">> CANCELLATION SUCCESSFUL");
        System.out.println("Room " + room.getRoomNumber() + " is now available for new guests.");
        System.out.printf("Refund:     $%.2f\n",refundAmount);
    }
	
	
	
	

}
