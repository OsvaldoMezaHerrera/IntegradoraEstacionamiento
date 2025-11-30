package mx.edu.utez.Estacionamiento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para servir las páginas HTML estáticas
 * Spring Boot automáticamente sirve archivos desde /static,
 * pero este controlador permite rutas más limpias y redirecciones
 */
@Controller
public class IndexController {

    /**
     * Redirige la ruta raíz a index.html
     * Spring Boot automáticamente servirá index.html desde /static/
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/index.html";
    }
    
    // Nota: Los archivos HTML se sirven automáticamente desde src/main/resources/static/
    // No necesitamos métodos adicionales ya que Spring Boot los encuentra automáticamente
    // cuando se accede a /index.html, /registro.html, etc.
}

