package com.fitzone.fitzonev2.controller;

import com.fitzone.fitzonev2.model.Encuesta;
import com.fitzone.fitzonev2.repository.EncuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import weka.classifiers.Classifier;
import weka.core.*;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.*;

@RestController
@RequestMapping("/api/predict")
@CrossOrigin(origins = "*")
public class PredictController {

    private Classifier modeloWeka;

    @Autowired
    private EncuestaRepository encuestaRepository;

    // Carga el modelo una sola vez al levantar el backend
    public PredictController() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("models/modelo_cancelacion.model");
        if (inputStream == null) throw new RuntimeException("No se encontró el modelo en resources/models/");
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        modeloWeka = (Classifier) ois.readObject();
        ois.close();
    }

    @GetMapping("/cancelaciones")
    public List<Map<String, Object>> predecirCancelaciones() throws Exception {
        List<Encuesta> encuestas = encuestaRepository.findAll();
        List<Map<String, Object>> resultados = new ArrayList<>();

        // Define los atributos igual como en el modelo .arff
        ArrayList<Attribute> attributes = new ArrayList<>(Arrays.asList(
                new Attribute("edad"),
                new Attribute("genero", Arrays.asList("hombre", "mujer")),
                new Attribute("frecuencia_semanal"),
                new Attribute("tiempo_membresia_meses"),
                new Attribute("objetivo", Arrays.asList("bajar_peso", "ganar_musculo", "mantener")),
                new Attribute("plan_membresia", Arrays.asList("mensual", "trimestral", "anual")),
                new Attribute("ha_asistido_ultima_semana", Arrays.asList("si", "no")),
                new Attribute("usa_entrenador_personal", Arrays.asList("si", "no")),
                new Attribute("asiste_clases_grupales", Arrays.asList("si", "no")),
                new Attribute("pagos_atrasados"),
                new Attribute("satisfaccion_servicio"),
                new Attribute("lesiones_recientes", Arrays.asList("si", "no")),
                new Attribute("peso_actual"),
                new Attribute("cambio_peso_mensual"),
                new Attribute("tiene_descuento", Arrays.asList("si", "no")),
                new Attribute("ingresos_mensuales"),
                new Attribute("cancelara", Arrays.asList("si", "no")) // Class Attribute
        ));

        Instances dataRaw = new Instances("PrediccionCancelacion", attributes, 0);
        dataRaw.setClassIndex(attributes.size() - 1);

        for (Encuesta enc : encuestas) {
            double[] vals = new double[attributes.size()];

            vals[0] = enc.getEdad();
            vals[1] = attributes.get(1).indexOfValue(enc.getGenero());
            vals[2] = enc.getFrecuencia_semanal();
            vals[3] = enc.getTiempo_membresia_meses();
            vals[4] = attributes.get(4).indexOfValue(enc.getObjetivo());
            vals[5] = attributes.get(5).indexOfValue(enc.getPlan_membresia());
            vals[6] = attributes.get(6).indexOfValue(enc.getHa_asistido_ultima_semana());
            vals[7] = attributes.get(7).indexOfValue(enc.getUsa_entrenador_personal());
            vals[8] = attributes.get(8).indexOfValue(enc.getAsiste_clases_grupales());
            vals[9] = enc.getPagos_atrasados();
            vals[10] = enc.getSatisfaccion_servicio();
            vals[11] = attributes.get(11).indexOfValue(enc.getLesiones_recientes());
            vals[12] = enc.getPeso_actual();
            vals[13] = enc.getCambio_peso_mensual();
            vals[14] = attributes.get(14).indexOfValue(enc.getTiene_descuento());
            vals[15] = enc.getIngresos_mensuales();
            vals[16] = Utils.missingValue(); // class attribute is unknown

            // Debug: Mostrar si algún valor es -1 (problema en categóricos)
            boolean categoriasOK = true;
            int[] categIndexes = {1,4,5,6,7,8,11,14};
            for (int idx : categIndexes) {
                if (vals[idx] == -1) {
                    categoriasOK = false;
                    System.out.println("Valor categórico inválido en índice " + idx + ": " + attributes.get(idx).name() + " valor: " + getCategoria(enc, idx));
                }
            }
            if (!categoriasOK) continue; // Salta este registro si hay error

            Instance instance = new DenseInstance(1.0, vals);
            dataRaw.add(instance);
        }

        for (int i = 0; i < dataRaw.size(); i++) {
            Instance instance = dataRaw.get(i);
            double[] dist = modeloWeka.distributionForInstance(instance);
            String prediccion = dist[1] > dist[0] ? "SI" : "NO";
            double probabilidad = dist[1];

            Map<String, Object> res = new HashMap<>();
            res.put("nombre", encuestas.get(i).getNombre());
            res.put("correo", encuestas.get(i).getCorreo());
            res.put("probabilidad_cancelar", probabilidad);
            res.put("prediccion", prediccion);
            resultados.add(res);
        }
        return resultados;
    }

    // Método auxiliar para mostrar el valor categórico correspondiente según el índice
    private String getCategoria(Encuesta enc, int idx) {
        switch (idx) {
            case 1: return enc.getGenero();
            case 4: return enc.getObjetivo();
            case 5: return enc.getPlan_membresia();
            case 6: return enc.getHa_asistido_ultima_semana();
            case 7: return enc.getUsa_entrenador_personal();
            case 8: return enc.getAsiste_clases_grupales();
            case 11: return enc.getLesiones_recientes();
            case 14: return enc.getTiene_descuento();
            default: return "";
        }
    }
}