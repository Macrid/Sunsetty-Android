package com.example.sunsettyandroid.APIs

data class CityResponse(val data:List<CityData>)

data class CityData(val id:Int,
                    val city:String,
                    val name:String,
                    val country:String,
                    val latitude:Float,
                    val longitude:Float)

/*
struct CityResponse: Codable {
    let data:[CityData]
    //let links:[CityLinks]
    //let metadata:CityMetadata

}

struct CityData: Codable{
    let id:Int
            //let wikiDataId:String
            //let type:String
            let city:String
            let name:String
            let country:String
            //let countryCode:String
            //let region:String
            //let regionCode:String
            let latitude:Float
            let longitude:Float
}

struct CityLinks: Codable{
    let rel:String
            let href:String
}

struct CityMetadata: Codable{
    let currentOffset:Int
            let totalCount:Int
}

 */