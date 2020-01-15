package pl.pwsztar.to_doer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main_window.*
import pl.pwsztar.to_doer.utils.verifyUser


class MainActivity : AppCompatActivity() {

    private var uid:String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_window)

        uid = verifyUser()
        if(uid == null) {
            Log.d("[MainActivity]", "uid is null")
            Toast.makeText(this, "You are not logged in!", Toast.LENGTH_SHORT)
                .show()
            goToLoginActivity()
        }
        firebaseUsername()

        firebaseTasks()
        go_back_btn2.setOnClickListener { goToLoginActivity() }
        register_btn.setOnClickListener { goToNewTaskActivity() }

    }

    private fun goToNewTaskActivity() {
        val intent = Intent(this, NewTaskActivity::class.java)
        startActivity(intent)
    }


    private fun goToLoginActivity() {
        if(AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut()
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun firebaseTasks() {
        val ref = FirebaseDatabase.getInstance().getReference("/tasks/").child("$uid")
        val eventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshots: DataSnapshot) {
                var str = ""
                for(ds in snapshots.children) {
                    val cat = ds.child("category").getValue(String::class.java)
                    val name = ds.child("name").getValue(String::class.java)
                    val date = ds.child("taskDate").getValue(String::class.java)
                    if(!date.isNullOrEmpty()) {
                        str += "$date\t\t\t\t $name\t\t\t\t\t\t $cat\n"
                    }
                }
                textView11.text = str
            }
        }
        ref.addListenerForSingleValueEvent(eventListener)
    }

    private fun firebaseUsername() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/").child("$uid")
        val eventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshots: DataSnapshot) {
                var asdf:String? = "  aaaa "
                for(ds in snapshots.children) {
                 asdf = ds.child("email").getValue(String::class.java)
             }
                textView20.text = asdf
            }
        }

        ref.addListenerForSingleValueEvent(eventListener)
    }



}