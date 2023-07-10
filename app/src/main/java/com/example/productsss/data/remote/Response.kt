package com.example.productsss.data.remote

class Response<T> {

    companion object {
        @JvmStatic
        val SuccessCode = 20000
    }


    var code: Int = 0
    var data: T? = null

    val isSuccess: Boolean
        get() = SuccessCode == code
}