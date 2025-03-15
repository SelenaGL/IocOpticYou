package com.example.opticyou.communications

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

/**
 * Representation of the order to be sent to the server
 * @author professor
 */
//open class EndPointData {
//    private val data = ArrayList<String>()
//
//
//    /**
//     * Adds a data to the order at the end.
//     * @param data data to be added
//     */
//    fun addDataObject(data: Any?) {
//        val gson = Gson()
//        this.data.add(gson.toJson(data))
//    }
//
//    /**
//     * Adds a data to the order at the end.
//     * @param data data to be added
//     */
//    fun addPrimitiveData(data: Any) {
//        this.data.add(data.toString() + "")
//    }
//
//
//    /**
//     * Gets the data, at the specified position, as an object of the specified class.
//     * @param DataNumber data position (starting with 0)
//     * @param clasz data class
//     * @return parameter value
//     */
//    fun getData(DataNumber: Int, clasz: Class<*>?): Any? {
//        try {
//            val gson = Gson()
//            return gson.fromJson<Any>(data[DataNumber], clasz)
//        } catch (ex: JsonSyntaxException) {
//            return null
//        }
//    }
//}

