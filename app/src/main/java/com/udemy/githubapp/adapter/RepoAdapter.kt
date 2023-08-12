package com.udemy.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udemy.githubapp.databinding.ItemRepoBinding
import com.udemy.githubapp.model.Repo


class RepoAdapter(private val onclick: (Repo) -> Unit):
    ListAdapter<Repo, RepoAdapter.ViewHolder>(diffUtil) {


    inner class ViewHolder(private val viewBinding: ItemRepoBinding) :
        RecyclerView.ViewHolder(viewBinding.root){

        fun bind(item: Repo) {
            viewBinding.repoNameTextView.text = item.name
            if(viewBinding.repoNameTextView!!.text.length > 18){
                val preview = viewBinding.repoNameTextView!!.text.substring(0,18) + "..."
                viewBinding.repoNameTextView!!.text = preview
            }
            viewBinding.descriptionTextView.text = item.description
            viewBinding.starCountTextView.text = item.starCount.toString()
            viewBinding.forkCountTextView.text = item.forkCount.toString()
            viewBinding.createAtTextView.text = item.created_at
            val createText =  viewBinding.createAtTextView!!.text.substring(0,10)
            viewBinding.createAtTextView!!.text = createText

            viewBinding.root.setOnClickListener {
                onclick(item)
            }
        }

    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Repo>(){
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(
            ItemRepoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

