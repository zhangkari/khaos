package com.class100.yunshixun.req

import com.class100.hades.http.HaApiRequest
import com.class100.hades.http.HaRequest

class ReqLogin(private val phone: String, private val password: String) : HaApiRequest() {
    override fun getUrl(): String {
        return "/base/login"
    }

    override fun buildParameters(map: MutableMap<String, String>) {
        super.buildParameters(map)
        map["phone"] = phone;
        map["password"] = password;
    }

    override fun getMethod(): Int {
        return HaRequest.METHOD_POST
    }
}