package com.example.sunsettyandroid.APIs

data class SunResponse(val results:SunResults)

data class SunResults(val sunrise:String, val sunset:String)

/*

struct sunResponse: Codable {
    let results:sunResults
    //let status:String
}

struct sunResults: Codable {
    let sunrise:String
    let sunset:String
}
 */