package UserMain;

import Appointment.AppointmentManager;
import Appointment.DoctorAvailabilityManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Patient extends User {
    private String patientID;
    private String dob;
    private String gender;
    private String contactNo;
    private String email;
    private String bloodType;
    private String pastTreatment;
    private AppointmentManager appointmentManager;
    private DoctorAvailabilityManager availabilityManager;

    public Patient(String patientID, String password, String role, String name, String dob, String gender, String contactNo, String email, String bloodType, String pastTreatment, AppointmentManager appointmentManager, DoctorAvailabilityManager availabilityManager) {
        super(patientID, password, role, name); // Call the User constructor to initialize ID, password, role, and name
        this.patientID = patientID;
        this.dob = dob;
        this.gender = gender;
        this.contactNo = contactNo;
        this.email = email;
        this.bloodType = bloodType;
        this.pastTreatment = pastTreatment;
        this.appointmentManager = appointmentManager;
        this.availabilityManager = availabilityManager;
    }

    public void viewMedicalRecord() {
        System.out.println("Medical Record:");
        System.out.println("Patient ID: " + patientID);
        System.out.println("Name: " + getName());
        System.out.println("Date of Birth: " + dob);
        System.out.println("Gender: " + gender);
        System.out.println("Contact No: " + contactNo);
        System.out.println("Email: " + email);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Past Treatment: " + pastTreatment);
    }

    public void updatePersonalInfo(String newEmail, String newContactNo) {
        this.email = newEmail;
        this.contactNo = newContactNo;
        System.out.println("Personal information updated successfully.");
    }

    public void viewAvailableAppointmentSlots(String doctorID, String date) {
        String[] availableSlots = availabilityManager.viewDoctorAvailability(doctorID, date);
        System.out.println("Available Slots:");
        for (String slot : availableSlots) {
            System.out.println(slot);
        }
    }

    public void scheduleAppointment(String doctorID, String date, String timeSlot) {
        appointmentManager.scheduleAppointment(doctorID, patientID, date, timeSlot);
        System.out.println("Appointment scheduled successfully.");
    }

    public void rescheduleAppointment(String appointmentID, String newDate, String newTimeSlot) {
        appointmentManager.rescheduleAppointment(appointmentID, newDate, newTimeSlot);
        System.out.println("Appointment rescheduled successfully.");
    }

    public void cancelAppointment(String appointmentID) {
        appointmentManager.cancelAppointment(appointmentID);
        System.out.println("Appointment canceled successfully.");
    }

    public void viewScheduledAppointments(String appointmentID) {
        String status = appointmentManager.viewAppointmentStatus(appointmentID);
        System.out.println("Appointment Status: " + status);
    }

    public void viewPastAppointmentOutcome() {
        String recordFilePath = "src/Files/AppointmentRecord.csv";
        System.out.println("Past Appointment Outcomes for Patient ID: " + patientID);

        try (BufferedReader reader = new BufferedReader(new FileReader(recordFilePath))) {
            String line;
            boolean hasRecord = false;

            // Read each line and check if it matches this patient's ID
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields[0].equals(patientID)) {
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
                }
            }

            if (!hasRecord) {
                System.out.println("No past appointment records found for this patient.");
            }
        } catch (IOException e) {
            System.err.println("Error reading AppointmentRecord.csv: " + e.getMessage());
        }
    }
}
