package org.haldokan.edge.interviewquest.amazon;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * My rough-edged solution to an Amazon interview questions - unless there was followup questions, this is a simple class design question
 *
 * The Question: 2.5-STAR
 *
 * Design an email client where you can only send emails to people who are present in your contact list.
 * Build for only the following functionality:
 * 1) Send/Receive Emails
 * 2) Add/Remove Contacts
 * 3) See Inbox
 * 4) See Sent Mail
 */
public class DesignEmailApp {
    Set<Contact> contacts;
    // want a stack (LIFO) semantics (show last emails first) - adding to the head of a linked list is better
    // than doing the same using array-based list (ArrayList) since the latter entails shifting all elements to the right
    List<Email> inbox = new LinkedList<>();
    List<Email> sentEmail = new LinkedList<>();

    void send(Email email) {
        if (contacts.contains(email.contact)) {
            sentEmail.add(0, email);
            System.out.printf("Send email to: %s%n", email.contact.email);
        }
    }

    void receive(Email email) {
        inbox.add(0, email);
        System.out.printf("Receive email from: %s%n", email.contact.email);
    }

    void inbox() {
        inbox.forEach(email -> System.out.printf("%s", email));
    }

    void sentEmail() {
        sentEmail.forEach(email -> System.out.printf("%s", email));
    }

    boolean addContact(Contact contact) {
        return contacts.add(contact);
    }

    boolean removeContact(Contact contact) {
        return contacts.remove(contact);
    }

    static class Contact {
        // equality and hash are done on 'email' value alone
        String email;
        String name;

        public Contact(String email, String name) {
            this.email = email;
            this.name = name;
        }
    }

    static class Email {
        String id;
        Contact contact;
        String text;
        LocalDateTime dateTime;
    }
}
