package com.ernest.kotlinmessenger.Activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ernest.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class ActivityRegister : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    val firestoreDB = Firebase.firestore
    var storage = Firebase.storage
    var storageRef = storage.reference
    val TAG: String = "My Activity";
    var userDisplayPic: Uri? = null
    var display_picture: String? = null
    var fetchedDownloadUrl: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        when {
            ContextCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.

            }

            else -> {
                // You can directly ask for the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    100)
            }
        }

        profilePic.setOnClickListener {

//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, 100)

            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(this@ActivityRegister)

        }

        registerButton.setOnClickListener {
            validateFields()

        }

        toLoginPage.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null){
//
//            userDisplayPic = data.data
//
//            Glide.with(this)
//                .load(userDisplayPic)
//                .into(profilePic)
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK){

                var resultUri: Uri? = result.uri
                userDisplayPic = resultUri

                Glide.with(this)
                    .load(userDisplayPic)
                    .into(profilePic)
            }

        }
    }

    private fun validateFields() {

        val userPic = userDisplayPic.toString()
        val username = regusernameField.text.toString()
        val email = regEmailField.text.toString()
        val password = regPasswordField.text.toString()

        if (userPic.isEmpty()){
            Toast.makeText(
                baseContext, "Please Select a Profile Picture.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if (username.isEmpty()){
            Toast.makeText(
                baseContext, "Please Enter Username.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if (email.isEmpty()){

            Toast.makeText(
                baseContext, "Please Enter Email.",
                Toast.LENGTH_SHORT
            ).show()
        }

        else if (password.isEmpty()){

            Toast.makeText(
                baseContext, "Please enter password.",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            registerNewUser(username,email, password)
        }
    }

    private fun registerNewUser(username: String, email: String, password: String) {

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth!!.currentUser
                    val userID = mAuth!!.currentUser!!.uid

                    uploadImageToFirebase(userDisplayPic, username, email, userID, user)

                } else {
                    Log.w("Test Error", "createUserWithEmail:failure", task.exception);

                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun uploadImageToFirebase(
        userDisplayPic: Uri?,
        username: String,
        email: String,
        userID: String,
        user: FirebaseUser?
    ) {

        val fileName = UUID.randomUUID().toString()
        val storageReference = FirebaseStorage.getInstance().getReference("Profile Images/$username")
        storageReference.putFile(userDisplayPic!!).addOnSuccessListener {
            Log.d("Test_register", "Successfully uploaded Image: ${it.metadata?.path}")
//            var fetchedDownloadUrl = storageReference.downloadUrl.toString()

            storageReference.downloadUrl.addOnSuccessListener { uri ->
                fetchedDownloadUrl = uri.toString()

                val newUser = hashMapOf(
                    "username" to username,
                    "email" to email,
                    "userID" to userID,
                    "dp" to fetchedDownloadUrl.toString()
                )
                firestoreDB.collection("Users")
                    .document(userID)
                    .set(newUser)
                    .addOnSuccessListener {

                    }.addOnFailureListener {
                        Log.w("FirestoreError", "Error adding document", it)

                        Toast.makeText(
                            baseContext,
                            "Error adding User Data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            }
            updateUI(user)

        }.addOnFailureListener {
            Log.e("Test_register", "Failure uploading image test")
        }
    }

    private fun updateUI(user: FirebaseUser?) {

        val intent = Intent(this, ActivityConversations::class.java)
        startActivity(intent)
    }

}