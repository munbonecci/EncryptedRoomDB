package com.mun.bonecci.encryptedroomdb.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.*
import com.mun.bonecci.encryptedroomdb.data.User
import com.mun.bonecci.encryptedroomdb.databinding.ItemUserLayoutBinding

/**
 * Adapter for the user list RecyclerView.
 *
 * @param clickListener The listener for item click events.
 * @param context The context of the adapter.
 */
class UserListAdapter(private val clickListener: OnClickListener, private val context: Context) :
    ListAdapter<User, ViewHolder>(TaskDiffCallBack()) {

    /**
     * Callback for calculating the diff between two non-null items in a list.
     */
    class TaskDiffCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Creates a new ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HolderUser(
            ItemUserLayoutBinding.inflate(inflater, parent, false)
        )
    }

    /**
     * Binds data to the ViewHolder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as HolderUser).bind(item, position)
    }

    private var itemSelected = -1

    /**
     * ViewHolder for each user item.
     */
    inner class HolderUser(
        private val binding: ItemUserLayoutBinding
    ) : ViewHolder(binding.root) {

        /**
         * Binds data to the views.
         */
        @SuppressLint("SetTextI18n")
        fun bind(item: User, position: Int) {
            val circle = "●"
            binding.labelTextView.text = "${item.name} $circle ${item.email}"
            binding.deleteButton.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    clickListener.onDeletePressed(item, position)
                }
            }
            itemView.setOnClickListener {
                clickListener.onItemClick(item, position)
            }
        }
    }

    /**
     * Sets the selected item position and notifies the adapter.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedItem(position: Int) {
        itemSelected = position
        notifyDataSetChanged()
    }

    /**
     * Interface for item click events.
     */
    interface OnClickListener {
        fun onItemClick(user: User, position: Int)
        fun onDeletePressed(user: User, position: Int)
    }
}