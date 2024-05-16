import json
import openai
import os

  
#use openai to fetch a list of recipes from given items
def maker(listr, foodCat, cuisine):
  API_KEY = open("OPENAI_API_KEY.txt").read()
  openai.api_key = API_KEY
  # listr = input("Provide the ingredients that you have: ")
  string = ', '.join(listr)
  response = openai.Completion.create(
    engine="text-davinci-003",
    prompt="Given a mainList of ingredients: " + string + ". The food category: " + foodCat + ". The cuisine: " + cuisine +
              """. Please provide me one possible recipe for food category from cuisine using mainList. 
              The very first line you should print should be the >Recipe Title<, so don't include ingredients.
              NOTE: You don't need to use all of the items in mainList, but the more items used, the better. 
              NOTE: List all ingredients use for the recipe in >Ingredients<. Do not change the name. Give measurements, use whole number. Use this format Name*measurements
              NOTE: Suggest substitution only for ingredients that are NOT from mainList but are used in the recipe, place those in >Substitutions<
              NOTE: Give me a list of possible dietary restriction for this recipe, place it in >Dietary Restriction<
              Use exactly this ordered format below for displaying the recipes, use g for grams, and number everything in >Instructions<, including the steps:
              Recipe Name:Ingredients:Instructions:Calories:Protein:Carbs:Fat:Substitutions:Dietary Restriction:
              """,
    max_tokens=3000
  )

  return response
  
  

