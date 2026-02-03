package ctrl.banque.dto;

import lombok.Data;

@Data
public class ClientEditDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String ville;
    private String adresse;
}
