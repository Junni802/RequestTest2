package com.example.requesttest2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import com.example.requesttest2.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import kotlin.concurrent.thread
import java.net.URL as URL

class MainActivity : AppCompatActivity() {
	val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		binding.txtMain.movementMethod = ScrollingMovementMethod.getInstance()

		binding.btn1.setOnClickListener {
			thread(start = true) {
				try {
					val url = URL("http://192.168.0.29:8083/woodaProject/main.jsp")

					val conn = url.openConnection() as HttpURLConnection
					conn.requestMethod = "GET"

					if (conn.responseCode == HttpURLConnection.HTTP_OK) {
						// 연결이 정상적으로 이루어 졌으면
						val streamReader = InputStreamReader(conn.inputStream)
						val buffer = BufferedReader(streamReader)

						val content = StringBuffer()
						// 요청에 대한 응답으로 다운받을 파일의 전체 내용을 저장할 변수
						// 다운 받으면서 계속 변경될 문자열이기 때문에 String 대신 StringBuffer를 사용
						while (true) {
							var data = buffer.readLine() ?: break
							content.append(data)
							content.append("\n")
						}

						buffer.close()
						conn.disconnect()
						Log.d("requestTest", "${content}")
						binding.txtMain.setText("${content}")

					} else {
						Log.d("requestTest", "연결이 잘못되었습니다.")
					}
				}catch (e: Exception){
					e.printStackTrace()
				}
			}
		}
	}
}