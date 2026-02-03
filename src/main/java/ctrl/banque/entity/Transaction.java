package ctrl.banque.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double montant;
    private String type; // DEPOT, RETRAIT, VIREMENT
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "compte_id")
    private Compte compte;
}
