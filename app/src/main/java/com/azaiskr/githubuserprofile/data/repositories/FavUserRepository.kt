package com.azaiskr.githubuserprofile.data.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.azaiskr.githubuserprofile.data.local.FavUserDao
import com.azaiskr.githubuserprofile.data.local.FavUserDb
import com.azaiskr.githubuserprofile.data.local.FavUserEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class FavUserRepository(application: Application) {
    private val favUserDao: FavUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavUserDb.getInstance(application)
        favUserDao = db.favUserDao()
    }

    fun getFavUserList(): LiveData<List<FavUserEntity>> {
        return favUserDao.getFavUserList()
    }

    fun addFavUser(user: FavUserEntity) {
        executorService.execute { favUserDao.addFavUser(user) }
    }

    fun deleteFavUser(user: FavUserEntity?) {
        executorService.execute { favUserDao.deleteFavUser(user) }
    }

    fun getFavUser(username: String): LiveData<FavUserEntity?> {
        return favUserDao.getFavUser(username)
    }


}