import uuid
from flask import Blueprint, request, jsonify, render_template, request, redirect, url_for
from flask_cors import CORS, cross_origin
import json
import time
import threading
import re


from dotenv import load_dotenv
from recipe import maker
from firebase_admin import firestore

load_dotenv()

db = firestore.client()
ingredient_Ref = db.collection('ingredient')
recipeSaved_Ref = db.collection('recipeSaved')
recipeHistory_Ref = db.collection('recipeHistory')
update = db.transaction()


userAPI = Blueprint('userAPI',__name__)
# this is where the server code will go and connect to the db 
# /add is for postman http request, the command for postman so to say

#add ingredient 
#working
#@userAPI.route('/addIngredient',methods=['POST'])
#def create(name):
#    try:
#        ingredient_Ref.document(name).set(request.json)
#        return jsonify({"Created Success": True}), 200
#    except Exception as e:
#        return f"ERROR CREATING {e}"
    
#add one or multiple ingredient, using array of JSON
#will also update it if the item already exist
#working

'''
@userAPI.route('/addIngredients',methods=['POST'])
def createMultiple():
    try:
        data = request.json
        if not isinstance(data, list):
            return jsonify({"error": "Request data must be a JSON array"}), 400

        for ingredient in data:
            # Assuming each ingredient in the array has a unique name
            name = ingredient.get("food_name")
            if name:
                ingredient_Ref.document(name).set(ingredient)
            else:
                return jsonify({"error": "Each ingredient must have a 'food_name' field"}), 400
            
        return jsonify({"Created Success": True}), 200
    except Exception as e:
        return jsonify({"error": f"Error creating ingredients: {e}"}), 500
'''
'''
#NEW ADD
@userAPI.route('/addIngredients', methods=['POST'])
def createMultiple():
    try:
        data = request.json
        if not isinstance(data, list):
            return jsonify({"error": "Request data must be a JSON array"}), 400

        for ingredient in data:
            # Assuming each ingredient in the array has a unique name
            name = ingredient.get("food_name")
            if name:
                # Check if the ingredient with the same name already exists
                existing_doc = ingredient_Ref.document(name).get()
                
                if existing_doc.exists:
                    # Update the quantity of the existing ingredient
                    existing_quantity = existing_doc.to_dict().get("quantity", 0)
                    new_quantity = existing_quantity + ingredient.get("quantity", 0)
                    ingredient_Ref.document(name).update({"quantity": new_quantity})
                else:
                    # Create a new ingredient
                    ingredient_Ref.document(name).set(ingredient)
            else:
                return jsonify({"error": "Each ingredient must have a 'food_name' field"}), 400
        return jsonify({"Created Success": True}), 200
    except Exception as e:
        return jsonify({"error": f"Error creating/updating ingredients: {e}"}), 500
'''


def parse_openai_response(text_response):
    text_response = text_response.replace('\n', ' ')
    
    # split is a list of string
    split = text_response.split(':')
    #print ("SPLIT1 RESPONSE" + split+ '\n')
    
    recipe_json = {
        "Recipe Name": "",
        "Ingredients": "",
        "Instructions": "",
        "Calories": "",
        "Protein": "",
        "Carbs": "",
        "Fat": "",
        "Substitutions":"",
        "Dietary Restriction": ""
    }
    
    
    namesplit = split[1]
    namesplit = namesplit.replace("Ingredients", "")
    
    ingredientsplit =  split[2]
    ingredientsplit = ingredientsplit.replace("Instructions", "")
    
    instructionsplit = split[3]
    instructionsplit = instructionsplit.replace("Calories", "")
    
    caloriesplit = split[4]
    caloriesplit = caloriesplit.replace("Protein", "")
    
    proteinsplit = split[5]
    proteinsplit = proteinsplit.replace("Carbs", "")
    
    carbsplit = split[6]
    carbsplit = carbsplit.replace("Fat", "")
    
    fatsplit = split[7]
    fatsplit = fatsplit.replace("Substitutions", "")
    
    substitutesplit = split[8]
    substitutesplit = substitutesplit.replace("Dietary Restriction", "")
    
    dietsplit = "base"
    
    if len(split) > 9:
        dietsplit = split[9].strip()
        recipe_json["Dietary Restriction"] = dietsplit
    else:
        recipe_json["Dietary Restriction"] = "None"
        
    
    recipe_json["Recipe Name"] = namesplit.strip()
    recipe_json["Ingredients"] = ingredientsplit.lower().strip()
    recipe_json["Instructions"] = instructionsplit.strip()
    recipe_json["Calories"] = caloriesplit.strip()
    recipe_json["Protein"] = proteinsplit.strip()
    recipe_json["Carbs"] = carbsplit.strip()
    recipe_json["Fat"] = fatsplit.strip()
    recipe_json["Substitutions"] = substitutesplit.strip()
    recipe_json["Dietary Restriction"] = dietsplit.strip()
    
    '''
    print ("NAME RESPONSE" + namesplit + '\n')
    print ("INGRE RESPONSE" + ingredientsplit+ '\n')
    print ("INSTRUCT RESPONSE" + instructionsplit+ '\n')
    print ("CALORIES RESPONSE" + caloriesplit+ '\n')
    print ("PROTEIN RESPONSE" + proteinsplit+ '\n')
    print ("CARB RESPONSE" + carbsplit+ '\n')
    print ("FAT RESPONSE" + fatsplit+ '\n')
    print ("SUBSTI RESPONSE" + substitutesplit+ '\n')
    print ("DIET RESPONSE" + dietsplit+ '\n')
    '''
    return recipe_json


