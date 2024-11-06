import java.io.IOException;
import java.util.Scanner;
import java.lang.Math;


public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String id, password;
        int option =1;
        System.out.println("==== Login ====");
        System.out.println("[1] Patient\n"
        + "[2] Doctor\n"
        + "[3] Pharmacist\n"
        + "[4] Administrator\n");
        System.out.println("Enter your choice: ");
        option = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter your user ID: ");
        id = sc.nextLine();
        System.out.println("Enter your password: ");
        password = sc.nextLine();

        switch (option){
            case 1:
                Patient patient = new Patient(id, password);
                Appointment app = new Appointment();


                int pChoice;
                do {
                    System.out.println("\n==== Patient Menu ====");
                    System.out.println("(1) View Medical Record\n"
                            + "(2) Update Personal Information\n"
                            + "(3) View Available Appointment Slots\n"
                            + "(4) Schedule Appointment\n"
                            + "(5) Reschedule Appointment\n"
                            + "(6) Cancel Appointment \n"
                            + "(7) View Scheduled Appointments\n"
                            + "(8) View Past Appointment Outcome Records\n"
                            + "(9) Logout");
                    System.out.println("Enter your choice: ");
                    pChoice = sc.nextInt();
                    sc.nextLine();
                    switch (pChoice) {
                        case 1:
                            patient.viewMedicalRecord();
                            break;
                        case 2:
                            System.out.print("Enter new contact number: ");
                            String newContact = sc.nextLine();
                            System.out.print("Enter new email: ");
                            String newEmail = sc.nextLine();
                            patient.updateContactNo(newContact);
                            patient.updateEmail(newEmail);
                            System.out.println("Contact information updated successfully.");
                            break;
                        case 3:
                            System.out.println("Available appointment slots:");
                            for (Appointment appt : Appointment.appointmentOutRecord) {
                                if (appt.getStatus().equals("confirmed")) {
                                    System.out.println("Doctor ID: " + appt.getDoctorId() + ", Date: " + appt.getDate() + ", Time: " + appt.getTimeSlot());
                                }
                            }
                            break;
                        case 4:
                            try {
                                patient.scheduleAppointment();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case 5:
                            patient.reschedule();
                            break;
                        case 6:
                            patient.cancelAppointment();
                            break;
                        case 7:
                            patient.viewScheduledAppointments();
                            break;
                        case 8:
                            patient.viewPastRecords();
                            break;
                        case 9:
                            patient.logOut();
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }


                } while (option != 9);

                break;
            case 2:
                System.out.println("==== Doctor Menu ====");
                System.out.println("(1) View Patient Medical Records\n"
                        + "(2) Update Patient Medical Records\n"
                        + "(3) View Personal Schedule\n"
                        + "(4) Set Availability for Appointments\n"
                        + "(5) Accept or Decline Appointment Requests\n"
                        + "(6) View Upcoming Appointments\n"
                        + "(7) Record Appointment Outcome\n"
                        + "(8) Logout");
                break;

            case 3:
                Pharmacist ph = new Pharmacist(id);
                System.out.println("==== Pharmacist Menu ====");
                System.out.println("(1) View Appointmnet Outcome Record\n"
                        + "(2) Update Prescription Status\n"
                        + "(3) View Medication Inventory\n"
                        + "(4) Submit Replenishment Request\n"
                        + "(5) Logout");
                int pchoice;
                System.out.println("Enter your choice: ");
                pchoice = sc.nextInt();
                sc.nextLine();
                switch (pchoice) {
                    case 1:
                        //ph.();
                        break;
                    case 2:
                        int appointment = sc.nextInt();
                        sc.nextLine();
                        ph.dispenseMed(appointment);
                        break;
                    case 3:
                        ph.getMedicineList();
                        break;
                    case 4:
                        System.out.println("Enter medicine name: ");
                        String medName = sc.nextLine();
                        System.out.println("Enter quantity: ");
                        int quantity = sc.nextInt();
                        sc.nextLine();
                        ph.submitReplenishmentRequest(medName, quantity);
                        break;
                    case 5:
                        System.out.println("Logging out...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

                break;

            case 4:
                System.out.println("==== Administrator Menu ====");
                System.out.println("(1) View and Manage Hospital Staff\n"
                        + "(2) View Appointments Details\n"
                        + "(3) View and Manage Medication Inventory\n"
                        + "(4) Approve Replenishment Requests\n"
                        + "(8) Logout");
                break;

            case 5:
                break;

        }

    }
}