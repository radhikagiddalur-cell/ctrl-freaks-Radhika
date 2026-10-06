package com.novabank.transfer.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

/**
 * Imports saved transfer templates ("pay rent", "pay tuition fee", ...) uploaded by customers as YAML.
 */
@Service
public class TemplateService {

    /**
     * @return the names of the templates found in the uploaded YAML document
     */
    public List<String> importTemplates(String yamlDocument) {
        Object parsed = new Yaml().load(yamlDocument);
        if (parsed instanceof Map<?, ?> map && map.get("templates") instanceof List<?> templates) {
            return templates.stream()
                    .filter(Map.class::isInstance)
                    .map(t -> String.valueOf(((Map<?, ?>) t).get("name")))
                    .toList();
        }
        return List.of();
    }
}
