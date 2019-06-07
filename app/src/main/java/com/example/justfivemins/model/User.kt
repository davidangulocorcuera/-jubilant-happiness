package com.example.justfivemins.model

import android.os.Parcel
import android.os.Parcelable

class User(
    var email: String = ""
    , var name: String = ""
    , var description: String = ""
    , var surname: String = ""
    , var age: Int = 0
    , var birthday: String = ""
    , var gender: Gender = Gender.OTHER
    , var phoneNumber: String = ""
    , var locations: ArrayList<Location> = ArrayList()
    , var profileImageUrl: String = ""
    , var images: ArrayList<Int> = ArrayList()
    , var friends: ArrayList<User> = ArrayList()
    , var numLikes: Int = 0
    , var universityName: String = ""
    , var jobName: String = ""
    , var currentLocation: Location? = null
    , var id: String = ""
    , var messages: ArrayList<Message> = ArrayList()

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        TODO("gender"),
        parcel.readString(),
        TODO("locations"),
        parcel.readString(),
        TODO("images"),
        TODO("friends"),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Location::class.java.classLoader),
        parcel.readString(),
        TODO("messages")
    ) {
    }

    enum class Gender {
        MALE,
        FEMALE,
        OTHER
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(surname)
        parcel.writeInt(age)
        parcel.writeString(birthday)
        parcel.writeString(phoneNumber)
        parcel.writeString(profileImageUrl)
        parcel.writeInt(numLikes)
        parcel.writeString(universityName)
        parcel.writeString(jobName)
        parcel.writeParcelable(currentLocation, flags)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}