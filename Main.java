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