@userAPI.route('/addIngredients', methods=['POST'])
def createSingle():
    try:
        data = request.json
        # Check if the request data is a JSON object
        if not isinstance(data, dict):
            return jsonify({"error": "Request data must be a JSON object"}), 400
        # Assuming each ingredient has a unique name
        name = data.get("Item")
        if name:
            # Check if the ingredient with the same name already exists
            existing_doc = ingredient_Ref.document(name).get()
            if existing_doc.exists:
                # Update the quantity of the existing ingredient
                existing_quantity = existing_doc.to_dict().get("quantity", 0)
                new_quantity = int(existing_quantity) + int(data.get("quantity", 0))
                ingredient_Ref.document(name).update({"quantity": str(new_quantity)})
                # If count is 0 or less, delete the ingredient
                if new_quantity <= 0:
                    ingredient_Ref.document(name).delete() 
            else:
                # Create a new ingredient
                ingredient_Ref.document(name).set(data)
        else:
            return jsonify({"error": "The ingredient must have a 'Item' field"}), 400

        return jsonify({"Created Success": True}), 200
    except Exception as e:
        return jsonify({"error": f"Error creating/updating ingredient: {e}"}), 500


#WORKING
#DELETE A LIST OF INGREDIENT
@userAPI.route('/deleteIngredients', methods=['POST'])
def updateIngredients():
    try:
        data = request.json
        # Check if the request data is a JSON object
        if not isinstance(data, dict):
            return jsonify({"error": "Request data must be a JSON object"}), 400
        
        ingredients = data.get("ingredients", [])

        if not ingredients:
            return jsonify({"error": "No ingredients provided to update"}), 400

        for ingredient in ingredients:
            name = ingredient.get("Item")
            if name:
                # Check if the ingredient with the same name already exists
                existing_doc = ingredient_Ref.document(name).get()
                if existing_doc.exists:
                     # Update the quantity of the existing ingredient
                    existing_quantity = existing_doc.to_dict().get("quantity", 0)
                    new_quantity = int(existing_quantity) + int(ingredient.get("quantity", 0))
                    
                    ingredient_Ref.document(name).update({"quantity": str(new_quantity)})
                    # If count is 0 or less, delete the ingredient
                    if new_quantity <= 0:
                        ingredient_Ref.document(name).delete() 
            else:
                return jsonify({"error": "Each ingredient must have an 'Item' field"}), 400

        return jsonify({"Updated Success": True}), 200
    except Exception as e:
        return jsonify({"error": f"Error updating ingredients: {e}"}), 500

              


#return the full list of ingredient
#working
@userAPI.route('listIngredients',methods=['GET'])
def read():
    try:
        all_ingredient = [doc.to_dict() for doc in ingredient_Ref.stream()]
        return jsonify(all_ingredient), 200
    except Exception as e:
        return f"An Error Occured: {e}"


#return a specific ingredient
#working
@userAPI.route('/listIngredient/<id>', methods=['GET'])
def read_one(id):
    try:
        doc = ingredient_Ref.document(id).get()
        if doc.exists:
            return jsonify(doc.to_dict()), 200
        else:
            return jsonify({"error": f"Ingredient with id {id} not found"}), 404
    except Exception as e:
        return jsonify({"error": f"An error occurred: {e}"}), 500


#delete ingredient by id
#working
@userAPI.route('/deleteIngredient/<id>',methods=['DELETE'])
def delete(id):
    try:
        ingredient_Ref.document(id).delete()
        return jsonify({"Delete Success": True}), 200
    except Exception as e:
        return f"ERROR CREATING {e}"
    
    
