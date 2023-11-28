package ru.reaperoq.test_task.datasource.models.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.reaperoq.test_task.datasource.models.Payment
import java.lang.reflect.Type

class PaymentDeserializer : JsonDeserializer<Payment> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Payment {
        val jsonObject = json.asJsonObject
        val amount = if (jsonObject["amount"]?.asString.isNullOrEmpty()) {
            null
        } else {
            jsonObject["amount"]?.asDouble
        }
        return Payment(
            id = jsonObject["id"].asInt,
            title = jsonObject["title"].asString,
            amount = amount,
            created = jsonObject["created"]?.asLong
        )
    }
}