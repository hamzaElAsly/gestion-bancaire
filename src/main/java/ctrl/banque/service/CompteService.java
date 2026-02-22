package ctrl.banque.service;

import ctrl.banque.entity.*;
import ctrl.banque.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CompteService {
    @Autowired
    private CompteRepository compteRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    
    public List<Compte> getAllComptes() { return compteRepository.findAll(); }
    
    public void depose(Long compteId, Double montant) {
        Compte cp = compteRepository.findById(compteId).orElseThrow(() -> new RuntimeException("Compte introuvable"));
        cp.setSolde(cp.getSolde() + montant);
        Transaction t = new Transaction( null, montant, "DEPOT", LocalDateTime.now(), cp);
        transactionRepository.save(t);
    }

    public void retire(Long compteId, Double montant) {
        Compte cp = compteRepository.findById(compteId).orElseThrow(() -> new RuntimeException("Compte introuvable"));

        if (cp.getSolde() < montant) throw new RuntimeException("Solde insuffisant");
        cp.setSolde(cp.getSolde() - montant);
        Transaction t = new Transaction( null, montant, "RETRAIT", LocalDateTime.now(), cp );
        transactionRepository.save(t);
    }

    public void virement(Long from, Long to, Double montant) {
        retire(from, montant);
        depose(to, montant);
    }
}