package com.github.rwsbillyang.nitritedemo


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.dizitart.kno2.filters.gte
import org.dizitart.kno2.getRepository
import org.dizitart.no2.Nitrite
import org.dizitart.no2.event.ChangeInfo
import org.dizitart.no2.event.ChangeListener
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters
import org.dizitart.no2.util.Iterables


class MainActivity : AppCompatActivity() {
    internal lateinit var progressBar: ProgressBar
    internal lateinit var adapter: UserAdapter
    internal lateinit var listener: ChangeListener

    private var db: Nitrite? = null
    private var repository: ObjectRepository<User>? = null
    private var repository2: ObjectRepository<ComplexBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val userList = findViewById<ListView>(R.id.userlist)
        adapter = UserAdapter(this)
        userList.setAdapter(adapter)

        if (db == null) {
            progressBar.visibility = View.VISIBLE
            val fileName = filesDir.path + "/test.db"
            Log.i("Nitrite", "Nitrite file - $fileName")
            db = Nitrite.builder()
                .filePath(fileName)
                .openOrCreate("test-user", "test-password")

//            db = nitrite ("test-user", "test-password") {
//                nitriteMapper = JacksonMapper()
//                    .apply {
//                        // registerModule(ThreetenabpTimeModule())
//                        registerModule(KotlinModule())
//                        objectMapper.apply {
//                            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                        }
//                    }
//                file = File(application.filesDir, fileName)
//                autoCommitBufferSize = 2048
//                compress = true
//                autoCompact = false
//            }
            repository = db!!.getRepository<User>()
            repository2 = db!!.getRepository<ComplexBean>()

            listener = object : ChangeListener {
                override fun onChange(changeInfo: ChangeInfo) {
                    val users = repository!!.find().project<User>(User::class.java)
                    adapter.setUsers(Iterables.toList(users))
                    adapter.notifyDataSetChanged()
                }
            }

            repository!!.register(listener)
            progressBar.visibility = View.INVISIBLE
            getUsers()


        }
    }

    private fun getUsers() {
        var users = Iterables.toList(repository!!.find().project<User>(User::class.java))
        if (users == null)
            users = ArrayList()

        adapter.setUsers(users)
        adapter.notifyDataSetChanged()
    }

    private fun addUser() {
        val user = User("user " + System.currentTimeMillis(), "")
        repository!!.insert(user)
    }
    private fun filterUser(){
        Log.d("Nitrite", "filterUser")
        adapter.setUsers(repository!!.find(User::id gte  "22233443").toList())
    }
    private fun addComplexBean(){
        val beans = arrayListOf(Tuple("name1",13),Tuple("name2",23),Tuple("name3",33))
        val complexBean = ComplexBean("123",1,Address("street A","54321"),beans)

        repository2!!.insert(complexBean)
        val str = "addComplexBean: "+complexBean.toString()

        Log.d("Nitrite",str)

        Toast.makeText(this,str,Toast.LENGTH_SHORT).show()
    }

    private fun readComplexBean(){
        //org.dizitart.no2.exceptions.NitriteIOException: NO2.2004: store is closed
        //        at org.dizitart.no2.internals.DefaultNitriteCollection.checkOpened(DefaultNitriteCollection.java:394)
       val str = repository2!!.find().toList().map { it.toString() }.joinToString("; ")
        Log.d("Nitrite",str)
        Toast.makeText(this,str,Toast.LENGTH_LONG).show()
    }

    private fun flushUsers() {
        repository!!.remove(ObjectFilters.ALL)
    }

    override fun onDestroy() {
        super.onDestroy()
        repository!!.deregister(listener)
        db?.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()


        if (id == R.id.add_user) {
            addUser()
            return true
        }
        if(id == R.id.filter_user){
            filterUser()
            return true
        }

        if (id == R.id.flush_user) {
            flushUsers()
            return true
        }

        if(id == R.id.find_bean)
        {
            addComplexBean()
            return true
        }
        if(id == R.id.read_bean)
        {
            readComplexBean()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}