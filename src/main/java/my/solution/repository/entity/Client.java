package my.solution.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import java.time.OffsetDateTime;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long clientId;

    @Column(nullable = false, unique = true)
    String login;

    @Column(nullable = false)
    String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account", referencedColumnName = "account_id")
    BankAccount account;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column
    String patronymic;

    @Column(unique = true)
    String phoneNumber;

    @Column(unique = true)
    String emailAddress;

    @Column(name = "birth_date")
    OffsetDateTime birthDate;

    @AssertTrue(message = "Either phone or email must be provided.")
    public boolean isPhoneOrEmailProvided() {
        return phoneNumber != null || emailAddress != null;
    }
}
