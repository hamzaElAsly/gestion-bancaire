package ctrl.banque.repository;

import ctrl.banque.entity.Compte;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
	@Query("SELECT SUM(c.solde) FROM Compte c")
    Double sumTotalSolde();
    List<Compte> findByClientId(Long clientId);
}