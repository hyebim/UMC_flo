package umcandroid.essential.week02_flo_1.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import umcandroid.essential.week02_flo_1.data.entities.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM UserTable")
    fun getUsers() : List<User>

    @Query("SELECT * FROM UserTable WHERE email =:email AND password = :password")
    fun getUser(email:String, password:String) : User?
}