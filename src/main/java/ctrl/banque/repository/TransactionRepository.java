package ctrl.banque.repository;

import ctrl.banque.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	@Query("SELECT SUM(t.montant) FROM Transaction t")
    Double sumTotalMontant();
    List<Transaction> findByCompteIdOrderByDateDesc(Long compteId);
    List<Transaction> findByCompteId(Long compteId);
    List<Transaction> findTop3ByOrderByDateDesc();
}