package es.officepoint.appofthrones


import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

const val URL_CHARATERS = "http://5aeeea1d5139c80014f228b3.mockapi.io/characters"

object CharacterRepo {
/*    val characters: MutableList<Character> = mutableListOf()
    get() {
        if (field.isEmpty())
            field.addAll(dummyCharacters())
        return field
    }*/

    private var characters : MutableList<Character> = mutableListOf()

/*    private fun dummyCharacters(): MutableList<Character>{
        return (1..10).map {
                    intToCharacter(it)
                }.toMutableList()
    }*/

    fun requestCharacters(context: Context,
                          success : ((MutableList<Character>) -> Unit),
                          error: (() -> Unit)){
        if (characters.isEmpty()) {
            val request = JsonArrayRequest(Request.Method.GET, URL_CHARATERS, null,
                    { response ->
                        characters = parseCharacters(response)
                        success.invoke(characters)
                    },
                    { volleyError ->
                            error.invoke()

                    })
            Volley.newRequestQueue(context)
                    .add(request)
        }
        else{
            success.invoke(characters)
        }
    }

    private fun parseCharacters(jsonArray: JSONArray): MutableList<Character> {
        val characters = mutableListOf<Character>()

        for (index in 0..(jsonArray.length() - 1)){
            val character = parseCharacter(jsonArray.getJSONObject(index))
            characters.add(character)
        }
        return characters
    }

    private fun parseCharacter (jsonCharacter: JSONObject) : Character {
        return Character(
                jsonCharacter.getString("id"),
                jsonCharacter.getString("title"),
                jsonCharacter.getString("name"),
                jsonCharacter.getString("born"),
                jsonCharacter.getString("actor"),
                jsonCharacter.getString("quote"),
                jsonCharacter.getString("father"),
                jsonCharacter.getString("mother"),
                jsonCharacter.getString("spouse"),
                jsonCharacter.getString("img"),
                parseHouse(jsonCharacter.getJSONObject("house"))
                )
    }

    private fun parseHouse(jsonHouse: JSONObject) : House {
        return House(
                jsonHouse.getString("name"),
                jsonHouse.getString("region"),
                jsonHouse.getString("words"),
                jsonHouse.getString("img")
        )
    }

    fun findCharacterById(id: String) : Character? {
        return characters.find {  character ->
            character.id == id
        }
    }

/*    private  fun intToCharacter(int: Int) : Character {
        return Character(
                    name = "Personaje ${int}",
                    title = "Titulo ${int}",
                    born = "Nació en ${int} ",
                    actor = "Actor ${int}",
                    quote = "quote ${int}",
                    father = "Padre ${int}",
                    mother = "Madre ${int}",
                    spouse = "Esposa ${int}",
                    house = dummyHouse()

            )
        //return  character
    }

    private fun dummyHouse() : House {
        val ids = arrayOf("stark", "lannister", "tyrell", "arryn", "tully")
        val randomIdPosition = Random().nextInt(ids.size)
        return House(
                name = ids[randomIdPosition],
                region = "Region",
                words =  "Lema"
        )
    }*/
}