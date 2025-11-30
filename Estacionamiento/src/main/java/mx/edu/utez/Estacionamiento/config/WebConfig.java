package mx.edu.utez.Estacionamiento.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración web para la aplicación ParkSmart
 * Maneja CORS, recursos estáticos y otras configuraciones web
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configuración de CORS (Cross-Origin Resource Sharing)
     * Permite que el frontend se comunique con el backend desde diferentes orígenes
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                // En desarrollo: permitir todos los orígenes
                // En producción: cambiar a dominios específicos
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    /**
     * Configuración de recursos estáticos
     * Asegura que los archivos HTML, CSS, JS e imágenes se sirvan correctamente
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Spring Boot ya maneja automáticamente /static, pero podemos agregar configuraciones adicionales
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }
}

