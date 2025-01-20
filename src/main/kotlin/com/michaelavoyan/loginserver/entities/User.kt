/**
 * Created by Michael Avoyan on 19/11/2024.
 *
 * Copyright 2022 Velocity Career Labs inc.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.michaelavoyan.loginserver.entities

//import jakarta.persistence.*

//@Entity
data class User(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
//    @Column(nullable = false, unique = true)
    val username: String,
//    @Column(nullable = false)
    var password: String,
//    @Column(nullable = false)
    var email: String
)
