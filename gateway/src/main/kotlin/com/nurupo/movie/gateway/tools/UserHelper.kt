package com.nurupo.movie.gateway.tools

import com.nurupo.movie.gateway.config.ServiceName
import com.nurupo.movie.gateway.entity.ResponseJSON
import org.springframework.web.client.RestTemplate

object UserHelper {
    fun userLoginCheck(restTemplate: RestTemplate, id: String?, token: String?, v: String): ResponseJSON {
        return RestJSONHelper.restGet(
                restTemplate,
                "http://${ServiceName.nurupoMovieUsers}/$v/user/token/$id/$token"
        )
    }
    fun loginValid(resp: ResponseJSON?): Boolean {
        return resp != null && resp.status == 0 && resp.obj == true
    }
}