package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        dbHelper = new DatabaseHelper(this);

        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvIngredients = findViewById(R.id.tvDetailIngredients);
        TextView tvSteps = findViewById(R.id.tvDetailSteps);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        // Load recipe name and steps
        Cursor recipeCursor = dbHelper.getRecipeById(recipeId);
        if (recipeCursor.moveToFirst()) {
            String name = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_NAME));
            String steps = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_STEPS));
            tvName.setText(name);
            tvSteps.setText(steps);
        }
        recipeCursor.close();

        // Load and display the ingredient list
        StringBuilder ingredientsText = new StringBuilder();
        Cursor ingredientCursor = dbHelper.getIngredientsForRecipe(recipeId);
        if (ingredientCursor.moveToFirst()) {
            do {
                String ingName = ingredientCursor.getString(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RI_INGREDIENT_NAME));
                double qty = ingredientCursor.getDouble(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RI_QUANTITY));
                String unit = ingredientCursor.getString(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RI_UNIT));

                ingredientsText.append("• ").append(qty).append(" ").append(unit)
                        .append(" ").append(ingName).append("\n");
            } while (ingredientCursor.moveToNext());
        }
        ingredientCursor.close();

        tvIngredients.setText(ingredientsText.toString().trim());
    }
}