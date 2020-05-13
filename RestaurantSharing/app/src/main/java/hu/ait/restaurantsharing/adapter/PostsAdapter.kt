package hu.ait.restaurantsharing.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import hu.ait.restaurantsharing.DetailActivity
import hu.ait.restaurantsharing.R
import hu.ait.restaurantsharing.data.Post
import kotlinx.android.synthetic.main.post_row.view.*


class PostsAdapter : RecyclerView.Adapter<PostsAdapter.ViewHolder>{
    lateinit var context: Context
    lateinit var currentUid: String
    var postsList = mutableListOf<Post>()
    var postsKeys = mutableListOf<String>()

    constructor(context: Context, uid: String) : super(){
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
        holder.tvRestaurantName.text = post.restaurantName
        selectCuisineIcon(post,holder)
        if(currentUid == post.uid){
            holder.btnDelete.visibility = View.VISIBLE
            holder.btnDelete.setOnClickListener {
                removePost(holder.adapterPosition)
            }
        }else{
            holder.btnDelete.visibility = View.GONE
        }
        holder.rbRating.setRating(post.rating)
        holder.cardView.setOnClickListener {
            val intent = Intent(it.getContext(), DetailActivity::class.java)
            intent.putExtra("KEY",postsKeys[holder.adapterPosition])
            context.startActivity(intent)
        }
        holder.tvPriceRange.text = post.priceRange
    }

    private fun selectCuisineIcon(post: Post, holder: ViewHolder){
        when(post.cuisine) {
            0 -> holder.ivCuisine.setImageResource(R.drawable.chinese_food)
            1 -> holder.ivCuisine.setImageResource(R.drawable.mexican_food)
            2 -> holder.ivCuisine.setImageResource(R.drawable.italian_food)
            3 -> holder.ivCuisine.setImageResource(R.drawable.japanese_food)
            4 -> holder.ivCuisine.setImageResource(R.drawable.greek_food)
            5 -> holder.ivCuisine.setImageResource(R.drawable.french_food)
            6 -> holder.ivCuisine.setImageResource(R.drawable.thai_food)
            7 -> holder.ivCuisine.setImageResource(R.drawable.spanish_food)
            8 -> holder.ivCuisine.setImageResource(R.drawable.indian_food)
            9 -> holder.ivCuisine.setImageResource(R.drawable.mediterranean_food)
            10 -> holder.ivCuisine.setImageResource(R.drawable.other_food)
        }
    }

    fun addPost(post: Post, key: String){
        postsList.add(post)
        postsKeys.add(key)
        notifyDataSetChanged()
    }

    private fun removePost(index: Int){
        FirebaseFirestore.getInstance().collection("posts").
        document(postsKeys[index]).delete()
    }

    fun removePostsByKey(key: String){
        var index = postsKeys.indexOf(key)
        if (index != -1){
            postsList.removeAt(index)
            postsKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(postView: View) : RecyclerView.ViewHolder(postView){
        var tvAuthor = postView.tvAuthor
        var tvRestaurantName = postView.tvRestaurantName
        var ivCuisine = postView.ivCuisine
        var btnDelete = postView.btnDelete
        var rbRating = postView.rbRatingV
        var cardView = postView.card_view
        var tvPriceRange = postView.tvPriceRange
    }
}