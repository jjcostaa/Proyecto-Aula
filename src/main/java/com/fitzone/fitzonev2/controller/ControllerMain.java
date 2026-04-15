package com.fitzone.fitzonev2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerMain {

    @GetMapping("/home")
    public String showHomePage() {
        return "home"; // src/main/resources/templates/home.html
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // src/main/resources/templates/login.html
    }

    @GetMapping("/promos")
    public String showPromosPage() {
        return "promos"; // src/main/resources/templates/promos.html
    }

    @GetMapping("/metodospago")
    public String showContactPage() {
        return "metodospago"; // src/main/resources/templates/contacto.html
    }

    @GetMapping("/informacion")
    public String showString() {
        return "informacion";
    }

    @GetMapping("/recuperar-formulario")
    public String mostrarFormularioRecuperacion() {
        return "recuperar_contrasena"; // src/main/resources/templates/recuperar_contrasena.html
    }

    // Nuevas rutas agregadas:

    @GetMapping("/about")
    public String showAboutPage() {
        return "about"; // src/main/resources/templates/about.html
    }

    @GetMapping("/feature")
    public String showFeaturePage() {
        return "feature"; // src/main/resources/templates/feature.html
    }

    @GetMapping("/class")
    public String showClassPage() {
        return "class"; // src/main/resources/templates/class.html
    }

    @GetMapping("/blog")
    public String showBlogPage() {
        return "blog"; // src/main/resources/templates/blog.html
    }

    @GetMapping("/single")
    public String showSingleBlogPage() {
        return "single"; // src/main/resources/templates/single.html
    }

    @GetMapping("/contact")
    public String showContact() {
        return "contact"; // src/main/resources/templates/contact.html
    }

    @GetMapping("/usuario/userpanel")
public String showUserPanel() {
    return "usuario/userpanel";
}

@GetMapping("/admin/adminpanel")
public String showAdminPanel() {
    return "admin/adminpanel";
}

@GetMapping("/entrenador/panelentrenador")
public String showEntrenadorPanel() {
    return "entrenador/panelentrenador";
}
    @GetMapping("/membresias")
    public String mostrarMembresias() {
        // Devuelve la ruta relativa al archivo dentro de templates (sin extensión .html)
        return "usuario/membresias";
    }

    @GetMapping("/mis_clases")
    public String mostrarPaginaReservarClases() {
        return "usuario/clases"; // sin .html
    }

        @GetMapping("/userpanel")
    public String userPanel(Model model) {
        return "usuario/userpanel";
    }

    
}
