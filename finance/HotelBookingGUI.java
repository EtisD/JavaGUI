package finance;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HotelBookingGUI extends JFrame {
    private RegistryManager registry;
    private FinanceManagement finance;
    
    private JComboBox<Integer> roomDropdown;
    private JTextField nameField, phoneField, emailField, checkInField, checkOutField;
    private JTextArea logArea;
    private DefaultTableModel tableModel;
    
    private Set<Integer> cancelledRooms = new HashSet<>();

    public HotelBookingGUI() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        registry = new RegistryManager();
        registry.addRoom(new StandardRoom(101, 85.0));
        registry.addRoom(new StandardRoom(202, 52.0));
        registry.addRoom(new StandardRoom(303, 61.0));
        registry.addRoom(new StandardRoom(404, 29.0));
        finance = new FinanceManagement();

        setTitle("Luxury Stay - Hotel Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Guest & Booking Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        roomDropdown = new JComboBox<>();
        registry.getRooms().forEach(r -> roomDropdown.addItem(r.getRoomNumber()));
        
        nameField = new JTextField(30);
        phoneField = new JTextField(30);
        emailField = new JTextField(30);
        checkInField = new JTextField(LocalDate.now().toString());
        checkOutField = new JTextField(LocalDate.now().plusDays(1).toString());

        addFormField(formPanel, "Room Number:", roomDropdown, gbc, 0);
        addFormField(formPanel, "Guest Name:", nameField, gbc, 1);
        addFormField(formPanel, "Phone Number:", phoneField, gbc, 2);
        addFormField(formPanel, "Email Address:", emailField, gbc, 3);
        addFormField(formPanel, "Check-in:", checkInField, gbc, 4);
        addFormField(formPanel, "Check-out:", checkOutField, gbc, 5);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton bookBtn = new JButton("Confirm Booking");
    
        bookBtn.setBackground(new Color(46, 204, 113));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFocusPainted(false);
        
        JButton cancelBtn = new JButton("Process Refund");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);

        btnPanel.add(bookBtn);
        btnPanel.add(cancelBtn);
        gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        String[] columns = {"Room", "Guest", "Status", "Total Paid"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable statusTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(statusTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Current Occupancy"));

        logArea = new JTextArea(8,0);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(245, 245, 245));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("System Activity Log"));

        JSplitPane upperSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tableScroll);
        upperSplit.setDividerLocation(350);
        mainPanel.add(upperSplit, BorderLayout.CENTER);
        mainPanel.add(logScroll, BorderLayout.SOUTH);

        bookBtn.addActionListener(e -> handleBooking());
        cancelBtn.addActionListener(e -> handleCancellation());

        setVisible(true);
    }

    private void addFormField(JPanel panel, String label, JComponent comp, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 2;
        panel.add(comp, gbc);

    }

    private void handleBooking() {
        try {
            int roomNum = (Integer) roomDropdown.getSelectedItem();
            Room room = findRoom(roomNum);
            
            if (!room.isAvailable()) {
                JOptionPane.showMessageDialog(this, "Room " + roomNum + " is already occupied!");
                return;
            }

            Guest guest = new Guest(nameField.getText(), phoneField.getText(), emailField.getText(),
                    LocalDate.parse(checkInField.getText()), LocalDate.parse(checkOutField.getText()));

            finance.createBooking(guest, room, guest.getCheckInDate(), guest.getCheckOutDate());
            
            cancelledRooms.remove(roomNum); // Allow future cancellation for this new booking
            tableModel.addRow(new Object[]{roomNum, guest.getName(), "OCCUPIED", "N/A"});
            logArea.append("SUCCESS: Room " + roomNum + " booked for " + guest.getName() + "\n");
            registry.saveData();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void handleCancellation() {
        int roomNum = (Integer) roomDropdown.getSelectedItem();
        
        if (cancelledRooms.contains(roomNum)) {
            JOptionPane.showMessageDialog(this, "Error: A refund has already been processed for this booking!", 
                                          "Cancellation Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room room = findRoom(roomNum);
        if (room.isAvailable()) {
            JOptionPane.showMessageDialog(this, "Room is already vacant. Nothing to cancel.");
            return;
        }

        try {
            Guest dummyGuest = new Guest(nameField.getText(), phoneField.getText(), emailField.getText(),
                    LocalDate.parse(checkInField.getText()), LocalDate.parse(checkOutField.getText()));
            
            finance.cancelAndRefund(room, dummyGuest);
            
            cancelledRooms.add(roomNum);
            logArea.append("REFUNDED: Room " + roomNum + " has been credited back.\n");
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if ((int)tableModel.getValueAt(i, 0) == roomNum) {
                    tableModel.setValueAt("REFUNDED/VACANT", i, 2);
                }
            }
            registry.saveData();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Cancellation failed: " + ex.getMessage());
        }
    }

    private Room findRoom(int number) {
        return registry.getRooms().stream().filter(r -> r.getRoomNumber() == number).findFirst().orElse(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelBookingGUI::new);
    }
}
