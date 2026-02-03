package ctrl.banque.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompteDetailsDTO {
    private Long id;
    private String type;
    private Double solde;
    private List<TransactionDTO> transactions;
}
