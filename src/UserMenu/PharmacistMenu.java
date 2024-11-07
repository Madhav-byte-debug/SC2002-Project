package UserMenu;
import UserMain.Pharmacist;

public class PharmacistMenu extends AbstractMenu {
    private Pharmacist pharmacist;

    public PharmacistMenu(Pharmacist pharmacist){
        this.pharmacist = pharmacist;
    }
    @Override
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n==== Pharmacist Menu ====");
            System.out.println("(1) View Appointment Outcome Record");
            System.out.println("(2) Update Prescription Status");
            System.out.println("(3) View Medication Inventory");
            System.out.println("(4) Submit Replenishment Request");

            displayLogoutOption(5); // Call the common logout option method

            choice = sc.nextInt();
            sc.nextLine(); // Clear the newline character from the buffer

            switch (choice) {
                case 1:
                    // View appointment outcome record logic
                    System.out.println("Ph1");
                    break;
                case 2:
                    // Update prescription status logic
                    System.out.println("Ph2");
                    break;
                case 3:
                    // View medication inventory logic
                    System.out.println("Ph3");
                    break;
                case 4:
                    // Submit replenishment request logic
                    System.out.println("Ph4");
                    break;
                case 5:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5); // Repeat until logout
    }
}