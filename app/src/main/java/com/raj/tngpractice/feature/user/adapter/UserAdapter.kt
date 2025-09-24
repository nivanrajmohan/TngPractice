package com.raj.tngpractice.feature.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raj.tngpractice.R
import com.raj.tngpractice.databinding.ListUserBinding
import com.raj.tngpractice.feature.user.model.User

class UserAdapter : ListAdapter<User, UserAdapter.UserHolder>(DIFFUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(binding)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = currentList[position]
        val address = user.address.suite.plus(" ")
            .plus(user.address.street).plus(" ")
            .plus(user.address.city).plus(" ")
            .plus(user.address.zipcode).plus(" ")
        holder.listUserBinding.apply {
            tvName.text = holder.itemView.context.getString(R.string.name_, user.name)
            tvEmail.text = holder.itemView.context.getString(R.string.email_, user.email)
            tvAddress.text = holder.itemView.context.getString(R.string.address_, address)
            tvPhone.text = holder.itemView.context.getString(R.string.phone_, user.phone)
            tvWebsite.text = holder.itemView.context.getString(R.string.website_, user.website)
            tvCompanyName.text = holder.itemView.context.getString(R.string.company_name, user.company.name)
            tvCatchPhrase.text = holder.itemView.context.getString(R.string.catchphrase_, user.company.catchPhrase)
        }
    }

    inner class UserHolder(val listUserBinding: ListUserBinding) : RecyclerView.ViewHolder(listUserBinding.root)

    private class DIFFUtilItemCallback : DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem == newItem
        }
    }

}