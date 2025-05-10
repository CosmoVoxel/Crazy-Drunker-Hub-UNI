package com.example.crazydrunker
import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
object HomeScreenData


@Serializable
data class CocktailMakeInfo(
    val text: String,
    val includeTimer: Boolean,
    val min: Int = 0,
    val sec: Int = 2,
)

val defaultIcon: ImageVector = Icons.Filled.Info

@Serializable
data class Ingredient(
    val name: String,
    @Contextual val img: ImageVector? = defaultIcon,
    val amount: Int = 1,
)

@Serializable
data class DetailsScreenData(
    val name: String,
    val img: Int,
    val timeToMake: Int = 5,
    val difficulty: Int = 5,
    val id: String = UUID.randomUUID().toString(),
)


@Serializable
data class TimerScreenData(
    var cocktailId: String,
    val isStarted: Boolean = false,
    var min: Int = 0,
    var sec: Int = 30,
    var stepIndex: Int = 0,
)

class ScreenSize private constructor() {
    private var windowSizeClass: WindowSizeClass? = null

    @Composable
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    fun Initialize(activity: Activity) {
        windowSizeClass = calculateWindowSizeClass(activity)
    }

    fun getDeviceSize(): WindowSizeClass {
        return windowSizeClass ?: throw IllegalStateException("ScreenSize not initialized. Call initialize() first.")
    }

    companion object {
        @Volatile
        private var instance: ScreenSize? = null

        fun getInstance(): ScreenSize {
            return instance ?: synchronized(this) {
                instance ?: ScreenSize().also { instance = it }
            }
        }
    }
}

object CocktailsData {
    private val cocktailKeys = mutableMapOf<String, String>()

    private val timerStore: MutableList<TimerScreenData> = mutableListOf()


    private val cocktails = listOf(
        createCocktail("mojito", "Mojito", 10, 3),
        createCocktail("margarita", "Margarita", 8, 2),
        createCocktail("oldFashioned", "Old Fashioned", 7, 4),
        createCocktail("cosmopolitan", "Cosmopolitan", 6, 3),
        createCocktail("longIsland", "Long Island Iced Tea", 10, 4),
        createCocktail("daiquiri", "Daiquiri", 5, 2)
    ).associateBy { it.id }

    val cocktailList: List<DetailsScreenData> = cocktails.values.toList()

    fun getCocktail(id: String): DetailsScreenData? = cocktails[id]
    fun getIngredients(id: String): List<Ingredient> = ingredients[cocktailKeys[id]].orEmpty()
    fun getCocktailSteps(id: String): List<CocktailMakeInfo> = steps[cocktailKeys[id]].orEmpty()

    private fun createCocktail(key: String, name: String, time: Int, difficulty: Int): DetailsScreenData {
        val cocktail = DetailsScreenData(
            id = UUID.randomUUID().toString(),
            name = name,
            img = R.drawable.juice1,
            timeToMake = time,
            difficulty = difficulty
        )
        cocktailKeys[cocktail.id] = key
        return cocktail
    }



    private val steps = mapOf(
        "mojito" to listOf(
            CocktailMakeInfo("Muddle mint leaves and sugar in a glass.", true, 0, 5),
            CocktailMakeInfo("Add lime juice and rum.", true, 0 , 5),
            CocktailMakeInfo("Fill glass with ice.", false),
            CocktailMakeInfo("Top up with soda water.", false),
            CocktailMakeInfo("Stir well and garnish with a mint sprig.", false),
        ),
        "margarita" to listOf(
            CocktailMakeInfo("Rub lime around the rim of a glass and dip in salt.", false),
            CocktailMakeInfo("Shake tequila, lime juice, and Cointreau with ice.", false),
            CocktailMakeInfo("Strain into the glass.", false),
            CocktailMakeInfo("Garnish with a lime wedge.", false),
        ),
        "oldFashioned" to listOf(
            CocktailMakeInfo("Muddle sugar with bitters in a glass.", false),
            CocktailMakeInfo("Add whiskey and ice.", false),
            CocktailMakeInfo("Stir well until chilled.", false),
            CocktailMakeInfo("Express orange peel over the drink and drop it in.", false)
        ),
        "cosmopolitan" to listOf(
            CocktailMakeInfo("Shake all ingredients with ice.", false),
            CocktailMakeInfo("Strain into a chilled martini glass.", false),
            CocktailMakeInfo("Garnish with a lime twist.", false)
        ),
        "longIsland" to listOf(
            CocktailMakeInfo("Fill a highball glass with ice.", false),
            CocktailMakeInfo("Add all liquors and lemon juice.", false),
            CocktailMakeInfo("Top with cola.", false),
            CocktailMakeInfo("Garnish with a lemon wedge.", false)
        ),
        "daiquiri" to listOf(
            CocktailMakeInfo("Shake all ingredients with ice.", false),
            CocktailMakeInfo("Strain into a chilled coupe glass.", false),
            CocktailMakeInfo("Garnish with a lime wedge.", false),
        )
    )
    private val ingredients = mapOf(
        "mojito" to listOf(
            Ingredient("White Rum", Icons.Filled.Person, 50),
            Ingredient("Lime", null, 1),
            Ingredient("Mint Leaves", null, 10),
            Ingredient("Sugar", null, 2),
            Ingredient("Soda Water", null, 100),
            Ingredient("Ice", null, 1),
        ),
        "margarita" to listOf(
            Ingredient("Tequila", Icons.Filled.Person, 45),
            Ingredient("Lime Juice", null, 30),
            Ingredient("Cointreau", Icons.Filled.Person, 15),
            Ingredient("Salt", null, 1)
        ),
        "oldFashioned" to listOf(
            Ingredient("Whiskey", Icons.Filled.Person, 50),
            Ingredient("Sugar Cube", null, 1),
            Ingredient("Angostura Bitters", null, 2),
            Ingredient("Orange Peel", null, 1),
            Ingredient("Ice", null, 1)
        ),
        "cosmopolitan" to listOf(
            Ingredient("Vodka", Icons.Filled.Person, 40),
            Ingredient("Cointreau", Icons.Filled.Person, 15),
            Ingredient("Lime Juice", null, 15),
            Ingredient("Cranberry Juice", null, 30),
        ),
        "longIsland" to listOf(
            Ingredient("Vodka", Icons.Filled.Person, 15),
            Ingredient("Gin", Icons.Filled.Person, 15),
            Ingredient("White Rum", Icons.Filled.Person, 15),
            Ingredient("Tequila", Icons.Filled.Person, 15),
            Ingredient("Triple Sec", Icons.Filled.Person, 15),
            Ingredient("Lemon Juice", null, 25),
            Ingredient("Simple Syrup", null, 30),
            Ingredient("Coca-Cola", null, 100),
        ),
        "daiquiri" to listOf(
            Ingredient("White Rum", Icons.Filled.Person, 60),
            Ingredient("Lime Juice", null, 30),
            Ingredient("Simple Syrup", null, 15),
        ),
    )

}
