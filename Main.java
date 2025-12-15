class Customer {
    private int id;
    private String name;
    private String phone;

    public Customer(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return id + " - " + name + " (" + phone + ")";
    }
}

class Service {
    private int id;
    private String name;
    private double price;
    private int durationMinutes;

    public Service(int id, String name, double price, int durationMinutes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String toString() {
        return id + " - " + name + " | Rs. " + price + " | " + durationMinutes + " mins";
    }
}

class Appointment {
    private int id;
    private Customer customer;
    private Service service;
    private LocalDateTime dateTime;

    public Appointment(int id, Customer customer, Service service, LocalDateTime dateTime) {
        this.id = id;
        this.customer = customer;
        this.service = service;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Service getService() {
        return service;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "[" + id + "] " + dateTime.format(formatter) + " - " +
                customer.getName() + " - " + service.getName();
    }
}

class AppointmentSystem {
    private List<Customer> customers = new ArrayList<>();
    private List<Service> services = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();

    private int nextCustomerId = 1;
    private int nextServiceId = 1;
    private int nextAppointmentId = 1;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ----- CUSTOMER METHODS -----

    public void addCustomer(Scanner scanner) {
        System.out.println("\n--- Add Customer ---");
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }

        Customer c = new Customer(nextCustomerId++, name, phone);
        customers.add(c);
        System.out.println("Customer added: " + c);
    }

    public void listCustomers() {
        System.out.println("\n--- Customers ---");
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        for (Customer c : customers) {
            System.out.println(c);
        }
    }

    private Customer findCustomerById(int id) {
        for (Customer c : customers) {
            if (c.getId() == id) return c;
        }
        return null;
    }
    // ----- SERVICE METHODS -----

    public void addService(Scanner scanner) {
        System.out.println("\n--- Add Service ---");
        System.out.print("Enter service name (e.g., Cleaning, Haircut): ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter price: ");
        String priceStr = scanner.nextLine().trim();

        System.out.print("Enter duration in minutes: ");
        String durationStr = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Service name cannot be empty.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int duration = Integer.parseInt(durationStr);

            Service s = new Service(nextServiceId++, name, price, duration);
            services.add(s);
            System.out.println("Service added: " + s);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price or duration. Service not added.");
        }
    }

public void listServices() {
        System.out.println("\n--- Services ---");
        if (services.isEmpty()) {
            System.out.println("No services found.");
            return;
        }
        for (Service s : services) {
            System.out.println(s);
        }
    }

    private Service findServiceById(int id) {
        for (Service s : services) {
            if (s.getId() == id) return s;
        }
        return null;
    }
// ----- APPOINTMENT METHODS -----

    public void bookAppointment(Scanner scanner) {
        System.out.println("\n--- Book Appointment ---");

        if (customers.isEmpty()) {
            System.out.println("No customers available. Please add a customer first.");
            return;
        }
        if (services.isEmpty()) {
            System.out.println("No services available. Please add a service first.");
            return;
        }

        // Choose customer
        listCustomers();
        System.out.print("Enter customer ID: ");
        int customerId = readInt(scanner);
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        // Choose service
        listServices();
        System.out.print("Enter service ID: ");
        int serviceId = readInt(scanner);
        Service service = findServiceById(serviceId);
        if (service == null) {
            System.out.println("Service not found.");
            return;
        }

        // Enter date
        LocalDate date;
        while (true) {
            System.out.print("Enter date (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateInput, dateFormatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Try again.");
            }
        }

        // Enter time
        LocalTime time;
        while (true) {
            System.out.print("Enter time (HH:mm): ");
            String timeInput = scanner.nextLine().trim();
            try {
                time = LocalTime.parse(timeInput, timeFormatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Try again.");
            }
        }

        LocalDateTime dateTime = LocalDateTime.of(date, time);

        // Simple clash check (same customer + same date & time)
        for (Appointment a : appointments) {
            if (a.getCustomer().getId() == customer.getId() &&
                    a.getDateTime().equals(dateTime)) {
                System.out.println("This customer already has an appointment at that time.");
                return;
            }
        }

        Appointment appt = new Appointment(nextAppointmentId++, customer, service, dateTime);
        appointments.add(appt);
        System.out.println("Appointment booked: " + appt);
    }

    public void viewAllAppointments() {
        System.out.println("\n--- All Appointments ---");
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        List<Appointment> sorted = new ArrayList<>(appointments);
        Collections.sort(sorted, Comparator.comparing(Appointment::getDateTime));

        for (Appointment a : sorted) {
            System.out.println(a);
        }
    }
public void searchAppointmentsByDate(Scanner scanner) {
        System.out.println("\n--- Search Appointments by Date ---");
        LocalDate date;
        while (true) {
            System.out.print("Enter date (yyyy-MM-dd): ");
            String input = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(input, dateFormatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Try again.");
            }
        }

        boolean found = false;
        for (Appointment a : appointments) {
            if (a.getDateTime().toLocalDate().equals(date)) {
                if (!found) {
                    System.out.println("Appointments on " + date + ":");
                }
                System.out.println(a);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found on this date.");
        }
    }

    public void searchAppointmentsByCustomer(Scanner scanner) {
        System.out.println("\n--- Search Appointments by Customer ---");
        System.out.print("Enter customer name (full or part): ");
        String query = scanner.nextLine().trim().toLowerCase();

        boolean found = false;
        for (Appointment a : appointments) {
            if (a.getCustomer().getName().toLowerCase().contains(query)) {
                if (!found) {
                    System.out.println("Appointments for \"" + query + "\":");
                }
                System.out.println(a);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found for this customer.");
        }
    }
    
 // Helper to safely read an int from Scanner
    private int readInt(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
} 