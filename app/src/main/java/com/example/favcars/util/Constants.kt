package com.example.favdish.util

object Constants {
    const val CAR_TYPE = "CarType"
    const val CAR_ENGINE_POWER= "CarEnginePower"
    const val CAR_PRICE = "CarPrice"

    const val CAR_IMAGE_SOURCE_LOCAL: String = "Local"
    const val CAR_IMAGE_SOURCE_ONLINE: String = "Online"

    /**
     * return the Car Type list items.
     */
    fun carTypes(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("sedan")
        list.add("jeep")
        list.add("coupe")
        list.add("sports car")
        list.add("station wagon")
        list.add("hatchback")
        list.add("convertible")
        list.add("sport-utility vehicle(SUV)")
        list.add("other")
        return list
    }

    /**
     * return the Car Engine Power list items.
     */
    fun carEnginePowerList(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("I-4")
        list.add("I-6")
        list.add("V-6")
        list.add("V-8")
        list.add("Other")
        return list
    }


    /**
     * return the Dish Cooking Time list items. The time added is in Minutes.
     */
    fun carPriceRange(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("10,000 - 20,000$")
        list.add("20,000 - 40,000$")
        list.add("40,000 - 60,000$")
        list.add("60,000 - 100,000$")
        list.add("over 100,000$$")
        return list
    }
}