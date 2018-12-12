package com.nurupo.movie.users.bean

import org.springframework.stereotype.Component
import java.time.Duration

@Component
class UserConfig {
    val userLoginDuration: Duration = Duration.ofHours(2)
    val maxPageSize = 100
}