import java.util.ArrayList;
import java.util.List;

public class Pharmacist {
    private List<String[]> replenishmentRequests = new ArrayList<>(); // Store replenishment requests

    // Method to submit a replenishment request for low-stock medications
    public void submitReplenishmentRequest(String medicineName, int requestedQuantity, List<String[]> medicineList) {
        // Check if the medication is low in stock
        boolean medicationFound = false;
        for (String[] medicine : medicineList) {
            if (medicine[0].equalsIgnoreCase(medicineName)) {
                medicationFound = true;
                int currentStock = Integer.parseInt(medicine[1]); // Assuming second column is Initial Stock
                int lowStockLevel = Integer.parseInt(medicine[2]); // Assuming third column is Low Stock Level Alert

                // Verify if the current stock is below the low stock level
                if (currentStock < lowStockLevel) {
                    replenishmentRequests.add(new String[]{medicineName, String.valueOf(requestedQuantity)});
                    System.out.println("Replenishment request submitted for " + medicineName + " with quantity " + requestedQuantity);
                } else {
                    System.out.println("Current stock for " + medicineName + " is sufficient. No need for replenishment.");
                }
                break;
            }
        }

        if (!medicationFound) {
            System.out.println("Medication " + medicineName + " not found.");
        }
    }

    // Other methods...

    // Method to get the list of replenishment requests
    public List<String[]> getReplenishmentRequests() {
        return replenishmentRequests;
    }

    // Method to remove a replenishment request after it's approved
    public void removeReplenishmentRequest(int index) {
        replenishmentRequests.remove(index);
    }
}