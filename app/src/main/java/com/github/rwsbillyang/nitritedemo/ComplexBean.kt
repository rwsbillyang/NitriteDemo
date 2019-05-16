package com.github.rwsbillyang.nitritedemo

import android.util.Log
import org.dizitart.no2.Document
import org.dizitart.no2.mapper.Mappable
import org.dizitart.no2.mapper.NitriteMapper

data class Tuple(var name: String? = null,var age: Int? = null): Mappable
{
    override fun write(mapper: NitriteMapper?) = Document().apply {
        Log.d("Nitrite","Tuple.write: " + this@Tuple.toString())
        name?.let { put("name",it) }
        age?.let { put("name",it) }
    }

    override fun read(mapper: NitriteMapper?, document: Document?) {

        document?.run {
            name = get("name") as String?
            age = get("age") as Int?
        }
        Log.d("Nitrite","Tuple.read, get "+this.toString())
    }
}

data class Address(var street: String? = null, var zip: String? = null): Mappable{
    override fun write(mapper: NitriteMapper?) = Document().apply {
        street?.let { put("street",it) }
        zip?.let { put("zip",it) }
    }

    override fun read(mapper: NitriteMapper?, document: Document?) {

        document?.run {
            street = get("street") as String?
            zip = get("zip") as String?
        }
    }
}
/**
 * embedded document like mongoDB
 * */
data class ComplexBean(var myid: String, var level: Int? = null, var addr: Address? = null, var list: List<Tuple>? = null)
    : Mappable
{
    override fun write(mapper: NitriteMapper?) = Document().apply {
        Log.d("Nitrite","ComplexBean.write: "+ this@ComplexBean.toString())
        put("myid",myid)
        level?.let { put("level",it) }
        addr?.let { put("addr",it.write(mapper)) }
        list?.let {put("list",it)  }
    }

    override fun read(mapper: NitriteMapper?, document: Document?) {
        document?.run {
            myid = get("myid") as String
            addr= get("addr")?.run {  Address().apply { this.read(mapper,this@run as Document) }}
            level = get("level") as Int?
            list = get("list") as List<Tuple>?


        }
        Log.d("Nitrite","ComplexBean.read, get "+ this.toString())
    }
}