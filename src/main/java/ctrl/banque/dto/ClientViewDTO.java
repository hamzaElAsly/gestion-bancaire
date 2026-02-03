package ctrl.banque.dto;

import ctrl.banque.entity.Client.StatutClient;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientViewDTO {
	private Long id;
    private String nomComplet;
    private String email;
    private int nbComptes;
    private String statut;

	public ClientViewDTO(Long id, String nom, String prenom, String email, Integer nbComptes, StatutClient statut){
		this.id = id;
        this.nomComplet = nom + " " + prenom;
        this.email = email;
        this.nbComptes = (nbComptes == null) ? 0 : nbComptes;
        this.statut = statut.name();
	}
}
