# SGBU - Sistema de Gerenciamento de Biblioteca Universitaria

Sistema web para automatizacao dos processos de uma biblioteca academica: catalogo, emprestimos, reservas, multas, notificacoes e relatorios.

## Tecnologias

- Java 24 + Spring Boot 3.4.4
- Maven
- Thymeleaf + Bootstrap 5
- Spring Security (BCrypt, session-based)
- Spring Data JPA + Hibernate
- H2 (dev) / MySQL (prod)
- OpenPDF (relatorios)
- Spring Mail (notificacoes)

## Requisitos

- JDK 24+
- Maven 3.9+

## Executar

```bash
mvn spring-boot:run
```

Acessar: http://localhost:8080

## Credenciais Padrao

| Perfil | Email | Senha |
|--------|-------|-------|
| Admin | admin@sgbu.com | admin123 |
| Bibliotecario | biblio@sgbu.com | biblio123 |
| Estudante | estudante@sgbu.com | estudante123 |
| Professor | professor@sgbu.com | professor123 |

## Funcionalidades

- Autenticacao e recuperacao de senha
- Cadastro e busca de livros no catalogo
- Emprestimo e devolucao de exemplares
- Reserva online com fila de espera
- Calculo automatico de multas
- Notificacoes por email
- Historico de emprestimos do usuario
- Sugestao de aquisicao de titulos
- Relatorios e dashboard administrativo
- Configuracoes de prazos e multas
- Gerenciamento de usuarios e perfis
=======
# Trabalho-Biblioteca-Engenharia-De-Software

