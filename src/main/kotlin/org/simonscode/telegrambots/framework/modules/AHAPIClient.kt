package org.simonscode.telegrambots.framework.modules

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.annotations.Expose
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder

class AHAPIClient {
    private val baseURL = "https://www.ah.nl/service/rest/delegate?url="
    private val cathegories = mutableSetOf<String>()
    private val productIds = mutableSetOf<String>()
    @Expose
    private val products = mutableListOf<Product>()

    companion object {
        @JvmStatic
        private val file = "products.json"
        @JvmStatic
        private var client: AHAPIClient? = null

        fun getInstance(): AHAPIClient {
            val store = File(file)
            client = if (store.exists() && store.canRead() && store.canWrite()) {
                val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create()
                gson.fromJson(store.readText(), AHAPIClient::class.java)
            } else {
                AHAPIClient()
            }
            return client!!
        }

        fun save() {
            if (client != null) {
                val store = File(file)
                if (store.exists())
                    store.delete()
                store.createNewFile()
                val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create()
                store.writeText(gson.toJson(client))
            }
        }
    }

    class Product(
            @Expose val name: String,
            @Expose val brand: String?,
            @Expose val cathegory: String?,
            @Expose val unitSize: String?,
            @Expose val link: String?
    ) {
        fun name(): String {
            return name.replace("\u00AD", "")
        }
    }

    fun get(): List<Product> {
        if (products.isEmpty()) {
            parseLink("/producten")
            save()
        }
        return products
    }

    fun parseLink(s: String) {
        val link = baseURL + URLEncoder.encode(s, "UTF-8")

        println("Requesting: $link")

        val url = URL(link)

        val con = url.openConnection()
        con.doOutput = true
        con.connect()

        val parser = JsonParser()
        val obj = parser.parse(BufferedReader(InputStreamReader(con.getInputStream())))
        val x = obj.asJsonObject.get("_embedded").asJsonObject.get("lanes").asJsonArray

        x.map { it.asJsonObject }
                .filter {
                    it.get("type")?.let {
                        it.asString == "ProductCategoryNavigationLane"
                    } ?: false
                }.forEach {
            it["_embedded"].asJsonObject["items"].asJsonArray.forEach {
                parseCathegory(it.asJsonObject)
            }
        }

        x.map { it.asJsonObject }
                .filter {
                    it.get("type")?.let {
                        it.asString == "ProductLane"
                    } ?: false
                }.forEach {
            it["_embedded"].asJsonObject["items"].asJsonArray.forEach {
                parseProduct(it.asJsonObject)
            }
        }
    }

    private fun parseProduct(o: JsonObject) {
        if (o["type"].asString != "Product")
            return
        if (!productIds.add(o["foldOutChannelId"].asString))
            return
        productIds.add(o["foldOutChannelId"].asString)
        val p = o.get("_embedded").asJsonObject.get("product").asJsonObject
        products.add(Product(p["description"].asString, p["brandName"]?.asString, p["categoryName"]?.asString, p["unitSize"]?.asString, o["navItem"]?.asJsonObject?.get("link")?.asJsonObject?.get("href")?.asString))
    }

    private fun parseCathegory(o: JsonObject) {
        if (o["type"].asString != "ProductCategory")
            return
        if (!cathegories.add(o["id"].asString))
            return
        parseLink(o["navItem"].asJsonObject["link"].asJsonObject["href"].asString)
    }
}