package id.neuman.identity.controller;

import id.neuman.common.dto.response.BaseResponse;
import id.neuman.identity.dto.request.AuthRequest;
import id.neuman.identity.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<String>> login(@RequestBody AuthRequest request) {
        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("Success", authenticationService.login(request)));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaseResponse<>("Created", authenticationService.register(request)));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<BaseResponse<Boolean>> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        log.info("TOKEN: {}", token);
        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("Success", authenticationService.validateToken(token)));
    }
}