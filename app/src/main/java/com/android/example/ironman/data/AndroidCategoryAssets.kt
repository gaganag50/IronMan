package com.android.example.ironman.data

import com.android.example.ironman.R

class AndroidCategoryAssets {
    companion object {


        private val categories = object : ArrayList<Int>() {
            init {


                add(R.string.cat_shopping)      // shopping
                add(R.string.cat_entertainment)     //entertainment
                add(R.string.cat_food)      // food
                add(R.string.cat_health)        // health
                add(R.string.cat_household)
                add(R.string.cat_education)
                add(R.string.cat_transportation)        // travel
                add(R.string.cat_others)


            }


        }
        val food = object : ArrayList<Int>() {
            init {
                add(R.string.foodSubGroceries)
                add(R.string.foodSubOther)
                add(R.string.foodSubRestaurant)
                add(R.string.foodSubCafe)
                add(R.string.foodSubSnack)

            }
        }
        val healthCare = object : ArrayList<Int>() {
            init {
                add(R.string.healthCareCatDental)
                add(R.string.healthCareCatEyeCare)
                add(R.string.healthCareCatHealthInsurance)
                add(R.string.healthCareCatMedical)
                add(R.string.healthCareCatNutrition)
                add(R.string.healthCareCatOther)
                add(R.string.healthCareCatPrescription)
                add(R.string.healthCareCatPharmacy)


            }
        }
        val automobile = object : ArrayList<Int>() {
            init {
                add(R.string.automobileSubRoadServices)
                add(R.string.automobileSubFuel)
                add(R.string.automobileSubInsurance)
                add(R.string.automobileSubLease)
                add(R.string.automobileSubMaintenance)
                add(R.string.automobileSubMileage)
                add(R.string.automobileRegistration)
                add(R.string.automobileSubOther)


            }
        }
        val entertainment = object : ArrayList<Int>() {
            init {
                add(R.string.entertainmentCatConcert)
                add(R.string.entertainmentCatMovies)
                add(R.string.entertainmentCatOther)
                add(R.string.entertainmentCatParty)
                add(R.string.entertainmentCatSports)


            }
        }
        val family = object : ArrayList<Int>() {
            init {
                add(R.string.familyCatChildCare)
                add(R.string.familyCatChildEducation)
                add(R.string.familyCatOther)
                add(R.string.familyCatToy)
                add(R.string.familyCatPets)
                add(R.string.familyCatPresents)
                add(R.string.familyCatFriendsLovers)


            }
        }
        val travel = object : ArrayList<Int>() {
            init {
                add(R.string.travelCatAirplane)
                add(R.string.travelCatCarRental)
                add(R.string.travelCatFood)
                add(R.string.travelCatHotel)
                add(R.string.travelCatMisc)
                add(R.string.travelCatOther)
                add(R.string.travelCatParkingFees)
                add(R.string.travelCatPetrol)

                add(R.string.travelCatTaxi)


            }
        }
        val shopping = object : ArrayList<Int>() {
            init {
                add(R.string.shoppingCatClothing)
                add(R.string.shoppingCatCarFootwear)
                add(R.string.shoppingCatElectronics)
                add(R.string.shoppingCatOther)
                add(R.string.shoppingCatAccessories)


            }
        }
        val education = object : ArrayList<Int>() {
            init {
                add(R.string.shoppingCatBooks)
                add(R.string.shoppingCatPrintPhotocopy)


            }
        }

        fun getCategories(): List<Int> {

            return categories
        }

        fun getfood(): List<Int> {

            return food
        }

        fun gethealthCare(): List<Int> {

            return healthCare
        }

        fun getautomobile(): List<Int> {

            return automobile
        }

        fun getentertainment(): List<Int> {

            return entertainment
        }

        fun getfamily(): List<Int> {

            return family
        }

        fun gettravel(): List<Int> {

            return travel
        }

        fun getshopping(): List<Int> {

            return shopping
        }

        fun geteducation(): List<Int> {

            return education
        }


    }


}