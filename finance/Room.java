package finance;


	import java.io.Serializable;
	
	//Variables needed for the room and the constructor

	public abstract class Room implements Serializable {

		private int roomNumber;

		private double pricePerNight;

		private boolean isAvailable;



		public Room(int roomNumber, double pricePerNight) {

			this.roomNumber = roomNumber;

			this.pricePerNight = pricePerNight;

			this.isAvailable = true; //We make it true by deafult.

		}

       //Getters and setters

		public int getRoomNumber() { 

			return roomNumber; 
		}
		
		public void setRoomNumber(int number) {
			this.roomNumber=number;
		}
		

		public double getPricePerNight() { 

			return pricePerNight; 
		}
		
		public void setPricePerNight(double price) {
			this.pricePerNight=price;
		}

		
		//VERY IMPORTANT  here we check the availability of a room, which by deafult should be true
		public boolean isAvailable() { 

			return isAvailable; 

		}
        //We set the availability to false when booking, and true after theyre done, they cancel
		public void setAvailable(boolean available) { 

			isAvailable = available; 

		}



		public abstract String getRoomType();



		@Override

		public String toString() {

			return "Room " + roomNumber + " [" + getRoomType() + "] - $" + pricePerNight + "/night";

		}

	}

