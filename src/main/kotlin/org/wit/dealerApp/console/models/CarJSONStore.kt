package org.wit.dealerApp.console.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging

import org.wit.dealerApp.console.helpers.*
import java.util.*

private val logger = KotlinLogging.logger {}

val JSON_FILE = "cars.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<CarModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class CarJSONStore : CarStore {

    var cars = mutableListOf<CarModel>()

    init {
        if (exists(JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<CarModel> {
        return cars
    }

    override fun findOne(id: Long) : CarModel? {
        var foundCar: CarModel? = cars.find { p -> p.id == id }
        return foundCar
    }

    override fun create(car: CarModel) {
        car.id = generateRandomId()
        cars.add(car)
        serialize()
    }

    override fun update(car: CarModel) {
        var foundCar = findOne(car.id!!)
        if (foundCar != null) {
            foundCar.make = car.make
            foundCar.model = car.model
            foundCar.year = car.year
        }
        serialize()
    }

    override fun delete(car: CarModel) {
        cars.remove(car)
        serialize()
    }

    internal fun logAll() {
        cars.forEach { logger.info("${it}") }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(cars, listType)
        write(JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(JSON_FILE)
        cars = Gson().fromJson(jsonString, listType)
    }
}