package rs.raf.demo.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data @Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @Transient
    private String token;

    @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "permissions")
    private Set<Permission> permissions;
 //   public Set<Permission> permission;

    public User() {
        permissions=new HashSet<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

}
