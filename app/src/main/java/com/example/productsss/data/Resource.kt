package com.example.productsss.data


sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class DataError<T>(message: String) : Resource<T>(null, message)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is DataError -> "Error[exception=$message]"
            is Loading<T> -> "Loading"
        }
    }

    fun handleDataError(action: (String) -> Unit): Resource<T> {
        if (this is DataError) {
            action(message!!)
        }
        return this
    }

}