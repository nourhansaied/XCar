package com.victoria.driver.data.pojo

data class Car(val a: String, val b: String) {
    fun function(c:String): Car = Car(a, c)
}