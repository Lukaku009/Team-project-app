package com.example.crime

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class AdminMissingActivity : AppCompatActivity() {

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mSharedPref: SharedPreferences
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mRef: DatabaseReference
    private lateinit var mainToolbar: Toolbar
    private lateinit var fireAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mainToolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)
        supportActionBar?.title = "Missing Reports"

        fireAuth = FirebaseAuth.getInstance()
        mSharedPref = getSharedPreferences("SortSettings", MODE_PRIVATE)
        val mSorting = mSharedPref.getString("Sort", "newest") ?: "newest"

        mLayoutManager = LinearLayoutManager(this).apply {
            reverseLayout = mSorting == "newest"
            stackFromEnd = mSorting == "newest"
        }

        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = mLayoutManager

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        val user1 = fireAuth.currentUser
        if (user1 == null) {
            sendToLogin()
        } else {
            mRef = mFirebaseDatabase.getReference("Admin_Missing").child(user1.uid)
        }

        firebaseSearch("")
    }

    private fun firebaseSearch(searchText: String) {
        val query = searchText
        val firebaseSearchQuery: Query = mRef.orderByChild("name").startAt(query).endAt(query + "\uf8ff")

        val options = FirebaseRecyclerOptions.Builder<Missing>()
            .setQuery(firebaseSearchQuery, Missing::class.java)
            .build()

        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Missing, ViewHolder>(options) {
            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int, model: Missing) {
                viewHolder.setDetails(applicationContext, model.name, model.age, model.dresscolor, model.image, model.city, model.gender, model.description, model.address)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_missing, parent, false)
                return ViewHolder(itemView).apply {
                    setOnClickListener(object : ViewHolder.ClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            // Handle item click
                        }

                        override fun onItemLongClick(view: View, position: Int) {
                            // Handle item long click
                        }
                    })
                }
            }
        }

        mRecyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                firebaseSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                firebaseSearch(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort -> {
                showSortDialog()
                true
            }
            R.id.actionProfile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.actionlogoutbtn -> {
                fireAuth.signOut()
                sendToLogin()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSortDialog() {
        val sortOptions = arrayOf("Newest", "Oldest")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sort by")
            .setIcon(R.drawable.ic_action_sort)
            .setItems(sortOptions) { _, which ->
                val editor = mSharedPref.edit()
                editor.putString("Sort", if (which == 0) "newest" else "oldest")
                editor.apply()
                recreate()
            }
        builder.show()
    }

    private fun sendToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
}
