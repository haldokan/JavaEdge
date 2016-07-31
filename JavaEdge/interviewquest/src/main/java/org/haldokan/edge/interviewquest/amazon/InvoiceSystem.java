package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * My solution to an Amazon interview question - complete with scheduled jobs to mark overdue invoices and charge them
 * based on days overdue
 * <p>
 * The Question: 4_STAR
 * <p>
 * You as a developer are tasked to create an application that builds invoices that are send out to the company’s
 * clients. Invoices are sent to the client’s address and the client should pay the invoice amount by a given date
 * otherwise fees could incur. The client should be able to see detailed information about the items they are being
 * billed for, like item cost, tax, quantity, etc. The client can be billed for products and/or services. Services are
 * not taxable and product tax varies by client zip code.
 * <p>
 * Using the objected oriented language of your choice; design an object model for this application.
 * Created by haytham.aldokanji on 7/30/16.
 */
public class InvoiceSystem {
    private final static IdGenerator idGenerator = new IdGenerator(); // should be passed to the class instead
    private final TaxCalculator taxCalculator;
    private final Map<String, Invoice> invoiceLedger = new HashMap<>();
    private final SetMultimap<LocalDate, Invoice> pendingInvoices = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    private final SetMultimap<LocalDate, Invoice> overdueInvoices = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    public InvoiceSystem(TaxCalculator taxCalculator) {
        this.taxCalculator = taxCalculator;
    }

    public void createInvoice(Client client, LocalDate dueDate, List<Product> products, List<Service> services) {
        Invoice invoice = Invoice.create(client, dueDate, taxCalculator);
        invoice.addProducts(products.toArray(new Product[products.size()]));
        invoice.addServices(services.toArray(new Service[services.size()]));
        invoice.calculate();

        invoiceLedger.put(invoice.getId(), invoice);
        pendingInvoices.put(invoice.getDueDate(), invoice);
    }

    public void invoicePaid(String invoiceId) {
        Invoice invoice = invoiceLedger.get(invoiceId);
        invoice.paid();

        LocalDate dueDate = invoice.getDueDate();
        pendingInvoices.remove(dueDate, invoice);
        overdueInvoices.remove(dueDate, invoice);
    }

    // this would be called from a scheduler every day -
    // we pass the date in case the job fails and it has to be run for a previous day
    public void markOverdueInvoices(LocalDate date) {
        Set<Invoice> overdueInvoicesForDate = pendingInvoices.removeAll(date);

        if (!overdueInvoicesForDate.isEmpty()) {
            overdueInvoices.putAll(date, overdueInvoicesForDate);
        }
    }

    // this would be called from a scheduler
    public void addChargesToOverdueInvoices(double chargesRate) {
        LocalDate[] overDueDates = overdueInvoices.keySet().stream().toArray(LocalDate[]::new);
        for (LocalDate date : overDueDates) {
            int daysOverdue = (LocalDate.now().getDayOfYear() - date.getDayOfYear());
            List<Invoice> overdueInvoicesForDate = Lists.newArrayList(overdueInvoices.get(date));
            overdueInvoicesForDate.forEach(invoice -> invoice.addCharges(invoice.getAmount() * chargesRate * daysOverdue));
        }
    }

    private static class Invoice {
        private final String id;
        private final Client client;
        private final LocalDate dueDate;
        private final TaxCalculator taxCalculator;
        private final List<Product> products = new ArrayList<>();
        private final List<Service> services = new ArrayList<>();
        private double amount;
        private double charges;
        private boolean paid;
        private boolean finalized;

        private Invoice(Client client, LocalDate dueDate, TaxCalculator taxCalculator) {
            this.id = idGenerator.getId();
            this.client = client;
            this.dueDate = dueDate;
            this.taxCalculator = taxCalculator;
        }

        public static Invoice create(Client client, LocalDate dueDate, TaxCalculator taxCalculator) {
            return new Invoice(client, dueDate, taxCalculator);
        }

