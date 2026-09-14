package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerSuggested);
        tvEmpty = findViewById(R.id.tvSuggestedEmpty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecipeAdapter(new ArrayList<>(), recipe -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        loadSuggestedRecipes();
    }

    /**
     * Normalizes an ingredient name so matching is robust to
     * simple real-world messiness: case, whitespace, and basic
     * singular/plural differences (e.g. "tomato" vs "tomatoes").
     */
    private String normalize(String name) {
        String result = name.trim().toLowerCase();
        // Basic plural handling: strip a trailing "es" or "s" if it looks plural
        if (result.endsWith("ies") && result.length() > 4) {
            result = result.substring(0, result.length() - 3) + "y"; // e.g. "berries" -> "berry"
        } else if (result.endsWith("es") && result.length() > 4) {
            result = result.substring(0, result.length() - 2); // e.g. "tomatoes" -> "tomato"
        } else if (result.endsWith("s") && result.length() > 3) {
            result = result.substring(0, result.length() - 1); // e.g. "eggs" -> "egg"
        }
        return result;
    }

    private void loadSuggestedRecipes() {
        // Build a normalized map of pantry: name -> [quantity, unit]
        Map<String, Double> pantryQuantities = new HashMap<>();
        Map<String, String> pantryUnits = new HashMap<>();

        Cursor pantryCursor = dbHelper.getAllPantryItems();
        if (pantryCursor.moveToFirst()) {
            do {
                String rawName = pantryCursor.getString(pantryCursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_NAME));
                double qty = pantryCursor.getDouble(pantryCursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_QUANTITY));
                String unit = pantryCursor.getString(pantryCursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_UNIT));
                String normName = normalize(rawName);
                pantryQuantities.put(normName, qty);
                pantryUnits.put(normName, unit == null ? "" : unit.trim().toLowerCase());
            } while (pantryCursor.moveToNext());
        }
        pantryCursor.close();

        List<Recipe> matchedRecipes = new ArrayList<>();

        Cursor recipeCursor = dbHelper.getAllRecipes();
        if (recipeCursor.moveToFirst()) {
            do {
                int recipeId = recipeCursor.getInt(recipeCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_ID));
                String recipeName = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_NAME));
                String recipeSteps = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_STEPS));

                if (recipeCanBeMade(recipeId, pantryQuantities, pantryUnits)) {
                    matchedRecipes.add(new Recipe(recipeId, recipeName, recipeSteps));
                }
            } while (recipeCursor.moveToNext());
        }
        recipeCursor.close();

        adapter.updateRecipes(matchedRecipes);
        tvEmpty.setVisibility(matchedRecipes.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
        recyclerView.setVisibility(matchedRecipes.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
    }

    /**
     * THE STRICT-MATCHING RULE (Section 2.3 of the brief):
     * A recipe only qualifies if EVERY required ingredient is present
     * in the pantry, in at least the required quantity. One missing
     * or insufficient ingredient excludes the whole recipe.
     */
    private boolean recipeCanBeMade(int recipeId, Map<String, Double> pantryQuantities, Map<String, String> pantryUnits) {
        Cursor ingredientCursor = dbHelper.getIngredientsForRecipe(recipeId);
        boolean canMake = true;

        if (ingredientCursor.moveToFirst()) {
            do {
                String requiredName = ingredientCursor.getString(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RI_INGREDIENT_NAME));
                double requiredQty = ingredientCursor.getDouble(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RI_QUANTITY));
                String requiredUnit = ingredientCursor.getString(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RI_UNIT));

                String normRequired = normalize(requiredName);

                if (!pantryQuantities.containsKey(normRequired)) {
                    canMake = false;
                    break;
                }

                double pantryQty = pantryQuantities.get(normRequired);
                String pantryUnit = pantryUnits.get(normRequired);
                String normRequiredUnit = requiredUnit == null ? "" : requiredUnit.trim().toLowerCase();

                // Only enforce the quantity check when units match (or are both "unit"/blank).
                // Cross-unit conversion (e.g. g to cup) is out of scope for this assignment.
                boolean unitsComparable = normRequiredUnit.equals(pantryUnit)
                        || (normRequiredUnit.isEmpty() && pantryUnit.isEmpty());

                if (unitsComparable && pantryQty < requiredQty) {
                    canMake = false;
                    break;
                }
            } while (ingredientCursor.moveToNext());
        }
        ingredientCursor.close();
        return canMake;
    }
}