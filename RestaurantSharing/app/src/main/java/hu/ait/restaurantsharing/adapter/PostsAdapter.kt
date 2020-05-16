package hu.ait.restaurantsharing.adapter

import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import hu.ait.restaurantsharing.DetailActivity
import hu.ait.restaurantsharing.R
import hu.ait.restaurantsharing.data.Post
import kotlinx.android.synthetic.main.post_row.view.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class PostsAdapter : RecyclerView.Adapter<PostsAdapter.ViewHolder>, Filterable{
    lateinit var context: Context
    lateinit var currentUid: String

    var postsList = mutableListOf<Post>()
    var postsKeys = mutableListOf<String>()
    var postListSearch = mutableListOf<Post>()
    var postKeySearch = mutableListOf<String>()

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
        postListSearch.add(post)
        notifyDataSetChanged()
    }

    private fun removePost(index: Int){
        FirebaseFirestore.getInstance().collection("posts").
        document(postsKeys[index]).delete()
    }

    fun removePostsByKey(key: String){
        var index = postsKeys.indexOf(key)
        if (index != -1){
            var deletePost = postListSearch.indexOf(postsList[index])
            postListSearch.removeAt(deletePost)
            postsList.removeAt(index)
            postsKeys.removeAt(index)
            postKeySearch.removeAt(postKeySearch.indexOf(key))
            notifyItemRemoved(index)
        }
    }

    fun declareMap():HashMap<Int, Float>{
        var map = HashMap<Int, Float>()
        for (i in 0 until postsList.size){
            map.put(i,postsList[i].rating)
        }
        return map
    }
    fun sortByRating(map: HashMap<Int, Float>) {
        var sortedMap=map.toList().sortedBy { (_, value) -> value}.toMap()
        var indexList = ArrayList(sortedMap.keys)

        var keyList = ArrayList<String>()
        for (i in 0 until indexList.size){
            var index = indexList[i].toString()
            keyList.add(postsKeys[index.toInt()])
        }

        postsKeys = keyList
        postsList.sortBy { it.rating }
        notifyDataSetChanged()
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

    fun updateSearchList(){
        postListSearch.clear()
        postKeySearch.clear()
        for(post in postsList){
            postListSearch.add(post)
        }
        for(key in postsKeys){
            postKeySearch.add(key)
        }

    }

    fun searchList(){
        if(postListSearch.isEmpty()){
            for(post in postsList){
                postListSearch.add(post)
            }
        }
        if(postKeySearch.isEmpty()){
            for(key in postsKeys){
                postKeySearch.add(key)
            }
        }
    }

    override fun getFilter(): Filter {
        searchList()
        return exampleFilter
    }

    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<Post> = ArrayList()
            val filterKeyList: MutableList<String> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(postListSearch)
                filterKeyList.addAll(postKeySearch)
            } else {
                val filterPattern =
                    constraint.toString().toLowerCase().trim { it <= ' ' }
                for(i in 0 until postListSearch.size){
                    if (postListSearch[i].restaurantName.toLowerCase().contains(filterPattern)) {
                        filteredList.add(postListSearch[i])
                        filterKeyList.add(postKeySearch[i])
                    }
                }
            }
            val combList = mutableListOf<Any>(filteredList,filterKeyList)
            val results = FilterResults()
            results.values = combList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            postsList.clear()
            postsKeys.clear()
            var filteredLists = results?.values as MutableList<Any>
            var postListFiltered = filteredLists[0] as ArrayList<Post>
            var keyListFiltered = filteredLists[1] as ArrayList<String>
            postsList.addAll(postListFiltered)
            postsKeys.addAll(keyListFiltered)
            notifyDataSetChanged()
        }
    }
}
