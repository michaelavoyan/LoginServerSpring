/**
 * Created by Michael Avoyan on 12/01/2025.
 *
 * Copyright 2022 Velocity Career Labs inc.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.michaelavoyan.loginserver.requests

import jakarta.validation.constraints.NotBlank

data class UserLoginRequest(
    @field:NotBlank(message = "Username must not be blank")
    val username: String,
    @field:NotBlank(message = "Password must not be blank")
    val password: String
)
