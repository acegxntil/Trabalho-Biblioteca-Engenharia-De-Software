package com.sgbu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/recuperar-senha", "/redefinir-senha", "/css/**", "/js/**", "/h2-console/**").permitAll()
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                .requestMatchers("/usuarios/**").hasAnyRole("ADMIN", "BIBLIOTECARIO")
                .requestMatchers("/emprestimos/registrar", "/emprestimos/devolucao", "/emprestimos/devolver")
                    .hasRole("BIBLIOTECARIO")
                .requestMatchers("/reservas/nova", "/reservas/minhas",
                        "/multas/minhas",
                        "/sugestoes/nova", "/sugestoes/salvar", "/emprestimos/meu-historico",
                        "/emprestimos/historico/**")
                    .hasAnyRole("ESTUDANTE", "PROFESSOR")
                .requestMatchers("/reservas/gerenciar", "/reservas/cancelar/**",
                        "/multas/quitar/**", "/sugestoes/listar", "/sugestoes/analisar/**",
                        "/relatorios/**")
                    .hasAnyRole("BIBLIOTECARIO", "ADMIN")
                .requestMatchers("/livros/novo", "/livros/salvar", "/livros/editar/**", "/livros/atualizar")
                    .hasAnyRole("BIBLIOTECARIO", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .headers(headers -> headers.frameOptions(config -> config.sameOrigin()))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
