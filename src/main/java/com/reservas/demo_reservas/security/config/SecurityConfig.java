package com.reservas.demo_reservas.security.config;

import lombok.RequiredArgsConstructor;
import com.reservas.demo_reservas.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ── Autenticación — pública ──────────────────────────────────
                        .requestMatchers("/auth/**").permitAll()

                        // ── Usuarios — solo ADMIN ────────────────────────────────────
                        .requestMatchers(HttpMethod.POST,   "/usuarios/crear").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/usuarios/listar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/usuarios/consultar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/usuarios/editar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/eliminar/**").hasRole("ADMIN")

                        // ── Sucursales — escritura solo ADMIN, lectura ambos ─────────
                        .requestMatchers(HttpMethod.POST,   "/sucursales/crear").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/sucursales/editar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/sucursales/eliminar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/sucursales/**").hasAnyRole("ADMIN", "GESTOR")

                        // ── Salones — escritura solo ADMIN, lectura ambos ────────────
                        .requestMatchers(HttpMethod.POST,   "/salones/crear").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/salones/editar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/salones/eliminar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/salones/**").hasAnyRole("ADMIN", "GESTOR")

                        // ── Reservas — flujo general (ambos roles) ───────────────────
                        .requestMatchers(HttpMethod.POST, "/reservas/crear").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.POST, "/reservas/finalizar").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.GET,  "/reservas/listar").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.GET,  "/reservas/consultar/**").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.GET,  "/reservas/listar/*/activas").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.GET,  "/reservas/buscar-documento").hasAnyRole("ADMIN", "GESTOR")

                        // ── Reservas — aprobación y rechazo solo ADMIN ───────────────
                        .requestMatchers(HttpMethod.POST, "/reservas/*/aprobar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/reservas/*/rechazar").hasRole("ADMIN")

                        // ── Reservas — editar y eliminar (ambos, o solo ADMIN si prefieres)
                        .requestMatchers(HttpMethod.PUT,    "/reservas/editar/**").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.DELETE, "/reservas/eliminar/**").hasRole("ADMIN")

                        // ── Indicadores ADMIN y GESTOR ───────────────────────────────
                        .requestMatchers(HttpMethod.GET, "/indicadores/top-clientes").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.GET, "/indicadores/top-clientes/salon/**").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.GET, "/indicadores/clientes-primera-vez").hasAnyRole("ADMIN", "GESTOR")
                        .requestMatchers(HttpMethod.GET, "/indicadores/reservas-activas/**").hasAnyRole("ADMIN", "GESTOR")

                        // ── Indicadores solo GESTOR ──────────────────────────────────
                        .requestMatchers(HttpMethod.GET, "/indicadores/ganancias/**").hasRole("GESTOR")

                        // ── Indicadores solo ADMIN ───────────────────────────────────
                        .requestMatchers(HttpMethod.GET, "/indicadores/top-sucursales").hasRole("ADMIN")

                        // ── Notificaciones — solo ADMIN ──────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/notificaciones").hasRole("ADMIN")

                        // ── Cualquier otra ruta autenticada ──────────────────────────
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

}
