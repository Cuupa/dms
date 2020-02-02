package com.cuupa.dms.service.extern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExternSemanticService {

    private final RestTemplate template = new RestTemplate();

    private final Gson gson = new Gson();

    @Value("${services.remote.semantic.url}")
    private String semanticUrl;

    public List<SemanticResult> analize(final byte[] text) {
        ResponseEntity<String> response = template.postForEntity(semanticUrl, text, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Type listType = new TypeToken<ArrayList<SemanticResult>>() {

            }.getType();
            return gson.fromJson(response.getBody(), listType);
        } else {
            throw new RuntimeException();
        }
    }
}
