package ctrl.banque.controller;

import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ctrl.banque.dto.ClientDetailsDTO;
import ctrl.banque.dto.ClientEditDTO;
import ctrl.banque.dto.ClientViewDTO;
import ctrl.banque.entity.Client;
import ctrl.banque.repository.*;
import ctrl.banque.service.ClientService;

@Controller
public class CompteController {
    // PAGE D'ACCUEIL
	@Autowired
    private CompteRepository compteRepository;
    private TransactionRepository transactionRepository1;
	private ClientRepository clientRepository;
	private ClientService clientService;
	
	public CompteController(ClientService clientService, TransactionRepository transactionRepository, CompteRepository compteRepository, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.compteRepository = compteRepository;
        this.transactionRepository1 = transactionRepository;
    }
	
    @GetMapping("/")
    public String accueil(Model model) {
    	long totalClients = clientRepository.count();
        long totalComptes = compteRepository.count();
        long totalTransactions = transactionRepository1.count();
        Double totalSolde = compteRepository.sumTotalSolde();
        DecimalFormat df = new DecimalFormat("0");
        String totalSoldeFormat = df.format(totalSolde);

        model.addAttribute("lastClients", clientRepository.findTop3ByOrderByIdDesc());
        model.addAttribute("lastTransactions", transactionRepository1.findTop3ByOrderByDateDesc());
        model.addAttribute("totalClients", totalClients);
        model.addAttribute("totalComptes", totalComptes);
        model.addAttribute("totalTransactions", totalTransactions);
        model.addAttribute("totalSolde", totalSoldeFormat);
        
        return "index";
    }
    
    @GetMapping("/clients")
    public String clients(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "") String keyword,
                          @RequestParam(required = false, defaultValue = "") String statut) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("nom").ascending());

        Client.StatutClient statutEnum = null;

        if (statut != null) {
            String v = statut.trim(); 
            if (!v.isEmpty()) {
                try { statutEnum = Client.StatutClient.valueOf(v.toUpperCase());
                } catch (IllegalArgumentException ex) { statutEnum = null;
                }
            }
        }

        Page<ClientViewDTO> clientsPage =
                (statutEnum == null)
                        ? clientService.getClients(keyword, pageable)
                        : clientService.getClientsWithStatus(keyword, statutEnum, pageable);

        model.addAttribute("clients", clientsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", clientsPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("statut", statut); // باش تبقى القيمة فـ UI

        long totalClients = clientRepository.count();
        Double totalSolde = compteRepository.sumTotalSolde();
        DecimalFormat df = new DecimalFormat("0");
        String totalSoldeFormat = df.format(totalSolde == null ? 0 : totalSolde);

        model.addAttribute("clientsActifs", clientService.getClientsActifs());
        model.addAttribute("nouveauxClients", clientService.getNouveauxClients());
        model.addAttribute("totalClients", totalClients);
        model.addAttribute("totalSolde", totalSoldeFormat);

        return "clients/clients";
    }

    
    // Ajouter client
    @PostMapping
    public Client addClient(@RequestBody Client client) {
        client.setStatut(Client.StatutClient.INACTIF);
        return clientService.save(client);
    }

    // Détails client
    @GetMapping("/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientService.getById(id);
    }
    // Modification
    @GetMapping("/clients/{id}") @ResponseBody
    public ClientEditDTO getClientById(@PathVariable Long id) {
        Client c = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client introuvable"));
        ClientEditDTO dto = new ClientEditDTO();
        dto.setId(c.getId());
        dto.setNom(c.getNom());
        dto.setPrenom(c.getPrenom());
        dto.setEmail(c.getEmail());
        dto.setVille(c.getVille());
        dto.setAdresse(c.getAdresse());
        return dto;
    }
    @PostMapping("/clients/update") @ResponseBody
    public String updateClient(@RequestBody ClientEditDTO dto) {
        Client c = clientRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Client introuvable"));
        c.setNom(dto.getNom());
        c.setPrenom(dto.getPrenom());
        c.setEmail(dto.getEmail());
        c.setVille(dto.getVille());
        c.setAdresse(dto.getAdresse());
        clientRepository.save(c);
        return "OK";
    }
    
    @GetMapping("/clients/details/{id}") @ResponseBody
    public ClientDetailsDTO clientDetails(@PathVariable Long id) {
        return clientService.getClientDetails(id);
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
