package finance;

	class StandardRoom extends Room {
		
		//This is just a type of room, if we wanted to add suites or luxury rooms, we can add a new room like this, but for now theyre all standard

		public StandardRoom(int roomNumber, double pricePerNight) {

			super(roomNumber, pricePerNight); 

		}



		@Override

		public String getRoomType() {

			return "Standard";

		}

	}

