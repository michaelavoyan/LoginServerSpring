/**
 * Created by Michael Avoyan on 19/11/2024.
 *
 * Copyright 2022 Velocity Career Labs inc.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.michaelavoyan.loginserver.controllers

import com.michaelavoyan.loginserver.entities.User
import com.michaelavoyan.loginserver.requests.UserLoginRequest
import com.michaelavoyan.loginserver.requests.UserRegisterRequest
import com.michaelavoyan.loginserver.requests.UserUpdateRequest
import com.michaelavoyan.loginserver.services.UserService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@Validated
class UserController(private val userService: UserService) {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: UserRegisterRequest
    ): ResponseEntity<User> {
        logger.info("Received registration request for username: {}", request.username)
        return try {
            val user = userService.createUser(request.username, request.password, request.email)
            logger.info("User successfully created: {}", user)
            ResponseEntity.ok(user)
        } catch (e: Exception) {
            logger.error("Error occurred during registration: {}", e.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: UserLoginRequest
    ): ResponseEntity<String> {
        logger.info("Login attempt for username: {}", request.username)
        val authenticated = userService.authenticate(request.username, request.password)
        return if (authenticated) {
            logger.info("Login successful for username: {}", request.username)
            ResponseEntity.ok("Login successful")
        } else {
            logger.warn("Invalid login attempt for username: {}", request.username)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")
        }
    }

    @GetMapping("/{id}")
    fun getUser(
        @PathVariable id: Long
    ): ResponseEntity<User> {
        logger.info("Fetching user with ID: {}", id)
        val user = userService.getUser(id)
        return if (user != null) {
            logger.info("User found: {}", user)
            ResponseEntity.ok(user)
        } else {
            logger.warn("User not found for ID: {}", id)
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserUpdateRequest
    ): ResponseEntity<User> {
        logger.info("Received request to update user with ID: {}", id)
        return try {
            val user = userService.updateUser(id, request.email)
            if (user != null) {
                logger.info("User updated successfully: {}", user)
                ResponseEntity.ok(user)
            } else {
                logger.warn("User not found for ID: {}", id)
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            logger.error("Error while updating user with ID {}: {}", id, e.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        logger.info("Attempting to delete user with ID: {}", id)
        return try {
            userService.deleteUser(id)
            logger.info("User deleted successfully with ID: {}", id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            logger.error("Error while deleting user with ID {}: {}", id, e.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
