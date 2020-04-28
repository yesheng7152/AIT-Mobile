package hu.ait.aitforum.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import hu.ait.aitforum.R
import hu.ait.aitforum.data.Post
import kotlinx.android.synthetic.main.post_row.view.*

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    lateinit var context: Context
    var  postsList = mutableListOf<Post>()
    var  postKeys = mutableListOf<String>()

    lateinit var currentUid: String

    constructor(context: Context, uid: String) : super() {
        this.context = context
        this.currentUid = uid
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.post_row, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var post = postsList.get(holder.adapterPosition)

        holder.tvAuthor.text = post.author
        holder.tvTitle.text = post.title
        holder.tvBody.text = post.body

        if (currentUid == post.uid) {
            holder.btnDelete.visibility = View.VISIBLE
            holder.btnDelete.setOnClickListener{
                removePost(holder.adapterPosition)
            }
        } else {
            holder.btnDelete.visibility = View.GONE
        }

        if(post.imgUrl.isNotEmpty()){
            holder.ivPhoto.visibility = View.VISIBLE
            Glide.with(context).load(post.imgUrl).into(holder.ivPhoto)
        }else{
            holder.ivPhoto.visibility = View.GONE
        }

    }

    fun addPost(post: Post, key: String) {
        postsList.add(post)
        postKeys.add(key)
        notifyDataSetChanged()
    }

    private fun removePost(index: Int) {
        FirebaseFirestore.getInstance().collection("posts").document(
            postKeys[index]
        ).delete()

//        postsList.removeAt(index)
//        postKeys.removeAt(index)
//        notifyItemRemoved(index)
    }


    fun removePostByKey(key: String) {
        var index = postKeys.indexOf(key)
        if (index != -1){
            postsList.removeAt(index)
            postKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvAuthor = itemView.tvAuthor
        var tvTitle = itemView.tvTitle
        var tvBody = itemView.tvBody
        var btnDelete = itemView.btnDelete
        var ivPhoto = itemView.ivPhoto
    }
}