package com.udemy.githubapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemy.githubapp.adapter.UserAdapter
import com.udemy.githubapp.databinding.ActivityMainBinding
import com.udemy.githubapp.model.Repo
import com.udemy.githubapp.model.UserDto
import com.udemy.githubapp.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private var searchFor: String = ""
    private val handler= Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter{
            val intent = Intent(this@MainActivity, RepoActivity::class.java)
            intent.putExtra("username", it.username)
            startActivity(intent)
        }

        binding.userRecyclerview.apply {
            //context 뷰에 있는거
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }

        //검색시 사용자가 완전히 키보드를 다 치고 나서 search가 되도록 함.
        val runnable = Runnable{
            searchUser()
        }
        binding.searchEditText.addTextChangedListener {
            searchFor = it.toString()
            //핸들러를 사용해서 이전에 있던 작업을 지우고 새로운 값을 넣음
            //3초 이후에 변화된 값이 없다면 그때 실행
            handler.removeCallbacks(runnable)
            handler.postDelayed(
                runnable,
                1000,
            )
        }
    }

private fun searchUser(){
    val githubService =  ApiClient.retrofit.create(GithubService::class.java)

    githubService.searchUsers(searchFor).enqueue(object : Callback<UserDto> {
        override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
            Log.e("MainActivity", "Search User : ${response.body().toString()}")
                    userAdapter.submitList(response.body()?.items)
                }
                override fun onFailure(call: Call<UserDto>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
        }
    }