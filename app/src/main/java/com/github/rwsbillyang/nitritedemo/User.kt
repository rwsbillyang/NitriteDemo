package com.github.rwsbillyang.nitritedemo

import org.dizitart.no2.Document
import org.dizitart.no2.mapper.Mappable
import org.dizitart.no2.mapper.NitriteMapper
import org.dizitart.no2.objects.Id


/**
 * @author Anindya Chatterjee.
 */
class User : Mappable {
   @Id var id: String? = null
    var username: String? = null
    var email: String? = null

    // needed for deserialization
    constructor() {}

    constructor(username: String, email: String) {
        this.username = username
        this.email = email
        this.id = "" + System.currentTimeMillis()
    }

    override fun write(mapper: NitriteMapper): Document {
        val document = Document()
        document.put("id", id)
        document.put("username", username)
        document.put("email", email)
        return document
    }

    override fun read(mapper: NitriteMapper, document: Document) {
        id = document.get("id") as? String
        username = document.get("username")as? String
        email = document.get("email")as? String
    }
}