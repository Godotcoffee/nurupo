package com.nurupo.movie.users.tools

import com.nurupo.movie.users.entity.User
import org.springframework.stereotype.Component
import java.security.MessageDigest

@Component
class UserPasswordHashImpl: IPasswordHash<User> {
    override fun hash(t: User): String {
        return md5("${t.username} ${t.password}")
    }

    companion object {
        fun md5(str: String): String {
            val digest = MessageDigest
                    .getInstance("MD5")
                    .digest(str.toByteArray())
            val sb = StringBuilder()
            for (b in digest) {
                (b.toInt() and 0xff).toString(0x10).let {
                    sb.append(if (it.length < 2) "0$it" else it)
                }
            }
            return sb.toString()
        }
    }
}