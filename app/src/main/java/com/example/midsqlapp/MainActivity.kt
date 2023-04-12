package com.example.midsqlapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var editName: EditText
    lateinit var editEmail:EditText
    lateinit var editIdNumber:EditText
    lateinit var btnSave:Button
    lateinit var btnView:Button
    lateinit var btnDelete:Button
    lateinit var db:SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editName = findViewById(R.id.mEdtName)
        editEmail = findViewById(R.id.mEdtEmail)
        editIdNumber = findViewById(R.id.mEdtNumber)
        btnSave = findViewById(R.id.mBtnSave)
        btnView = findViewById(R.id.mBtnView)
        btnDelete = findViewById(R.id.mBtnDelete)
        //Create a database called eMobilisDB
        db = openOrCreateDatabase("eMobilisDB",Context.MODE_PRIVATE, null )
        // create a table called
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR,arafa VARCHAR,kitambulisho VARCHAR)")
        //set on click listeners
        btnSave.setOnClickListener {
            var name = editName.text.toString().trim()
            var email = editEmail.text.toString().trim()
            var idNumber = editIdNumber.text.toString().trim()

            if (name.isEmpty()|| email.isEmpty()||idNumber.isEmpty()){
                message("Empty field!!", "Please fill all inputs")
            }else{
                db.execSQL("Insert into users values('"+name+"','"+email+"','"+idNumber+"')")
                clear()
                message("Succes!!", "user saved")
            }
        }
        btnView.setOnClickListener {
            //Use cursor to select data
            var cursor = db.rawQuery("SELECT * FROM users",null)
            if(cursor.count ==0){
                message("NO RECORDS!!","Fuck off")
            }else{
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n\n")
                }
                message("USERS", buffer.toString())
            }

        }
        btnDelete.setOnClickListener {
            val IdNumber = editIdNumber.text.toString().trim()
            if (IdNumber.isEmpty()){
                message("Empty fields", "fill the field")
            }else{
                var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho = ''"null)
                if (cursor.count == 0){
                    message("NO RECORD FOUND", "Sorry there is no user with provided id")
                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho = '"+IdNumber+'")
                }
            }
        }
    }

    fun message (title:String, message:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Cancel",null)
        alertDialog.create().show()
    }
    fun clear(){
        editName.setText("")
        editEmail.setText("")
        editIdNumber.setText("")
    }
}