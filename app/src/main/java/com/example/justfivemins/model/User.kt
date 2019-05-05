package com.example.justfivemins.model

import com.example.justfivemins.R

class User(
    var email: String = "Jesus@bot.com"
    , var password: String = "erre"
    , var name: String = "Jesus"
    , var surname: String = "Gómez"
    , var secondSurname: String = "Cebollo"
    , var age: Int = 27
    , var gender: String = "Male"
    , var phoneNumber: String = "638787465"
    , var locations: ArrayList<Location> = ArrayList()
    , var profileImage: Int = R.drawable.img_profile_example
    , var headerImage: Int = R.drawable.header_background
    , var images: ArrayList<Int> = ArrayList()
    , var friends: ArrayList<User> = ArrayList()
    , var numLikes: Int = 0
    , var universityName: String = ""
    , var jobName: String = "",
    var currentLocation: Location = Location(),
    override var cardTitle: String = "",
    override var cardDescription: String = "",
    override var cardImage: Int = 3
) : CardItem