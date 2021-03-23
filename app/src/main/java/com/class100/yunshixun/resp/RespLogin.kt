package com.class100.yunshixun.resp

data class Token(val token: String, val expired_at: String)
data class RespLogin(val token: Token)