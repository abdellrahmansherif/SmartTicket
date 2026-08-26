package com.smartticket.identity.internal.web.Dto;

import com.smartticket.identity.internal.domain.Role;
import com.smartticket.identity.internal.domain.UserStatus;

import java.util.UUID;

public record UserResponse(UUID id,
                               String email,
                               String firstName,
                               String lastName,
                               Role role,
                               UserStatus status) {
}
