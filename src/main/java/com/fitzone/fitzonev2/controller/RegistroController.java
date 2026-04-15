package com.fitzone.fitzonev2.controller;

import com.fitzone.fitzonev2.model.Usuario;
import com.fitzone.fitzonev2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // Clase para almacenar tokens temporalmente
    public static class TokenStorage {
        public static Map<String, String> tokens = new HashMap<>();
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    // Procesar el formulario de registro
    

@PostMapping("/registro")
@ResponseBody
public Map<String, Object> registrarUsuario(@RequestBody Map<String, Object> formData) {
    Map<String, Object> response = new HashMap<>();

    try {
        String correo = (String) formData.get("correo");

        Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreo(correo);
        if (usuarioExistente.isPresent()) {
            response.put("success", false);
            response.put("message", "No se pueden crear dos cuentas con el mismo correo: " + correo);
            return response;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre((String) formData.get("nombre"));
        usuario.setApellido((String) formData.get("apellido"));

        try {
            usuario.setEdad(Integer.parseInt(formData.get("edad").toString()));
            if (formData.get("peso") != null) {
                usuario.setPeso(Double.parseDouble(formData.get("peso").toString()));
            }
            if (formData.get("altura") != null) {
                usuario.setAltura(Integer.parseInt(formData.get("altura").toString()));
            }
        } catch (NumberFormatException e) {
            response.put("success", false);
            response.put("message", "Error en el formato de los datos numéricos");
            return response;
        }

        usuario.setTelefono((String) formData.get("telefono"));
        usuario.setCorreo(correo);

        // Encriptar la contraseña antes de guardarla
        String contrasenaPlano = (String) formData.get("contrasena");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String contrasenaEncriptada = encoder.encode(contrasenaPlano);
        usuario.setContrasena(contrasenaEncriptada);

        usuario.setDireccion((String) formData.get("direccion"));
        usuario.setNivelActividad((String) formData.get("actividad"));
        usuario.setCondicionesMedicas((String) formData.get("condiciones_medicas"));
        usuario.setComoEnteraste((String) formData.get("como_enteraste"));
        usuario.setAceptaPromos(formData.get("acepta_promos") != null && (boolean) formData.get("acepta_promos"));

        String token = UUID.randomUUID().toString();
        usuario.setVerificado(false);
        usuarioRepository.save(usuario);
        TokenStorage.tokens.put(usuario.getCorreo(), token);

        // Crear enlace y enviar correo
        enviarCorreoVerificacion(usuario, token);

        response.put("success", true);
        response.put("message", "Registro exitoso. Revisa tu correo para verificar tu cuenta.");
        response.put("nombre", usuario.getNombre());
        response.put("correo", usuario.getCorreo());

    } catch (Exception e) {
        response.put("success", false);
        response.put("message", "Error en el registro: " + e.getMessage());
    }

    return response;
}

    // Verificación de correo
    @GetMapping("/verificar")
    public String verificarCorreo(@RequestParam("correo") String correo,
                                  @RequestParam("token") String token,
                                  Model model) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String tokenGuardado = TokenStorage.tokens.get(correo);

            if (tokenGuardado != null && tokenGuardado.equals(token)) {
                usuario.setVerificado(true);
                usuarioRepository.save(usuario);
                TokenStorage.tokens.remove(correo);
                model.addAttribute("mensaje", "Cuenta verificada con éxito. Ya puedes iniciar sesión.");
                return "login";
            } else {
                model.addAttribute("mensaje", "Token inválido o expirado.");
                return "login";
            }
        }

        model.addAttribute("mensaje", "Usuario no encontrado.");
        return "login";
    }

    // Reenviar correo de verificación
    @PostMapping("/reenviar-verificacion")
    @ResponseBody
    public Map<String, Object> reenviarCorreoVerificacion(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();

        String correo = requestData.get("correo");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (usuario.isVerificado()) {
                response.put("success", false);
                response.put("message", "La cuenta ya está verificada. Puedes iniciar sesión.");
                return response;
            }

            // Generar un nuevo token y reenviar
            String nuevoToken = UUID.randomUUID().toString();
            TokenStorage.tokens.put(correo, nuevoToken);
            enviarCorreoVerificacion(usuario, nuevoToken);

            response.put("success", true);
            response.put("message", "Correo de verificación reenviado correctamente.");
        } else {
            response.put("success", false);
            response.put("message", "No se encontró una cuenta registrada con ese correo.");
        }

        return response;
    }

    // Método común para enviar correos de verificación
    private void enviarCorreoVerificacion(Usuario usuario, String token) {
        String urlVerificacion = "http://localhost:8080/verificar?correo=" + usuario.getCorreo() + "&token=" + token;

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(fromEmail);
        mensaje.setTo(usuario.getCorreo());
        mensaje.setSubject("Verificación de cuenta Fitzone 💪");

        mensaje.setText(
            "🏋️‍♂️ ¡Hola " + usuario.getNombre() + "!\n\n" +
            "¡Bienvenido a Fitzone! 💪 Estamos emocionados de tenerte con nosotros en esta aventura fitness. 🔥\n\n" +
            "Antes de comenzar a entrenar juntos, necesitamos que verifiques tu cuenta. Solo haz clic en el siguiente enlace:\n\n" +
            urlVerificacion + "\n\n" +
            "✅ Una vez verificado, podrás acceder a tu perfil, planes de entrenamiento personalizados y mucho más.\n\n" +
            "Gracias por ser parte de la familia Fitzone 🧡\n\n" +
            "Con energía,\n" +
            "El equipo de Fitzone 🏆"
        );

        mailSender.send(mensaje);
    }
}
