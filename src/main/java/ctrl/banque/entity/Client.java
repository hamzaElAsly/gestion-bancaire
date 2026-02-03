package ctrl.banque.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    public enum StatutClient { ACTIF, INACTIF }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String ville;
    private String adresse;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private StatutClient statut = StatutClient.INACTIF;;
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Compte> comptes;
}