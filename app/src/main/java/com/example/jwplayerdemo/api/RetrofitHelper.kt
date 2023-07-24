package com.example.jwplayerdemo.api


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

   private const val baseUrl = "https://stage-nlearn.gcf.education/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
            //Adding App Version as Header to every api
             .addHeader("versionCode", "50")
            .addHeader("versionName", "3.3.9.debug")
            .addHeader("platform", "mobile")
            .addHeader("isSureMDM", "false")
            .addHeader("source", "nlearn")
            .addHeader("Authorization", "Bearer eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpZCI6IlQ5ZjR3aGFlNUIiLCJ1c2VyIjp7Im9iamVjdElkIjoiVDlmNHdoYWU1QiIsImFkbWlzc2lvbk51bWJlciI6Im1nbXQxMWxlYXJuMTI2IiwiU3RyZWFtIjoiU1BvdFJxZ1RIVCIsIkNvbGxlZ2UiOiJTZmxmUFdyWGhKIiwiQmF0Y2giOiJtZ210WDZhejRRIiwiQ2xhc3MiOiJ5MTYzZXdpZ0VDIiwiU3ViQmF0Y2giOiJlQkVhNHZta1JzIiwic3R1ZGVudE5hbWUiOiJtZ210MTFsZWFybjEyNiIsImFkZHJlc3MiOiJhZGRyZXNzIiwic3RhdGUiOiJNQU5BR0VNRU5UIFNUQVRFIE5FVyIsIlNlY3Rpb24iOiJuMjE2cDM1OENlIiwibGFzdExvZ2luIjoiMjAyMy0wNy0yMVQxNzoxODoxNiswNTozMCIsImlzUHJvZmlsZVVwZGF0ZWQiOnRydWUsInJlZ2lzdHJhdGlvblN0YXR1cyI6MSwiZXhhbU1vZGVsTGlzdCI6WyJ1OWtoelNxYW4zIiwidjNBNk5LRkFiQSIsImpDVDlHRDhXQmsiLCJRQ3BMTTFXZEVVIiwidGlvSUJPUVFRSiIsIlVIenZxaG96UTIiLCJ4OUhMeDVZaDY3IiwicUN1WlhRTFV4RiIsImRWQ1RLQVppaVQiLCJZVDVYYjFPeDFlIl0sIlllYXIiOiJ3VWh4R1hvd0FkIiwiUmVnaXN0cmF0aW9uU3RhdGUiOiJMbWpzTG13ZWtZIiwicHJvZmlsZUltYWdlIjpudWxsLCJkaXNwbGF5TmFtZSI6bnVsbCwiQ291cnNlIjoiN0V4Z2pjYmRHcyIsIkJvYXJkIjoiRzZiTWtNYkJiNSIsInZlcnNpb25Db2RlIjpudWxsLCJ0ZXh0UGFzc3dvcmQiOm51bGwsInByb2ZpbGV1cGRhdGVkYWF0IjpudWxsLCJOVFNFRXhhbSI6bnVsbCwibWF0ZXJpYWxGZWVTdGF0dXMiOiJOTyIsInRhZyI6Imdyb3VwYXBpIiwibWV0YURhdGEiOnt9LCJzb3VyY2UiOm51bGwsImJhdGNoX2lkIjoibWdtdFg2YXo0USIsImJhdGNoX25hbWUiOiJNQU5BR0VNRU5UIiwiTGVhcm5pbmdQYWNrYWdlSWQiOjEsIlByYWN0aWNlUGFja2FnZUlkIjoxLCJMZWFybmluZ1BhY2thZ2UiOiJ1eUtVdTZQQ3hBIiwiUHJhY3RpY2VQYWNrYWdlIjoiSGNTRTVHSXlJTSIsInN0YXRlT2JqZWN0SWQiOiJ0MllNUzdlSmpYIiwiU3RhdGUiOiJ0MllNUzdlSmpYIiwic3RhdGVOYW1lIjoiTUFOQUdFTUVOVCBTVEFURSBORVciLCJEaXN0cmljdCI6Ikd5eXVrQkhjVmsiLCJkaXN0cmljdE5hbWUiOiJERU1PIERJU1RSSUNUIiwiY29sbGVnZU5hbWUiOiJNQU5BR0VNRU5UIE5FVyIsImJhdGNoTmFtZSI6Ik1BTkFHRU1FTlQiLCJzdWJCYXRjaE5hbWUiOiJNQU5BR0VNRU5UIiwic3ViQmF0Y2hJZCI6ImVCRWE0dm1rUnMiLCJ5ZWFyTmFtZSI6IjIwMjAtMjAyMSIsIlJlZ2lzdHJhdGlvblN0YXR1cyI6IkxtanNMbXdla1kiLCJSZWdpc3RyYXRpb25TdGF0dXNOYW1lIjoicmVnaXN0cmF0aW9uIiwiY2xhc3NOYW1lIjoiMTFUSCBTVEQiLCJjbGFzc0lkIjoieTE2M2V3aWdFQyIsInNlY3Rpb25OYW1lIjoiNTAxIiwicGxhdGZvcm0iOiJubGVhcm4iLCJuR3VpZGVQcm9ncmFtQ29kZSI6bnVsbCwiYm9hcmROYW1lIjoiQ0JTRSJ9LCJqd3RJRCI6IjE1ZjlmNGQ2YTVkZjZiMTZkODNjN2NlODBlNzdlODY0NGZiN2I1MjA4NGM3MzEyZjA3Y2IwMTVhZGE2ZjQwN2IiLCJpYXQiOjE2ODk5NDAwOTYsImV4cCI6MTY5MjUzMjA5NiwiaXNzIjoiand0LW5sZWFybiJ9.MIGIAkIA8VZOP0gg962UA-cB9pnSwZFlAETGjQDdF4bluMZCLE4cS6G4V7mDvJ7E9F54Rio4fNjq5x7fIgB_1XlcQOYWotcCQgCIKkTZwE3KziwcRa1WSZdgBQ_8cDE1ys3yDrqIedwUAicP3DLIdTihtsn29wHXfaRgyvrUfYc7j-vfm4XHVKHv3A")
            .addHeader("admissionNumber", "mgmt11learn156")
            chain.proceed(request.build())
        }
        .build()


    fun getInstance(): ApiInterface {
        val retro= Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return  retro.create(ApiInterface::class.java )
    }
}
