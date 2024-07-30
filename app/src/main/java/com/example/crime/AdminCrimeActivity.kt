package com.example.crime

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminCrimeActivity : AppCompatActivity() {

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mSharedPref: SharedPreferences // saving sort settings
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mRef: DatabaseReference
    private lateinit var fireAuth: FirebaseAuth
    private val TAG = "AdminCrimeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up toolbar
        val mainToolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)
        supportActionBar?.title = "ADMIN SECTION: Crime Reports"

        // Initialize Firebase Auth
        fireAuth = FirebaseAuth.getInstance()

        // Initialize SharedPreferences for sort settings
        mSharedPref = getSharedPreferences("SortSettings", MODE_PRIVATE)
        val mSorting = mSharedPref.getString("Sort", "newest") // default = newest

        // Set up RecyclerView layout manager
        mLayoutManager = LinearLayoutManager(this)
        if (mSorting == "newest") {
            mLayoutManager.reverseLayout = true
            mLayoutManager.stackFromEnd = true
        } else {
            mLayoutManager.reverseLayout = false
            mLayoutManager.stackFromEnd = false
        }

        // Initialize RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = mLayoutManager

        // Initialize Firebase Database
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = mFirebaseDatabase.getReference("Crime")

        // Check if the user is authenticated
        val currentUser = fireAuth.currentUser
        if (currentUser == null) {
            sendToLogin()
        }
    }

    private fun firebaseSearch(searchText: String) {
        val firebaseSearchQuery = mRef.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff")

        val options = FirebaseRecyclerOptions.Builder<Post>()
            .setQuery(firebaseSearchQuery, Post::class.java)
            .build()

        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Post, ViewHolder>(options) {
            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int, post: Post) {
                viewHolder.setDetails(applicationContext, post.title, post.description, post.image, post.type)
                Log.d(TAG, post.title ?: "")
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
                val viewHolder = ViewHolder(view)
                viewHolder.setOnClickListener(object : ViewHolder.ClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val itemId = getRef(position).key
                        if (itemId != null) {
                            mRef.child(itemId).child("condition").setValue("Seen")
                        }
                    }

                    override fun onItemLongClick(view: View, position: Int) {
                        // TODO: Implement long item click action
                    }
                })
                return viewHolder
            }
        }

        mRecyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = fireAuth.currentUser
        if (currentUser == null) {
            sendToLogin()
        } else {
            val options = FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(mRef, Post::class.java)
                .build()

            val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Post, ViewHolder>(options) {
                override fun onBindViewHolder(viewHolder: ViewHolder, position: Int, post: Post) {
                    viewHolder.setDetails(applicationContext, post.title, post.description, post.image, post.type)
                    val itemId = getRef(position).key
                    if (itemId != null) {
                        mRef.child(itemId).child("condition").setValue("Seen")
                    }
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
                    val viewHolder = ViewHolder(view)
                    viewHolder.setOnClickListener(object : ViewHolder.ClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            val itemId = getRef(position).key
                            if (itemId != null) {
                                mRef.child(itemId).child("condition").setValue("Seen")
                            }
                        }

                        override fun onItemLongClick(view: View, position: Int) {
                            // TODO: Implement long item click action
                        }
                    })
                    return viewHolder
                }
            }

            mRecyclerView.adapter = firebaseRecyclerAdapter
            firebaseRecyclerAdapter.startListening()
        }
    }

    private fun sendToLogin() {
        val intent = Intent(this, AdminLoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    firebaseSearch(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    firebaseSearch(newText)
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_sort) {
            showSortDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        val sortOptions = arrayOf("Newest", "Oldest")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sort by")
        builder.setIcon(R.drawable.ic_action_sort)
        builder.setItems(sortOptions) { _, which ->
            val editor = mSharedPref.edit()
            if (which == 0) {
                editor.putString("Sort", "newest")
            } else {
                editor.putString("Sort", "oldest")
            }
            editor.apply()
            recreate() // Refresh activity to apply sorting
        }
        builder.show()
    }
}
