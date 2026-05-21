package com.eventhub.service.impl;

import com.eventhub.dto.auth.*;
import com.eventhub.entity.User;
import com.eventhub.entity.enums.Role;
import com.eventhub.exception.BusinessException; import static com.eventhub.exception.ErrorMessages.*;
import com.eventhub.repository.UserRepository;
import com.eventhub.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserMeResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        Role role = request.roles() != null && !request.roles().isEmpty() ? 
                request.roles().iterator().next() : Role.ROLE_USER;

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .createdAt(OffsetDateTime.now())
                .build();

        userRepository.save(user);
        return toMe(user);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(INVALID_CREDENTIALS);
        }

        var authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                Set.of(() -> user.getRole().name())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new AuthResponseDTO("session-auth", "Session", toMe(user));
    }

    @Override
    public UserMeResponseDTO me() {
        return toMe(getAuthenticatedUser());
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new BusinessException(UNAUTHENTICATED_USER);
        }
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new BusinessException(AUTHENTICATED_USER_NOT_FOUND));
    }

    private UserMeResponseDTO toMe(User user) {
        return new UserMeResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                Set.of(user.getRole().name()),
                user.getCreatedAt()
        );
    }
}
