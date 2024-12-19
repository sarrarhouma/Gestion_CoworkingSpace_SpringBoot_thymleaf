package com.poly.gestioncoworkingspace.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class securityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(form -> form.permitAll()) // Permettre l'accès à la page de login
       .logout(logout -> logout
                .logoutUrl("/logout") // URL pour la déconnexion
                .logoutSuccessUrl("/login?logout") // Redirige vers /login après la déconnexion
                .permitAll()) // Déconnexion
        .exceptionHandling(exception -> exception.accessDeniedPage("/erreur")); // Page d'accès refusé

        httpSecurity.authorizeHttpRequests(request -> request
                // ADMIN peut ajouter/modifier/supprimer des rooms
                .requestMatchers("/rooms/admin/**").hasRole("ADMIN")

                // Routes accessibles uniquement en GET pour USER et ADMIN (visualisation)
                .requestMatchers(HttpMethod.GET, "/rooms/**").hasAnyRole("USER", "ADMIN")
                // Routes members réservées à ADMIN
                .requestMatchers("/members/**").hasRole("ADMIN")

                // Routes subscriptions réservées à ADMIN
                .requestMatchers("/subscriptions/**").hasRole("ADMIN")

                // ADMIN peut gérer les réservations
                .requestMatchers("/reservations/admin/**").hasRole("ADMIN")

                // USER et ADMIN peuvent visualiser les réservations
                .requestMatchers(HttpMethod.GET, "/reservations/**").hasAnyRole("USER", "ADMIN")

                // Toutes les autres requêtes nécessitent une authentification
                .anyRequest().authenticated()
        );

        return httpSecurity.build();
    }


    @Bean
    InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("user")
                        .password(passwordEncoder().encode("user123"))
                        .roles("USER")
                        .build(),
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin123"))
                        .roles("USER", "ADMIN")
                        .build()
        );
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
