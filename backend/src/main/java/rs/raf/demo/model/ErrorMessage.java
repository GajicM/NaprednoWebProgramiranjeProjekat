package rs.raf.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "error_messages")
public class ErrorMessage {
    @Id @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private long vacuumId;

    @Column(nullable = false)
    private String operation;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private long userId;

}
