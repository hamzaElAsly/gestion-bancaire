package ctrl.banque.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDetailsDTO {

    private String nom;
    private String prenom;
    private String email;
    private String adresse;
    private String ville;
    private String statut;

    private List<CompteDetailsDTO> comptes;
}
