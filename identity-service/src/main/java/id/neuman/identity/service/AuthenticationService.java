package id.neuman.identity.service;

import id.neuman.identity.dto.request.AuthRequest;
import id.neuman.identity.model.User;
import id.neuman.identity.repository.UserRepository;
import id.neuman.identity.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public String login(AuthRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());
        authenticationManager.authenticate(authenticationToken);
        return jwtUtil.generate(request.getEmail());
    }

    public String register(AuthRequest request) {
        boolean isEmailExists = userRepository.existsByEmail(request.getEmail());
        if (isEmailExists) {
            throw new IllegalArgumentException("Email already used");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return jwtUtil.generate(user.getEmail());
    }

    public boolean validateToken(String token) {
        token = token.split(" ")[1];
        return jwtUtil.validate(token);
    }
}
