package market.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.DefaultValue;

@Entity
@Table(name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Email
    private String email;

    @Column
    @NotBlank
    private String password;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Role.class)
    @JoinColumn(name = "role", referencedColumnName = "authority", nullable = false)
    private Role role;

    @Column
    @DefaultValue("false")
    private Boolean isBlocked;

    public Account() {
    }

    public Account(String email, String password, Role role, Boolean isBlocked) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.isBlocked = isBlocked;
    }

    public Account(String email, String password, Boolean isBlocked) {
        this.email = email;
        this.password = password;
        this.isBlocked = isBlocked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }
}
