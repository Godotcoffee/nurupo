package com.nurupo.movie.users.bean

import org.springframework.stereotype.Component
import java.time.Duration

@Component
class UserConfig {
    val USER_LOGIN_VALID_DURATION = Duration.ofSeconds(30)
}