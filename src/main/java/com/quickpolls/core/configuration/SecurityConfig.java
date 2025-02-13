package com.quickpolls.core.configuration;

import com.quickpolls.core.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {
                cors.configurationSource(corsConfigurationSource());
            })
            .csrf(csrf -> {
                csrf.disable();
            })
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/auth/**").permitAll();
                auth.requestMatchers("/admin/**").hasRole("ADMIN");
                auth.anyRequest().authenticated();
            })
            .httpBasic(httpBasic -> {})
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Filtro JWT

        return http.build();
    }

    // METODO BEAN QUE DEVUELVE UNA CONFIGURACION CORS
    // ESTA CONFIGURACION ES USADA POR EL OBJETO DE CONFIG HTTP AL HABILITAR EL SOPORTE CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Crear una configuración de CORS
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. Especificar los orígenes permitidos
        configuration.setAllowedOrigins(Arrays.asList("https://frontend.com", "http://localhost:3000"));

        // 2. Especificar los métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. Especificar los encabezados permitidos
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // 4. Permitir el envío de credenciales (cookies, tokens)
        configuration.setAllowCredentials(true);

        // 5. Configurar el tiempo de caché de CORS (en segundos)
        configuration.setMaxAge(3600L);

        // 6. Especificar los encabezados expuestos al cliente
        configuration.setExposedHeaders(Arrays.asList("Custom-Header"));

        // Registrar la configuración de CORS para todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encriptación de contraseñas
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Gestor de autenticación
    }
}