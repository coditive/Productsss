package com.example.productsss.data.remote.converter

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.File
import java.lang.reflect.Type

class FileRequestBodyConverter : Converter<File, RequestBody> {
    override fun convert(value: File): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), value)
    }
}


class FileConverterFactory : Converter.Factory() {
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        if (type == File::class.java) {
            return FileRequestBodyConverter()
        }
        return null
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return null
    }
}