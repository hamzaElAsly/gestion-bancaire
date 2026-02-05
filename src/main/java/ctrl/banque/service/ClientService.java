package ctrl.banque.service;

import ctrl.banque.dto.*;
import ctrl.banque.entity.Client;
import ctrl.banque.repository.ClientRepository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    
	private final ClientRepository clientRepository;
    
    public ClientService(ClientRepository clientRepository) { this.clientRepository = clientRepository; }
    public Page<ClientViewDTO> getClients(String keyword, Pageable pageable) {
        return clientRepository.searchAll(keyword, pageable);
    }
    
    // Clients Nouveaux (dernier mois)
    public List<Client> getNouveauxClients() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return clientRepository.findByDateCreationAfter(oneMonthAgo);
    }
    
    public Client save(Client client) { return clientRepository.save(client); }
    public Client getById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client introuvable"));
    }
    
    public long getClientsActifs() {
        return clientRepository.countByStatut(Client.StatutClient.ACTIF);
    }
    
    public Page<ClientViewDTO> getClientsWithStatus(String keyword, Client.StatutClient statut, Pageable pageable) 
    {
    	String kw = (keyword == null) ? "" : keyword.trim();
        Page<Client> pageClients;

        if (kw.isBlank()) {
        	pageClients = clientRepository.searchByStatutAndKeyword(statut, "", pageable);
        } 
        else {
        	pageClients = clientRepository.searchByStatutAndKeyword(statut, keyword, pageable);
        }

        return pageClients.map(client -> {
            int nbComptes = (client.getComptes() == null) ? 0 : client.getComptes().size();
            //String statutClient = nbComptes > 0 ? "ACTIF" : "INACTIF";
            String statutClient = (client.getStatut()== null) ? null : client.getStatut().name();

            return new ClientViewDTO(
                client.getId(),               
                client.getPrenom() + " " + client.getNom(),
                client.getEmail(),
                nbComptes,
                statutClient
            );
        });
    }
    
    public ClientDetailsDTO getClientDetails(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client introuvable"));
        String statut = (client.getComptes() != null && !client.getComptes().isEmpty()) ? "ACTIF" : "INACTIF";
        List<CompteDetailsDTO> comptesDTO = client.getComptes().stream().map(compte -> {
            List<TransactionDTO> transactionsDTO =
                compte.getTransactions().stream()
                    .map(t -> new TransactionDTO(
                            t.getType(),
                            t.getMontant(),
                            t.getDate()
                    ))
                    .toList();
            return new CompteDetailsDTO(
                    compte.getId(),
                    compte.getType(),
                    compte.getSolde(),
                    transactionsDTO
            );}).toList();
        return new ClientDetailsDTO(
                client.getNom(),
                client.getPrenom(),
                client.getEmail(),
                client.getAdresse(),
                client.getVille(),
                statut,
                comptesDTO
        );
    }
}

