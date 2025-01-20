package com.michaelavoyan.loginserver.controllers

/**
 * Created by Michael Avoyan on 12/01/2025.
 *
 * Copyright 2022 Velocity Career Labs inc.
 * SPDX-License-Identifier: Apache-2.0
 */

import com.michaelavoyan.loginserver.entities.User
import com.michaelavoyan.loginserver.requests.UserLoginRequest
import com.michaelavoyan.loginserver.requests.UserRegisterRequest
import com.michaelavoyan.loginserver.requests.UserUpdateRequest
import com.michaelavoyan.loginserver.services.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserControllerTests {

    private val userService = mockk<UserService>()
    private val userController = UserController(userService)

    @Test
    fun `register should create a new user`() {
        val request = UserRegisterRequest("testuser", "password123", "testuser@example.com")
        val user = User(1L, "testuser", "password123", "testuser@example.com")

        every { userService.createUser(request.username, request.password, request.email) } returns user

        val response = userController.register(request)

        assertEquals(ResponseEntity.ok(user), response)
        assertNotNull(response.body)
        assertEquals("testuser", response.body!!.username)
    }

    @Test
    fun `login should return success for valid credentials`() {
        val request = UserLoginRequest("testuser", "password123")

        every { userService.authenticate(request.username, request.password) } returns true

        val response = userController.login(request)

        assertEquals(ResponseEntity.ok("Login successful"), response)
    }

    @Test
    fun `login should return unauthorized for invalid credentials`() {
        val request = UserLoginRequest("testuser", "wrongpassword")

        every { userService.authenticate(request.username, request.password) } returns false

        val response = userController.login(request)

        assertEquals(401, response.statusCode.value())
        assertEquals("Invalid credentials", response.body)
    }

    @Test
    fun `getUser should return user if found`() {
        val user = User(1L, "testuser", "password123", "testuser@example.com")

        every { userService.getUser(1L) } returns user

        val response = userController.getUser(1L)

        assertEquals(ResponseEntity.ok(user), response)
        assertNotNull(response.body)
        assertEquals(1L, response.body!!.id)
    }

    @Test
    fun `getUser should return not found if user does not exist`() {
        every { userService.getUser(1L) } returns null

        val response = userController.getUser(1L)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    fun `updateUser should update and return the user`() {
        val request = UserUpdateRequest("newemail@example.com")
        val updatedUser = User(1L, "testuser", "password123", "newemail@example.com")

        every { userService.updateUser(1L, request.email) } returns updatedUser

        val response = userController.updateUser(1L, request)

        assertEquals(ResponseEntity.ok(updatedUser), response)
        assertNotNull(response.body)
        assertEquals("newemail@example.com", response.body!!.email)
    }

    @Test
    fun `updateUser should return not found if user does not exist`() {
        val request = UserUpdateRequest("newemail@example.com")

        every { userService.updateUser(1L, request.email) } returns null

        val response = userController.updateUser(1L, request)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    fun `deleteUser should return no content`() {
        every { userService.deleteUser(1L) } returns Unit

        val response = userController.deleteUser(1L)

        assertEquals(204, response.statusCode.value())
    }
}

