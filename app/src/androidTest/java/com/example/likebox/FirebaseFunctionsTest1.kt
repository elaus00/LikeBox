package com.example.likebox

import com.google.firebase.functions.FirebaseFunctions
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test

class FirebaseFunctionTest1 {
    private lateinit var functions: FirebaseFunctions

    @Before
    fun setup() {
        functions = FirebaseFunctions.getInstance()
    }

    @Test
    fun testFunction1Call() {
        runBlocking {
            try {
                val testData = mapOf(
                    "testParam" to "Hello from Android Test",
                    "timestamp" to System.currentTimeMillis()
                )

                println("📱 테스트 시작: $testData")

                val response = functions
                    .getHttpsCallable("testFunction1")
                    .call(testData)
                    .await()

                val result = response.getData() as? Map<String, Any>
                println("📱 서버 응답: $result")

                // success 체크
                assertTrue("API 호출이 실패했습니다", result?.get("success") as Boolean)

            } catch (e: Exception) {
                println("❌ 에러 발생: ${e.message}")
                throw e  // 테스트 실패 처리
            }
        }
    }
}