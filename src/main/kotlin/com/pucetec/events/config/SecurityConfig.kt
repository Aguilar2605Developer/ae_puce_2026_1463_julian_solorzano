package com.pucetec.events.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

// Configura la seguridad del microservicio con JWT de Cognito
@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // Desactiva CSRF porque usamos JWT (stateless)
            .csrf { it.disable() }
            // No se guardan sesiones, cada request se valida con su token
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    // Público: cualquiera puede ver la cartelera de eventos
                    .requestMatchers(HttpMethod.GET, "/api/events", "/api/events/**").permitAll()
                    // Todo lo demás requiere token válido de Cognito
                    .anyRequest().authenticated()
            }
            // Valida el JWT contra el JWKS de Cognito
            .oauth2ResourceServer { oauth2 -> oauth2.jwt { } }
        return http.build()
    }
}