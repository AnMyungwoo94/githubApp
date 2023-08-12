package com.udemy.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udemy.githubapp.databinding.ItemBinding
import com.udemy.githubapp.model.User

class UserAdapter(val onClick:(User)-> Unit): ListAdapter<User, UserAdapter.ViewHolder >(diffutil) {

    inner class ViewHolder(private  val viewBinding : ItemBinding)
        : RecyclerView.ViewHolder(viewBinding.root){

            fun bind(item: User){
                viewBinding.userTextView.text = item.username
                viewBinding.root.setOnClickListener {
                    onClick(item)
                }
            }
        }

        companion object{
            val diffutil = object : DiffUtil.ItemCallback<User>(){
                override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                    return oldItem == newItem
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //리스트 아이템에 선정되어 있는 아이템은 currentList로 불러올 수 있음
        holder.bind(currentList[position])
    }
}