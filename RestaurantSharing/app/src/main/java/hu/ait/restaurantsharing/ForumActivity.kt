package hu.ait.restaurantsharing

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import hu.ait.restaurantsharing.adapter.PostsAdapter
import hu.ait.restaurantsharing.data.Post
import kotlinx.android.synthetic.main.activity_form.*

class ForumActivity : AppCompatActivity() {

    private lateinit var postsAdapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        setSupportActionBar(toolbar)

        postsAdapter = PostsAdapter(this, FirebaseAuth.getInstance().currentUser!!.uid)
        recyclerPosts.adapter = postsAdapter

        initPosts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)

        val searchItem = menu?.findItem(R.id.acSearch)
        val searchView = searchItem!!.actionView as androidx.appcompat.widget.SearchView


        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                postsAdapter.getFilter()?.filter(newText)
                return false
            }
        })
        return true
        //return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.acAddPost) {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    fun initPosts(){
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("posts")

        query.addSnapshotListener(
            object : EventListener<QuerySnapshot>{
                override fun onEvent(qSnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                    if(e != null){
                        Toast.makeText(this@ForumActivity, "Error: ${e.message}",
                        Toast.LENGTH_LONG).show()
                        return
                    }
                    for (docChange in qSnapshot?.getDocumentChanges()!!){
                        if(docChange.type == DocumentChange.Type.ADDED){
                            val post = docChange.document.toObject(Post::class.java)
                            postsAdapter.addPost(post, docChange.document.id)
                        }else if (docChange.type == DocumentChange.Type.REMOVED){
                            postsAdapter.removePostsByKey(docChange.document.id)
                        }else if (docChange.type == DocumentChange.Type.MODIFIED){

                        }
                    }
                }
            }
        )
    }
}
