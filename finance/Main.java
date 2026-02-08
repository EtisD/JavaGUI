package finance;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        SwingUtilities.invokeLater(LoginGUI::new);
        // 1. Initialize the Registry
        
        RegistryManager registry = new RegistryManager();
        registry.addRoom(new StandardRoom(101, 85.0));
        registry.addRoom(new StandardRoom(202, 52.0));
        registry.addRoom(new StandardRoom(303, 61.0));
        registry.addRoom(new StandardRoom(404, 29.0));
        
        FinanceManagement finance = new FinanceManagement();
        try {
            // --- ROOM SELECTION ---
            System.out.println("Pick between room 101 // 202 // 303 // 404");
            int choice = input.nextInt();
            input.nextLine(); // CLEAR BUFFER: Crucial after nextInt()

            Room selectedRoom = null;
            List<Room> allRooms = registry.getRooms();

            for (int i = 0; i < allRooms.size(); i++) {
                if (allRooms.get(i).getRoomNumber() == choice) {
                    selectedRoom = allRooms.get(i);
                    break; 
                }
            }
            
            // Validation: Did we find the room?
            if (selectedRoom == null) {
                throw new IllegalArgumentException("Room " + choice + " does not exist in the registry.");
            }

            // --- DYNAMIC GUEST INPUT WITH VALIDATION ---
            System.out.println("\n--- Guest Registration ---");
            
            System.out.print("Enter Guest Full Name: ");
            String name = input.nextLine();
         // This Regex only allows letters (a-z, A-Z) and spaces (\\s)
            if (name.trim().isEmpty() || !name.matches("^[a-zA-Z\\s]+$")) {
                throw new IllegalArgumentException("Invalid Name: Must contain only letters and spaces (no numbers or symbols).");
            }

            System.out.print("Enter Phone Number: ");
            String phone = input.nextLine();
            if (phone.length() < 7 || !phone.matches("^[+0-9-]+$")) {
                throw new IllegalArgumentException("Invalid Phone: Must be at least 7 digits and contain only numbers/symbols.");
            }

            System.out.print("Enter Email Address: ");
            String email = input.nextLine();
            if (!email.contains("@gmail.com")) {
                throw new IllegalArgumentException("Invalid Email: Format must be user@domain.com");
            }

            System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
            LocalDate checkIn = LocalDate.parse(input.nextLine());

            System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
            LocalDate checkOut = LocalDate.parse(input.nextLine());

            // 2. Create the Guest object
            Guest guest1 = new Guest(name, phone, email, checkIn, checkOut);

            // 3. Start Operations
            System.out.println("\n--- Starting Hotel Operations ---");
            registry.displayRooms();

            finance.createBooking(guest1, selectedRoom, checkIn, checkOut);

            System.out.println("Status after booking: " + (selectedRoom.isAvailable() ? "Available" : "Occupied"));

            // 4. Cancellation and Refund
            System.out.println("\n--- Processing Cancellation ---");
            finance.cancelAndRefund(selectedRoom, guest1);

            // 5. Final State
            registry.displayRooms();
            registry.saveData();
            
        } catch (IllegalArgumentException e) {
            System.out.println("\n[!] INPUT ERROR: " + e.getMessage());
            System.out.println("[!] Please restart and enter valid data.");
        } catch (DateTimeParseException e) {
            System.out.println("\n[!] DATE ERROR: Use YYYY-MM-DD format (e.g., 2026-05-20).");
        } catch (Exception e) {
            System.out.println("\n[!] UNEXPECTED ERROR: " + e.getMessage());
        } finally {
            System.out.println("\n--- System Session Ended ---");
            input.close();
        }
    }
}
