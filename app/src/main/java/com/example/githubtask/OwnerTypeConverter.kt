package com.example.githubtask

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OwnerTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromOwner(owner: Owner?): String? {
        return owner?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toOwner(ownerString: String?): Owner? {
        return ownerString?.let {
            gson.fromJson(it, Owner::class.java)
        }
    }
}
