package ctrl.banque.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ctrl.banque.dto.ClientViewDTO;
import ctrl.banque.entity.Client;
import ctrl.banque.entity.Client.StatutClient;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	//Auth / Validation
    Optional<Client> findByEmail(String email);
    Boolean existsByEmail(String email);
     @Query("""
    	    select new ctrl.banque.dto.ClientViewDTO( c.id, c.nom, c.prenom, c.email, size(c.comptes), c.statut)
    	    from Client c where (:kw = '' or
				    	         lower(c.nom) like lower(concat('%', :kw, '%')) or
				    	         lower(c.prenom) like lower(concat('%', :kw, '%')) or
				    	         lower(c.email) like lower(concat('%', :kw, '%')))
    		"""
    )
    Page<ClientViewDTO> searchAll(@Param("kw") String keyword, Pageable pageable);

    //Dérniers 3 clients
    List<Client> findTop3ByOrderByIdDesc();

    //Clients créés après une date
    List<Client> findByDateCreationAfter(LocalDateTime date);

    //Filtre par statut
    long countByStatut(Client.StatutClient statut);
    Page<Client> findByStatut(Client.StatutClient statut, Pageable pageable);
    
    Page<Client> findByStatutAndNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
            Client.StatutClient statut,
            String nom,
            String prenom,
            Pageable pageable
    );
    //Recherche 3adi
    Page<Client> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
            String nom,
            String prenom,
            Pageable pageable
    );

    //Recherche + statut + pagination
    @Query("""
	        SELECT c FROM Client c
	        WHERE c.statut = :statut
	        AND (
	            LOWER(c.nom) LIKE LOWER(CONCAT('%', :keyword, '%'))
	            OR LOWER(c.prenom) LIKE LOWER(CONCAT('%', :keyword, '%'))
	        )
	    """)
    Page<Client> searchByStatutAndKeyword(
            @Param("statut") StatutClient statut,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
