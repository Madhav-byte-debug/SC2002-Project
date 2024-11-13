package UserMain;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Random;
import enums.PrescriptionStatus;
import java.util.HashMap;
import java.util.Map;

public class Pharmacist extends User {
    private static List<String[]> replenishmentRequests = new ArrayList<>();// Store replenishment requests
    private static List<String[]> medicineList;
    private static final Map<String, Double> MEDICINE_PRICES = new HashMap<>();
    static {
        // Initialize medicine prices
        MEDICINE_PRICES.put("paracetamol", 0.125);
        MEDICINE_PRICES.put("ibuprofen", 0.50);
        MEDICINE_PRICES.put("amoxicillin", 0.95);
    }

    public Pharmacist(String id, String password, String role, String name){
        super(id, password, role, name);
    }

    public Pharmacist (String id){
        super(id);
    }

    public void viewAppointmentOutcome(String appointmentID) {
        String appointmentFilePath = "resources/Appointment.csv";
        String recordFilePath = "resources/AppointmentRecord.csv";
        System.out.println("Appointment Outcome for Appointment ID: " + appointmentID);

        // Step 1: Verify that the appointment is completed for this ID in Appointment.csv
        boolean isCompleted = false;

        try (BufferedReader appointmentReader = new BufferedReader(new FileReader(appointmentFilePath))) {
            String line = appointmentReader.readLine(); // Skip header line

            while ((line = appointmentReader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the line has the expected number of fields
                if (fields.length < 6) {
                    System.out.println("Skipping malformed line in Appointment.csv: " + line);
                    continue;
                }

                String currentAppointmentID = fields[0];
                String status = fields[5];

                // Check if this is the requested appointment and if it is completed
                if (currentAppointmentID.equals(appointmentID) && status.equalsIgnoreCase("completed")) {
                    isCompleted = true;
                    break;
                }
            }

            if (!isCompleted) {
                System.out.println("No completed appointment found for the given appointment ID.");
                return;
            }
        } catch (IOException e) {
            System.err.println("Error reading Appointment.csv: " + e.getMessage());
            return;
        }

        // Step 2: Read AppointmentRecord.csv to print details for the completed appointment
        try (BufferedReader recordReader = new BufferedReader(new FileReader(recordFilePath))) {
            String line;
            boolean hasRecord = false;

            while ((line = recordReader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the line has the expected number of fields
                if (fields.length < 9) {
                    continue;
                }

                String recordAppointmentID = fields[0];

                // If the record matches the completed appointment ID, print the outcome
                if (recordAppointmentID.equals(appointmentID)) {
                    hasRecord = true;
                    System.out.println("Appointment ID: " + fields[0]);
                    System.out.println("Diagnosis: " + fields[1]);
                    System.out.println("Prescription Medicine: " + fields[2]);
                    System.out.println("Prescription Quantity: " + fields[3]);
                    System.out.println("Prescription Status: " + fields[4]);
                    System.out.println("Treatment Plan: " + fields[5]);
                    System.out.println("Date: " + fields[6]);
                    System.out.println("Type of Service: " + fields[7]);
                    System.out.println("Consultation Notes: " + fields[8]);
                    System.out.println("-------------------------");
                    break;
                }
            }

            if (!hasRecord) {
                System.out.println("No past appointment record found for the given appointment ID.");
            }
        } catch (IOException e) {
            System.err.println("Error reading AppointmentRecord.csv: " + e.getMessage());
        }
    }

    public void updatePrescriptionStatus(String appointmentID) {
        String recordFilePath = "resources/AppointmentRecord.csv";
        String medicineFilePath = "resources/Medicine_List.csv";
        boolean appointmentFound = false;
        boolean stockSufficient = false;

        // Step 1: Read AppointmentRecord.csv to find the appointment and required medication details
        List<String[]> records = new ArrayList<>();
        String prescribedMedicine = "";
        int prescribedQuantity = 0;

        try (BufferedReader recordReader = new BufferedReader(new FileReader(recordFilePath))) {
            String line;

            while ((line = recordReader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if this line matches the requested appointmentID
                if (fields[0].equals(appointmentID)) {
                    appointmentFound = true;

                    prescribedMedicine = fields[2];
                    prescribedQuantity = Integer.parseInt(fields[3]);

                    if (!fields[4].equalsIgnoreCase(PrescriptionStatus.PENDING.name())) {
                        System.out.println("Prescription is already dispensed for this appointment.");
                        return;
                    }
                }
                records.add(fields);
            }
        } catch (IOException e) {
            System.err.println("Error reading AppointmentRecord.csv: " + e.getMessage());
            return;
        }

        if (!appointmentFound) {
            System.out.println("Appointment ID " + appointmentID + " not found.");
            return;
        }

        // Step 2: Read Medicine_List.csv to check stock
        List<String[]> medicineList = new ArrayList<>();

        try (BufferedReader medicineReader = new BufferedReader(new FileReader(medicineFilePath))) {
            String line;

            while ((line = medicineReader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[0].equalsIgnoreCase(prescribedMedicine)) {
                    int currentStock = Integer.parseInt(fields[1]);

                    if (currentStock >= prescribedQuantity) {
                        stockSufficient = true;
                        currentStock -= prescribedQuantity;
                        fields[1] = String.valueOf(currentStock); // Update stock
                        System.out.println("Dispensed " + prescribedQuantity + " units of " + prescribedMedicine + ". Updated stock: " + currentStock);
                    } else {
                        System.out.println("Insufficient stock for " + prescribedMedicine + ". Please submit a stock replenishment request.");
                        return;
                    }
                }
                medicineList.add(fields);
            }
        } catch (IOException e) {
            System.err.println("Error reading Medicine_List.csv: " + e.getMessage());
            return;
        }

        if (!stockSufficient) {
            System.out.println("Medicine " + prescribedMedicine + "not found.");
            return;
        }

        // Step 3: Update the prescription status in AppointmentRecord.csv
        try (BufferedWriter recordWriter = new BufferedWriter(new FileWriter(recordFilePath))) {
            for (String[] fields : records) {
                if (fields[0].equals(appointmentID)) {
                    fields[4] = PrescriptionStatus.DISPENSED.name(); // Update status to dispensed
                }
                recordWriter.write(String.join(",", fields));
                recordWriter.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to AppointmentRecord.csv: " + e.getMessage());
        }

        // Step 4: Write the updated Medicine_List.csv
        try (BufferedWriter medicineWriter = new BufferedWriter(new FileWriter(medicineFilePath))) {
            for (String[] fields : medicineList) {
                medicineWriter.write(String.join(",", fields));
                medicineWriter.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to Medicine_List.csv: " + e.getMessage());
        }

        // Step 5: Generate the bill after dispensing the medicine
        generateBill(appointmentID, prescribedMedicine, prescribedQuantity);
    }

    public void viewMedicationInventory() {
        String medicineFilePath = "resources/Medicine_List.csv";

        System.out.println("\n==== Medication Inventory ====");
        System.out.printf("%-20s %-15s %-20s%n", "Medicine Name", "Initial Stock", "Low Stock Level Alert");

        try (BufferedReader reader = new BufferedReader(new FileReader(medicineFilePath))) {
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                // Check if the line has the expected number of fields
                if (fields.length >= 3) {
                    String medicineName = fields[0].trim();
                    String initialStock = fields[1].trim();
                    String lowStockLevel = fields[2].trim();

                    System.out.printf("%-20s %-15s %-20s%n", medicineName, initialStock, lowStockLevel);
                } else {
                    System.out.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Medicine_List.csv: " + e.getMessage());
        }
    }

    public void submitReplenishmentRequest(String medicineName, int quantity) {
        String replenishmentFilePath = "resources/ReplenishmentRequest.csv";
        String status = PrescriptionStatus.PENDING.name();

        // Generate RRID with "RR" followed by 3 random digits
        String rrid = "RR" + String.format("%03d", new Random().nextInt(1000));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(replenishmentFilePath, true))) {
            // Write the replenishment request to the CSV file
            writer.write(rrid + "," + medicineName + "," + quantity + "," + status);
            writer.newLine();
            System.out.println("Replenishment request submitted for " + medicineName + " with quantity " + quantity + ". Status: " + status);
        } catch (IOException e) {
            System.err.println("Error writing to ReplenishmentRequest.csv: " + e.getMessage());
        }
    }

    public void generateBill(String appointmentID, String prescribedMedicine, int prescribedQuantity) {
        String billFilePath = "resources/Bill.csv";
        double unitPrice = MEDICINE_PRICES.getOrDefault(prescribedMedicine.toLowerCase(), 0.0);
        double billAmount = unitPrice * prescribedQuantity;

        // Set the status as "pending" and feedback as "na"
        String status = "pending";
        String feedback = "na";

        // Append the new bill entry to Bill.csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(billFilePath, true))) {
            writer.write(appointmentID + "," + billAmount + "," + status + "," + feedback);
            writer.newLine();
            //System.out.println("Bill generated for Appointment ID: " + appointmentID + " - Amount: $" + billAmount + " - Status: " + status);
        } catch (IOException e) {
            System.err.println("Error writing to Bill.csv: " + e.getMessage());
        }
    }
}
