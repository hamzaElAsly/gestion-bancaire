package ctrl.banque.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ctrl.banque.entity.Client;
@Component
public class StatutClientConverter implements Converter<String, Client.StatutClient> {

    @Override
    public Client.StatutClient convert(String source) {
        if (source == null) return null;
        String v = source.trim();
        if (v.isEmpty() || v.equalsIgnoreCase("ALL")) { return null; }
        return Client.StatutClient.valueOf(v.toUpperCase());
    }
}
