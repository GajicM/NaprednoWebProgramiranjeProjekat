package rs.raf.demo.model;

import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Table(name = "vacuums")
@Data
public class Vacuum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VacuumStatus status;

    @CreatedBy
    @ManyToOne
    private User addedBy;

    @Column(nullable = false)
    private boolean active;

    @Column(updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean locked;

    @Column(nullable = false)
    private int cycles;

    @Version
    private Integer version = 0;

}
