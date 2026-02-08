package finance;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.List;

//Create arrays for rooms and guests
public class RegistryManager {
    private List<Room> rooms = new ArrayList<>();
    private List<Guest> guests = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void registerGuest(Guest guest) {
        guests.add(guest);
    }
    
    public List<Room> getRooms() { //Added by Markos to make the room choosing dynamic
        return rooms;
    }
    @SuppressWarnings("unchecked")
public void loadData() {
    File file = new File("hotel_data.txt");
    if (!file.exists()) return; // Nothing to load yet

    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        this.rooms = (List<Room>) ois.readObject();
        this.guests = (List<Guest>) ois.readObject();
        System.out.println("Data loaded successfully.");
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error loading data: " + e.getMessage());
    }
}
    
    //Saves data into file and catches input output exception
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("hotel_data.txt"))) {
            oos.writeObject(rooms);
            oos.writeObject(guests);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    //Display the rooms
    public void displayRooms() {
        System.out.println("\n~~ Current Room Registry ~~");
        for (Room r : rooms) 
        	System.out.println(r);
    }
}

