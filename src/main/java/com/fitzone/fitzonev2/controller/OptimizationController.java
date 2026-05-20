package com.fitzone.fitzonev2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/optimize")
public class OptimizationController {

    @GetMapping
    public ResponseEntity<String> runOptimization() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "modelo_optimizacion_json.py");
            pb.directory(new File(".")); 
            pb.redirectErrorStream(true);
            Process process = pb.start();

            String output = new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));

            process.waitFor();
            
            // Pulp a veces imprime info inicial. Extraemos solo el JSON:
            int jsonStart = output.indexOf("{");
            if(jsonStart >= 0) {
                return ResponseEntity.ok(output.substring(jsonStart));
            }

            return ResponseEntity.status(500).body("{\"error\": \"Output inválido del modelo.\", \"detalle\": \"" + output + "\"}");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
