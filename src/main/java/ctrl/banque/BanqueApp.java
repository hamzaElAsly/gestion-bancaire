package ctrl.banque;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ctrl.banque.entity.Client;
import ctrl.banque.entity.Compte;
import ctrl.banque.repository.ClientRepository;
import ctrl.banque.repository.CompteRepository;

@SpringBootApplication
public class BanqueApp  implements CommandLineRunner{
	public static void main(String[] args) {
		SpringApplication.run(BanqueApp.class, args);
		System.out.println("Bonjour MAIN");
	}
	
	private final ClientRepository clientRepository;
    private final CompteRepository compteRepository;
    public BanqueApp(ClientRepository clientRepository,
                     CompteRepository compteRepository) {
        this.clientRepository = clientRepository;
        this.compteRepository = compteRepository;
    }
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Bonjouuuuuur Run");		
		System.out.println("Initialisation des données...");

        // ====== CLIENTS ======
        //Client cl1 = new Client(null, "EL AMRANI", "Hamza","Fès","oued fès", "hamza@gmail.com",new Date(2000,12, 20) , null);
        Client cl2 = new Client(null, "BENALI", "Sara","Rabat","Centre ville",  "sara@gmail.com",Client.StatutClient.ACTIF,LocalDateTime.now().minusMonths(3), null);
        //Client cl3 = new Client(null, "AIT SAID", "Youssef","Tanger","ain dalia",  "youssef@gmail.com", null);

        // ====== COMPTES ======
        Compte c1 = new Compte(null, 12000.0, "COURANT", cl2, null);
        Compte c2 = new Compte(null, 8000.0, "EPARGNE", cl2, null);
        
        if (clientRepository.count() == 0) {
            clientRepository.saveAll(List.of(cl2));
            compteRepository.saveAll(List.of(c1, c2));
        }
        System.out.println("Clients et comptes insérés avec succès !!!!!");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String adminPwd = encoder.encode("admin123");
        String userPwd  = encoder.encode("user123");

        System.out.println("=================================");
        System.out.println("PASSWORDS GÉNÉRÉS :");
        System.out.println("admin123 = " + adminPwd);
        System.out.println("user123  = " + userPwd);
        System.out.println("=================================");
	}
}