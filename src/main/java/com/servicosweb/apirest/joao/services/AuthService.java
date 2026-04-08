package com.servicosweb.apirest.joao.services;

import com.servicosweb.apirest.joao.dtos.auth.LoginRequestDTO;
import com.servicosweb.apirest.joao.entities.Usuario;
import com.servicosweb.apirest.joao.exceptions.RegraDeNegocioException;
import com.servicosweb.apirest.joao.repositories.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public String autenticar(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário ou senha inválidos."));

        if (!passwordEncoder.matches(dto.senha(), usuario.getPassword())) {
            throw new RegraDeNegocioException("Usuário ou senha inválidos.");
        }

        Instant now = Instant.now();
        long expiresAt = 3600L;

        String scope = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("servicos-web-api")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresAt))
                .subject(usuario.getId().toString())
                .claim("email", usuario.getEmail())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}