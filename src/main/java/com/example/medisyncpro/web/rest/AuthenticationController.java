package com.example.medisyncpro.web.rest;

import com.example.medisyncpro.config.JwtService;
import com.example.medisyncpro.config.auth.AuthenticationRequest;
import com.example.medisyncpro.config.auth.AuthenticationResponse;
import com.example.medisyncpro.config.auth.AuthenticationService;
import com.example.medisyncpro.config.auth.RegisterRequest;
import com.example.medisyncpro.model.dto.*;
import com.example.medisyncpro.model.excp.*;
import com.example.medisyncpro.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
public class AuthenticationController {

    private final AuthenticationService service;

    private final JwtService jwtService;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws Exception {

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    @ExceptionHandler(value = {BadCredentialsException.class, UserNotFoundException.class})
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request, HttpServletRequest req, HttpServletResponse response, final RuntimeException ex
    ) throws ServletException {
        try {
            req.getSession().setAttribute("username", request.getEmail());
            AuthenticationResponse authenticationResponse = service.authenticate(request,response);

            return ResponseEntity.ok(authenticationResponse);
        } catch (BadCredentialsException | UserNotFoundException e) {
            System.out.println("ERROR");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody TokensDto tokens) {
        service.logout(tokens.getAccessToken(),tokens.getRefreshToken());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/extract-username")
    public ResponseEntity<String> getUsername(@RequestBody TokenDto token) {
        return ResponseEntity.ok(jwtService.extractUsername(token.getToken()));
    }

    @PostMapping("/extract-email")
    public ResponseEntity<String> getEmail(@RequestBody TokenDto token) {
        return ResponseEntity.ok(this.userService.extractEmail(jwtService.extractUsername(token.getToken())));
    }

    @GetMapping("/getRoles")
    public ResponseEntity<String> getRoles(HttpServletRequest req) {
        final String authHeader = req.getHeader("Authorization");
        final String jwt;
        if (authHeader != null || authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            return ResponseEntity.ok(this.userService.getRoles(jwtService.extractUsername(jwt)));
        }
        return ResponseEntity.ofNullable("User doesnt found");
    }



    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto change) throws ServletException {
        System.out.println("==============ITS CALLEDD=====================");
        try {
            this.userService.changePassword(change);
            return ResponseEntity.ok("Password has changed");

        } catch (UsernameNotFoundException | PasswordsDoNotMatchException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/change-fullname")
    public ResponseEntity<?> changeFullName(@RequestBody ChangeFullNameDto change) throws ServletException {
        try {
            this.userService.changeFullName(change);
            return ResponseEntity.ok("Full name has changed");

        } catch (UsernameNotFoundException  | PasswordsDoNotMatchException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/change-avatar")
    public ResponseEntity<?> changeFullName(@RequestBody ChangeAvatarDto change) throws ServletException {
        try {
            this.userService.changeAvatar(change);
            return ResponseEntity.ok("Avatar has changed");

        } catch (UsernameNotFoundException  | PasswordsDoNotMatchException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/change-email")
    public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailDto change) throws ServletException {
        try {
            this.userService.changeEmail(change);
            return ResponseEntity.ok("Email has changed");
        } catch (UsernameNotFoundException | PasswordsDoNotMatchException |
                 EmailDoesntExistsException |
                 EmailAlreadyExistsException | PasswordIncorrectExecption | VerificationCodeDoNotMatchException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPassword change) throws ServletException {
        try {
            this.userService.forgetPassword(change);
            return ResponseEntity.ok("Forget pass has changed");

        } catch (EmailDoesntExistsException | PasswordsDoNotMatchException |
                 VerificationCodeDoNotMatchException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