        public void addProducts(Product... products) {
            if (products != null && !finalized) {
                this.products.addAll(Arrays.stream(products).collect(Collectors.toList()));
            }
        }

        public void addServices(Service... services) {
            if (services != null && !finalized) {
                this.services.addAll(Arrays.stream(services).collect(Collectors.toList()));
            }
        }

        public void calculate() {
            if (!finalized) {
                finalized = true;
                products.stream().forEach(product ->
                        product.setTax(taxCalculator.getTax(product.getCost(), client.getAddress().getZip())));
                amount = products.stream()
                        .collect(Collectors.summingDouble(product ->
                                (product.getCost() + product.getTax()) * product.getQuantity()));
                amount += services.stream()
                        .collect(Collectors.summingDouble(service ->
                                service.getCost() * service.getQuantity()));
            }
        }

        public void addCharges(double charges) {
            this.charges += charges;
            this.amount += charges;
        }

        public void paid() {
            this.paid = true;
        }

        public String getId() {
            return id;
        }

        public double getAmount() {
            return amount;
        }

        public Client getClient() {
            return client;
        }

        public LocalDate getDueDate() {
            return LocalDate.from(dueDate);
        }

        public List<Product> getProducts() {
            return Collections.unmodifiableList(products);
        }

        public List<Service> getServices() {
            return Collections.unmodifiableList(services);
        }

        public double getCharges() {
            return charges;
        }

        public boolean isPaid() {
            return paid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Invoice invoice = (Invoice) o;

            return id.equals(invoice.id);

        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    private static abstract class BillingItem {
        protected final String name;
        protected final double cost;
        protected final LocalDate date;
        protected final int quantity;

        private BillingItem(String name, double cost, LocalDate date, int quantity) {
            this.name = name;
            this.cost = cost;
            this.date = LocalDate.from(date); // defensive
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public double getCost() {
            return cost;
        }

        public LocalDate getDate() {
            return LocalDate.from(date); // defensive
        }

        public int getQuantity() {
            return quantity;
        }
    }

    private static final class Product extends BillingItem {
        private double tax;
        private boolean finalized;

        private Product(String name, double cost, LocalDate date, int quantity) {
            super(name, cost, date, quantity);
        }

        public static Product create(String name, double cost, LocalDate date, int quantity) {
            return new Product(name, cost, date, quantity);
        }

        public double getTax() {
            return tax;
        }

        public void setTax(double tax) {
            if (!finalized) {
                finalized = true;
                this.tax = tax;
            }
        }
    }

    private static class Service extends BillingItem {
        private final String[] details;

        private Service(String name, double cost, LocalDate date, int quantity, String[] details) {
            super(name, cost, date, quantity);
            this.details = Arrays.copyOf(details, details.length); // defensive
        }

        public static Service create(String name, double cost, LocalDate date, int quantity, String[] details) {
            return new Service(name, cost, date, quantity, details);
        }

        public String[] getDetails() {
            return Arrays.copyOf(details, details.length); // defensive
        }
    }

    private static class Client {
        private final String id, name;
        private Address address;

        public Client(String id, String name, Address address) {
            this.id = id;
            this.name = name;
            this.address = address;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Address getAddress() {
            return address;
        }
    }

    private static class Address {
        private final String apartment, street, zip, state, country;

        private Address(String apartment, String street, String zip, String state, String country) {
            this.apartment = apartment;
            this.street = street;
            this.zip = zip;
            this.state = state;
            this.country = country;
        }

        public String getApartment() {
            return apartment;
        }

        public String getStreet() {
            return street;
        }

        public String getZip() {
            return zip;
        }

        public String getState() {
            return state;
        }

        public String getCountry() {
            return country;
        }
    }

    private static class TaxCalculator {

        public double getTax(double amount, String zip) {
            return ((double) (new Random().nextInt(5) + 5)) * amount / 100;
        }
    }

    public static class IdGenerator {
        private SecureRandom random = new SecureRandom();

        public String getId() {
            return new BigInteger(128, random).toString(32);
        }
    }
}
