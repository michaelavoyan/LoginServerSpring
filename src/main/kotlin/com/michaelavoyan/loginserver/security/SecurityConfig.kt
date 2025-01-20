/**
 * Created by Michael Avoyan on 20/01/2025.
 *
 * Copyright 2022 Velocity Career Labs inc.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.michaelavoyan.loginserver.security

import com.michaelavoyan.loginserver.security.Constants.ACCESS_TOKEN
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
//            .csrf().disable() // Disable CSRF if not needed
            .authorizeRequests()
            .anyRequest().authenticated() // All requests require authentication
            .and()
            .addFilterBefore(
                AccessTokenFilter(ACCESS_TOKEN),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }
}

