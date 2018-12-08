package com.nurupo.movie.users.dao.impl

import com.nurupo.movie.users.dao.IUserDao
import com.nurupo.movie.users.entity.User
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Service
class UserDaoImpl: IUserDao {
    @PersistenceContext
    lateinit var entityManager: EntityManager

    override fun getAllUsers(): List<User> {
        return entityManager.createQuery("from User", User::class.java).resultList
    }

    override fun getUserByName(name: String): User? {
        return entityManager.createQuery("from User where username = ?1", User::class.java)
                .setParameter(1, name)
                .resultList.firstOrNull()
    }

    @Transactional
    override fun addUser(user: User): Boolean {
        entityManager.persist(user)
        return true
    }
}