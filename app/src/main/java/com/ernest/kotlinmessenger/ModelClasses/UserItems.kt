package com.ernest.kotlinmessenger.ModelClasses

import com.ernest.kotlinmessenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class UserItems: Item<GroupieViewHolder>() {
    override fun getLayout(): Int {

        return R.layout.select_users_single_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }


}