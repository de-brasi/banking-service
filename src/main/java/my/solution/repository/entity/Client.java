package my.solution.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Client implements UserDetails {
    @Id
    @Column(name = "client_id")
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
