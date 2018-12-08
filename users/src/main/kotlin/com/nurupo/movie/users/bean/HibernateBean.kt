package com.nurupo.movie.users.bean

import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManagerFactory

@Configuration
class HibernateBean {

    /*@Autowired
    lateinit var emf: EntityManagerFactory

    @Bean
    fun entityManager(): SessionFactory {
        return emf.unwrap(SessionFactory::class.java)
    }*/
}