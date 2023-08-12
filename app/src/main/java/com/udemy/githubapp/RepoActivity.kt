package com.udemy.githubapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udemy.githubapp.adapter.RepoAdapter
import com.udemy.githubapp.databinding.ActivityRepoBinding
import com.udemy.githubapp.model.Repo
import com.udemy.githubapp.network.GithubService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class RepoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoBinding
    private lateinit var repoAdapter: RepoAdapter
    private var page = 0
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //인텐트값 받기(검색한 사용자의 아이디)
        val username = intent.getStringExtra("username") ?: return
        binding.usernameTextView.text = username

        repoAdapter = RepoAdapter {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.htmlUrl))
            startActivity(intent)

        }
        val linearLayoutManager = LinearLayoutManager(this@RepoActivity)

        binding.repoRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = repoAdapter
        }

        //페이징 처리하기
        binding.repoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // findFirstCompletelyVisibleItemPosition 마지막 아이템을 가져오는 함수
                val totalCount = linearLayoutManager.itemCount
                val lastVisiblaPosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                //lastVisiblaPosition = 0부터시작 , totalCount = 1부터 시작이라 -1을 해줌
                if (lastVisiblaPosition >= (totalCount - 1) && hasMore) {
                    page += 1
                    listRepo(username, page)
                }
            }
        })
        listRepo(username, 0)
    }

    private fun listRepo(username: String, page: Int) {
        val githubService = ApiClient.retrofit.create(GithubService::class.java)
        //listRepos을 통해서 API호출
        //enqueue를 통해서 콜백을 받아옴
        githubService.listRepos(username, page).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.e("Main", response.body().toString())
                hasMore = response.body()?.count() == 30
                //orEmpty() null값일때 빈 리스트로 추가
                repoAdapter.submitList(repoAdapter.currentList + response.body().orEmpty())
                if (response.body().toString() == "[]") {
                    Toast.makeText(this@RepoActivity, "결과가 없습니다", Toast.LENGTH_SHORT).show()
                    Log.e("RepoActivity", "결과가 없습니다")
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
            }
        })
    }
}