#update the ingredient by amount using id, and delete the ingredient if count hit 0
#working
@userAPI.route('/updateIngredient/<id>', methods=['PUT'])
def updateIngredient(id):
    try:
        # update the ingredient count
        doc_ref = ingredient_Ref.document(id)
        doc = doc_ref.get()

        if doc.exists:
            change = request.json.get("change", 0)
            # Update the count based on the change
            current_quantity = doc.to_dict().get("quantity", 0)
            new_quantity =  current_quantity + change
            doc_ref.update({"quantity": new_quantity})

            # If count is 0 or less, delete the ingredient
            if new_quantity <= 0:
                doc_ref.delete()    

            return jsonify({"Updated Success": True}), 200
        else:
            return jsonify({"error": f"Ingredient with id {id} not found"}), 404
    except Exception as e:
        return jsonify({"error": f"Error updating/deleting ingredient: {e}"}), 500
    


#generate a recipe for user
#working
@userAPI.route('/recipe', methods=['POST'])
def get_recipe():
    try:
        data = request.json
        if not isinstance(data, dict):
            return jsonify({"error": "Request data must be a JSON object"}), 400
        
        foodType = str(data.get("FoodCategory"))
        cuisine = str(data.get("Cuisine"))
        
        print(foodType + cuisine)
        
        # Get all documents from the 'ingredient' collection
        query_result = ingredient_Ref.get()
        
        # Extract product names from the documents
        product_names = [entry.to_dict()['Item'] for entry in query_result]
        # Your 'maker' function should process the product_names and return a recipe
        recipe = maker(product_names,foodType,cuisine)
        #parsedRecipe = parse_recipe_string(recipe)
        #print(recipe)
        #return jsonify(recipe), 200
        return recipe, 200
    except Exception as e:
        print("Error:", str(e))
        import traceback
        traceback.print_exc()
        return jsonify({"errorRecipe": f"Error getting recipe: {e}"}), 500
    
    
#save the recipe, also parse the string 
#working
@userAPI.route('/saveRecipe', methods=['POST'])
def saveRecipe():
    try:
        #data is a dict/list (Python Object)
        data = request.json
        # Check if the request data is a JSON object
        if not isinstance(data, dict):
            return jsonify({"error": "Request data must be a JSON object"}), 400

        # Assuming each ingredient has a unique name
        # stringRecipeInfo is a string 
        stringRecipeInfo = data.get("recipeInfo")
        
        
        '''THIS IS WHERE WE NEED TO PARSE THE NAME'''
        recipeDict = parse_openai_response(stringRecipeInfo)
        #NEW FUNCTION HERE Parse the stringRecipeInfo into a new organized dict
        #Extract the Recipe Name = name
        name = recipeDict["Recipe Name"]
        
        if name:
            # Check if the ingredient with the same name already exists
            existing_doc = recipeSaved_Ref.document(name).get()

            if existing_doc.exists:
                return jsonify({"EXCEPTION": "The recipe is already saved"}), 300
            else:
                # Create a recipe
                recipeSaved_Ref.document(name).set(recipeDict)
        else:
            return jsonify({"error": "error saving recipe"}), 400

        return jsonify({"Save Success": True}), 200
    except Exception as e:
        print("Error:", str(e))
        import traceback
        traceback.print_exc()
        return jsonify({"error": f"Error except saving recipe: {e}"}), 500
    
    
    
#WORKING, REMEMBER THE SPACE IN THE NAME
@userAPI.route('/deleteRecipe', methods=['POST'])
def deleteRecipe():
    try:
        data = request.json
        # Check if the request data is a JSON object
        if not isinstance(data, dict):
            return jsonify({"error": "Request data must be a JSON object"}), 400
        stringRecipeName = data.get("recipeName")
        print(stringRecipeName)
        if stringRecipeName:
            # Check if the recipe with the same name already exists
            existing_doc = recipeSaved_Ref.document(stringRecipeName).get()
            if existing_doc.exists:
                recipeSaved_Ref.document(stringRecipeName).delete()
                return jsonify({"Delete Success": True}), 200
            else:
                return jsonify({"error": "The recipe does not exist"}), 404
        else:
            return jsonify({"error": "Recipe name not provided"}), 400
    except Exception as e:
        print("Error:", str(e))
        import traceback
        traceback.print_exc()
        return jsonify({"error": f"Error deleting recipe: {e}"}), 500


#return the full list of recipe
#working
@userAPI.route('listRecipes',methods=['GET'])
def listRecipe():
    try:
        all_recipes = [doc.to_dict() for doc in recipeSaved_Ref.stream()]
        return jsonify(all_recipes), 200
    except Exception as e:
        return f"An Error Occured: {e}"



# takes list of items and returns recipes
# not working
#@userAPI.route('/recipe', methods=['GET'])
#def get_recipe():
#    try:
#        data = json.load(ingredient_Ref)
#        product_names = [entry['name'] for entry in data['data']]
#        recipe = maker(product_names)
#        print(recipe)
#        return json.dumps(recipe)
#    except Exception as e:
#            return f"ERROR GETTING RECIPE {e}"



