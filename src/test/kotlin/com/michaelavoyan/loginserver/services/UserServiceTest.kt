/**
 * Created by Michael Avoyan on 20/11/2024.
 *
 * Copyright 2022 Velocity Career Labs inc.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.michaelavoyan.loginserver.services

import com.michaelavoyan.loginserver.controllers.UserController
import com.michaelavoyan.loginserver.entities.User
import com.michaelavoyan.loginserver.requests.UserLoginRequest
import com.michaelavoyan.loginserver.requests.UserRegisterRequest
import com.michaelavoyan.loginserver.requests.UserUpdateRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus

class UserControllerTest {

    private val userService = mock(UserService::class.java)
    private val userController = UserController(userService)

    @Test
    fun `register user successfully`() {
        val request = UserRegisterRequest("username", "password", "email@example.com")
        val user = User(1L, "username 1", "password 1", "email@example.com")

        `when`(userService.createUser(request.username, request.password, request.email)).thenReturn(user)

        val response = userController.register(request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(user, response.body)
        verify(userService).createUser(request.username, request.password, request.email)
    }

    @Test
    fun `login user successfully`() {
        val request = UserLoginRequest("username", "password")

        `when`(userService.authenticate(request.username, request.password)).thenReturn(true)

        val response = userController.login(request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Login successful", response.body)
        verify(userService).authenticate(request.username, request.password)
    }

    @Test
    fun `login user fails with invalid credentials`() {
        val request = UserLoginRequest("username", "wrong password")

        `when`(userService.authenticate(request.username, request.password)).thenReturn(false)

        val response = userController.login(request)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertEquals("Invalid credentials", response.body)
        verify(userService).authenticate(request.username, request.password)
    }

    @Test
    fun `get user by id successfully`() {
        val user = User(1L, "username 1", "password 1", "email@example.com")

        `when`(userService.getUser(1L)).thenReturn(user)

        val response = userController.getUser(1L)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(user, response.body)
        verify(userService).getUser(1L)
    }

    @Test
    fun `get user by id fails when user does not exist`() {
        `when`(userService.getUser(1L)).thenReturn(null)

        val response = userController.getUser(1L)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(userService).getUser(1L)
    }

    @Test
    fun `update user successfully`() {
        val request = UserUpdateRequest("newemail@example.com")
        val updatedUser = User(1L, "username 1", "password 1", "newemail@example.com")

        `when`(userService.updateUser(1L, request.email)).thenReturn(updatedUser)

        val response = userController.updateUser(1L, request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(updatedUser, response.body)
        verify(userService).updateUser(1L, request.email)
    }

    @Test
    fun `update user fails when user does not exist`() {
        val request = UserUpdateRequest("newemail@example.com")

        `when`(userService.updateUser(1L, request.email)).thenReturn(null)

        val response = userController.updateUser(1L, request)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(userService).updateUser(1L, request.email)
    }

    @Test
    fun `update user fails with server error`() {
        val request = UserUpdateRequest("newemail@example.com")

        `when`(userService.updateUser(1L, request.email)).thenThrow(RuntimeException("Database error"))

        val response = userController.updateUser(1L, request)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertNull(response.body)
        verify(userService).updateUser(1L, request.email)
    }

    @Test
    fun `delete user successfully`() {
        doNothing().`when`(userService).deleteUser(1L)

        val response = userController.deleteUser(1L)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
        verify(userService).deleteUser(1L)
    }
}

