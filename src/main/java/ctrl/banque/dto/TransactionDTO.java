package ctrl.banque.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDTO {
    private String type;
    private Double montant;
    private LocalDateTime date;
}
