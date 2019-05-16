package com.github.rwsbillyang.nitritedemo


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


/**
 * @author Anindya Chatterjee.
 */
class UserAdapter(internal var mContext: Context) : BaseAdapter() {
    private var users: List<User>? = null
    private var mInflator: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    fun setUsers(users: List<User>) {
        this.users = users
    }

    override fun getCount(): Int {
        return if (users != null) users!!.size else 0
    }

    override fun getItem(i: Int): User {
        return users!![i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, convertView: View?, container: ViewGroup): View {
        var convertView = convertView

        val holder: Viewholder

        val t = getItem(position)

        if (convertView == null) {
            holder = Viewholder()
            convertView = mInflator.inflate(R.layout.row_user, container, false)
            holder.username = convertView!!.findViewById(R.id.username)
            convertView.setTag(holder)

        } else {
            holder = convertView.getTag() as Viewholder
        }

        holder.username!!.text = t.username

        return convertView
    }

    private inner class Viewholder {
        internal var username: TextView? = null
    }
}