package com.example.medisyncpro.config.auth;
import com.example.medisyncpro.model.enums.Role;
import com.example.medisyncpro.model.enums.TokenType;

import com.example.medisyncpro.config.JwtService;
import com.example.medisyncpro.model.User;
import com.example.medisyncpro.model.excp.UserNotFoundException;
import com.example.medisyncpro.repository.UserRepository;
import com.example.medisyncpro.service.BlackListedTokenService;
import com.example.medisyncpro.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final BlackListedTokenService blacklistedTokenService;

  private final UserService userService;
  private final UserRepository userRepository;

  public AuthenticationResponse register(RegisterRequest request) throws Exception {
    User user = this.userService.register(request.getFullName(),request.getEmail(), request.getPassword(), request.getRepeatPassword(),request.getUserType());

    var jwtToken = jwtService.generateToken(user);
    var jwtRefreshToken = jwtService.generateRefreshToken(user);
    return AuthenticationResponse.builder()
            .token(jwtToken).refreshToken(jwtRefreshToken)
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) throws ServletException {

    UserDetails user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserNotFoundException(request.getEmail()));


    if (!passwordEncoder.matches(request.getPassword(),user.getPassword())){
      throw new BadCredentialsException("Password is incorrect");
    }
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    var jwtToken = jwtService.generateToken(user);
    System.out.println("JWT TOKEN: " + jwtToken);
    var jwtRefreshToken = jwtService.generateRefreshToken(user);
    Role role = this.userRepository.findByEmail(request.getEmail()).get().getRole();

    return AuthenticationResponse.builder()
        .token(jwtToken).refreshToken(jwtRefreshToken).role(role)
        .build();
  }

  public void logout(String accessToken, String refreshToken) {
    if (accessToken != null && !blacklistedTokenService.isTokenBlacklisted(accessToken, TokenType.ACCESS)) {
      blacklistedTokenService.blacklistToken(accessToken, TokenType.ACCESS);
    }
    if (refreshToken != null && !blacklistedTokenService.isTokenBlacklisted(refreshToken, TokenType.REFRESH)) {
      blacklistedTokenService.blacklistToken(refreshToken, TokenType.REFRESH);
    }
  }

}
