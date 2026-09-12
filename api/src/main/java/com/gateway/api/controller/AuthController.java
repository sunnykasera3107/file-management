package com.gateway.api.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.api.annotation.ApiController;
import com.gateway.api.dto.login.LoginRequest;
import com.gateway.api.dto.register.RegisterRequest;
import com.gateway.api.service.AuthService;
import com.gateway.api.service.JwtService;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@ApiController 
@Tag(
    name = "Authentication & Authorization", 
    description = "This API for authentication & authorization before any request."
)
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    public AuthController(
        AuthService authService,
        JwtService jwtService
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @Operation(
        summary = "Get CSRF token for secured request",
        description = "Generates csrf token for rest api request."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "User token successfully generated")
        }
    )
    @GetMapping("/csrf")
    public CsrfToken getCSRF(
        @Parameter(hidden = true) CsrfToken token
    ) {
        return token;
    }

    @Operation(
        summary = "Register user account",
        description = "Based on user information it register user account.",
        security = {
            @SecurityRequirement(name = "csrfToken")
        }
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "User account successfully created")
        }
    )
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
        @Valid @RequestBody RegisterRequest request
    ) throws Exception{
        Map<String, String> response = authService.registerUser(request);
        return ResponseEntity.created(null)
                .body(response);
                
    }

    @Operation(
        summary = "Login user account",
        description = "Based on username or email and password, user can login.",
        security = {
            @SecurityRequirement(name = "csrfToken")
        }
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully")
        }
    )
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
        @Valid @RequestBody LoginRequest request
    ) throws Exception {
        Map<String, String> response = authService.loginUser(request);

        Claims claims = jwtService.extractToken(response.get("token"));

        ResponseCookie cookie = ResponseCookie
                .from("access_token", response.get("token"))
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(claims.getExpiration().getTime())
                .build();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Logged in successfully."));
    }

    @Operation(
        summary = "Logout user account",
        description = "It logs out user which is currently logged in.",
        security = {
            @SecurityRequirement(name = "csrfToken")
        }
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "User logged out successfully.")
        }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(
        @Parameter(hidden = true) HttpServletRequest request
    ) {
        ResponseCookie cookie = ResponseCookie
                .from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Logged out successfully."));
    }
}
