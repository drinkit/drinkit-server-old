package guru.drinkit.service

/**
 * @author pkolmykov
 */
interface StatsService{
    fun addViewToRecipe(recipeId: Int, userId: String)
}


//@Autowired
//private val mongoOperations: MongoOperations? = null
//@Autowired
//private val recipeRepository: RecipeRepository? = null
//
//@After(value = "@annotation(EnableStats) && args(id)")
//fun writeStats(id: Int) {
//    recipeRepository!!.incrementViews(id)
//    val userName = DrinkitUtils.getUserName()
//    if (userName != null) {
//        mongoOperations!!.upsert(query(
//                where("recipeId").`is`(id).and("userId").`is`(userName)),
//                Update().inc("views", 1).set("lastViewed", Date()), UserRecipeStats::class.java)
//    }
//
//    mongoOperations!!.updateFirst(
//            query(where("id").`is`(id)), Update().inc("stats.views", 1), Recipe::class.java)
//}