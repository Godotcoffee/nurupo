package com.nurupo.movie.users.tools

import org.springframework.core.io.ClassPathResource
import java.io.File

fun main(args: Array<String>) {
    val res = ClassPathResource("data/usernameAll.txt")
    val nameList = mutableListOf<String>()
    res.file.forEachLine { line ->
        nameList.add(line)
    }
    File("./username610.txt").printWriter().use { pw ->
        nameList.filter{ it.length in 4..16 }.shuffled().take(610).forEach { name ->
            pw.println(name.capitalize())
        }
    }

}