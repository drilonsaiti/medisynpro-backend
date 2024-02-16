package com.example.medisyncpro.web.rest;


import com.example.medisyncpro.config.JwtService;
import com.example.medisyncpro.model.dto.TokenDto;
import com.example.medisyncpro.model.dto.UserProfileDto;
import com.example.medisyncpro.model.dto.UserUsernameDto;
import com.example.medisyncpro.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://medisyncpro.vercel.app")
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;



    @GetMapping()
    public ResponseEntity<List<UserUsernameDto>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("find")
    public ResponseEntity<UserUsernameDto> getUser(@RequestParam String token) {
        return ResponseEntity.ok(this.userService.findUser(token));
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletRequest response) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        String accesToken = "";
        System.out.println("AUTH HEADER: " + authHeader);
        if (authHeader != null || authHeader.startsWith("Bearer ")) {

            jwt = authHeader.substring(7);
            System.out.println(jwt);
            username = jwtService.extractUsername(jwt);

            UserDetails userDetails = this.userService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                request.getSession().setAttribute("username", username);
                SecurityContextHolder.getContext().setAuthentication(authToken);
                accesToken = jwtService.generateToken(userDetails);
            } else {
                accesToken = "Refresh token not valid";
            }
        } else {
            accesToken = "No authorization";

        }
        return ResponseEntity.ok(accesToken);

    }

    @PostMapping("token/expired")
    public ResponseEntity<Boolean> checkIsTokenExpired(@RequestBody TokenDto token) {
        System.out.println("TOKEN EXPIRED");
        System.out.println(jwtService.isTokenExpired(token.getToken()));
       return  ResponseEntity.ok(jwtService.isTokenExpired(token.getToken()));

    }

   /* @PostMapping("verification-code")
    public ResponseEntity.BodyBuilder sendVerificationCode(@RequestBody VerificationDto dto) throws MessagingException, IOException, GeneralSecurityException {

        this.userService.sendVerificationEmail(dto);
        return  ResponseEntity.ok();

    }*/

    @PostMapping("delete-account")
    public ResponseEntity.BodyBuilder deleteAccount(@RequestBody TokenDto dto) {

        this.userService.deleteAccount(dto.getToken());
        return  ResponseEntity.ok();

    }

    @GetMapping("profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@RequestParam String email) {
        return ResponseEntity.ok(this.userService.findUserProfile(email));
    }



}