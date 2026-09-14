package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry table
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QUANTITY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    // Recipes table
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_STEPS = "steps";

    // Recipe ingredients table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RI_ID = "id";
    public static final String COL_RI_RECIPE_ID = "recipe_id";
    public static final String COL_RI_INGREDIENT_NAME = "ingredient_name";
    public static final String COL_RI_QUANTITY = "quantity";
    public static final String COL_RI_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PANTRY_NAME + " TEXT NOT NULL, " +
                COL_PANTRY_QUANTITY + " REAL NOT NULL, " +
                COL_PANTRY_UNIT + " TEXT, " +
                COL_PANTRY_EXPIRY + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_RECIPE_STEPS + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_RI_INGREDIENT_NAME + " TEXT NOT NULL, " +
                COL_RI_QUANTITY + " REAL NOT NULL, " +
                COL_RI_UNIT + " TEXT, " +
                "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_RECIPE_ID + "))");

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Tomato Pasta", "1. Boil pasta until al dente.\n2. Sauté garlic in olive oil.\n3. Add tomatoes and simmer 10 min.\n4. Toss pasta with sauce and serve.",
                new String[][]{{"pasta", "200", "g"}, {"tomato", "3", "unit"}, {"garlic", "2", "clove"}, {"olive oil", "2", "tbsp"}});

        insertRecipe(db, "Scrambled Eggs", "1. Crack eggs into a bowl and whisk.\n2. Melt butter in a pan.\n3. Pour eggs in and stir gently until set.\n4. Season with salt.",
                new String[][]{{"egg", "3", "unit"}, {"butter", "1", "tbsp"}, {"salt", "1", "pinch"}});

        insertRecipe(db, "Grilled Cheese Sandwich", "1. Butter two slices of bread.\n2. Place cheese between them.\n3. Grill in a pan until golden on both sides.",
                new String[][]{{"bread", "2", "slice"}, {"cheese", "2", "slice"}, {"butter", "1", "tbsp"}});

        insertRecipe(db, "Vegetable Stir Fry", "1. Chop all vegetables.\n2. Heat oil in a wok.\n3. Stir fry vegetables 5-7 min.\n4. Add soy sauce and serve.",
                new String[][]{{"carrot", "1", "unit"}, {"broccoli", "1", "cup"}, {"bell pepper", "1", "unit"}, {"soy sauce", "2", "tbsp"}, {"oil", "1", "tbsp"}});

        insertRecipe(db, "Chicken Rice Bowl", "1. Cook rice according to package.\n2. Season and cook chicken until done.\n3. Slice chicken and serve over rice.",
                new String[][]{{"chicken", "200", "g"}, {"rice", "1", "cup"}, {"salt", "1", "pinch"}});

        insertRecipe(db, "Banana Pancakes", "1. Mash banana in a bowl.\n2. Mix in flour, egg, and milk.\n3. Cook spoonfuls on a hot pan until golden.",
                new String[][]{{"banana", "1", "unit"}, {"flour", "1", "cup"}, {"egg", "1", "unit"}, {"milk", "0.5", "cup"}});

        insertRecipe(db, "Tuna Sandwich", "1. Mix tuna with mayonnaise.\n2. Spread on bread.\n3. Add lettuce and top with second slice.",
                new String[][]{{"tuna", "1", "can"}, {"mayonnaise", "2", "tbsp"}, {"bread", "2", "slice"}, {"lettuce", "1", "leaf"}});

        insertRecipe(db, "Fried Rice", "1. Heat oil in a pan.\n2. Add cooked rice, egg, and vegetables.\n3. Stir fry with soy sauce until combined.",
                new String[][]{{"rice", "2", "cup"}, {"egg", "1", "unit"}, {"carrot", "1", "unit"}, {"soy sauce", "2", "tbsp"}, {"oil", "1", "tbsp"}});

        insertRecipe(db, "Cheese Omelette", "1. Whisk eggs with salt.\n2. Pour into a heated, greased pan.\n3. Add cheese, fold, and cook until set.",
                new String[][]{{"egg", "2", "unit"}, {"cheese", "1", "slice"}, {"butter", "1", "tbsp"}, {"salt", "1", "pinch"}});

        insertRecipe(db, "Potato Soup", "1. Peel and cube potatoes.\n2. Boil in stock until soft.\n3. Blend with milk until smooth. Season and serve.",
                new String[][]{{"potato", "3", "unit"}, {"milk", "1", "cup"}, {"onion", "1", "unit"}, {"salt", "1", "pinch"}});

        insertRecipe(db, "Garlic Butter Rice", "1. Melt butter in a pan.\n2. Sauté garlic until fragrant.\n3. Stir in cooked rice and season.",
                new String[][]{{"rice", "2", "cup"}, {"garlic", "2", "clove"}, {"butter", "2", "tbsp"}, {"salt", "1", "pinch"}});

        insertRecipe(db, "Apple Oatmeal", "1. Cook oats with milk.\n2. Dice apple and stir in.\n3. Sweeten and serve warm.",
                new String[][]{{"oats", "1", "cup"}, {"milk", "1", "cup"}, {"apple", "1", "unit"}});

        insertRecipe(db, "Chicken Salad", "1. Shred cooked chicken.\n2. Mix with mayonnaise and chopped celery.\n3. Serve on lettuce or bread.",
                new String[][]{{"chicken", "150", "g"}, {"mayonnaise", "2", "tbsp"}, {"celery", "1", "stalk"}, {"lettuce", "2", "leaf"}});

        insertRecipe(db, "Simple Tomato Soup", "1. Sauté onion and garlic.\n2. Add tomatoes and stock, simmer 15 min.\n3. Blend until smooth and serve.",
                new String[][]{{"tomato", "4", "unit"}, {"onion", "1", "unit"}, {"garlic", "1", "clove"}});

        insertRecipe(db, "Egg Fried Noodles", "1. Boil noodles until soft.\n2. Scramble egg in a pan.\n3. Add noodles and soy sauce, stir fry together.",
                new String[][]{{"noodles", "1", "pack"}, {"egg", "2", "unit"}, {"soy sauce", "2", "tbsp"}});

        insertRecipe(db, "Carrot and Onion Soup", "1. Chop carrot and onion.\n2. Simmer in stock until soft.\n3. Blend until smooth and season.",
                new String[][]{{"carrot", "3", "unit"}, {"onion", "1", "unit"}, {"salt", "1", "pinch"}});
    }

    private void insertRecipe(SQLiteDatabase db, String name, String steps, String[][] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COL_RECIPE_NAME, name);
        recipeValues.put(COL_RECIPE_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        for (String[] ing : ingredients) {
            ContentValues ingValues = new ContentValues();
            ingValues.put(COL_RI_RECIPE_ID, recipeId);
            ingValues.put(COL_RI_INGREDIENT_NAME, ing[0]);
            ingValues.put(COL_RI_QUANTITY, Double.parseDouble(ing[1]));
            ingValues.put(COL_RI_UNIT, ing[2]);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, ingValues);
        }
    }

    // ---------- PANTRY CRUD METHODS ----------

    public long addPantryItem(String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, name.trim().toLowerCase());
        values.put(COL_PANTRY_QUANTITY, quantity);
        values.put(COL_PANTRY_UNIT, unit);
        values.put(COL_PANTRY_EXPIRY, expiryDate);
        long id = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return id;
    }

    public boolean updatePantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, name.trim().toLowerCase());
        values.put(COL_PANTRY_QUANTITY, quantity);
        values.put(COL_PANTRY_UNIT, unit);
        values.put(COL_PANTRY_EXPIRY, expiryDate);
        int rows = db.update(TABLE_PANTRY, values, COL_PANTRY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_PANTRY, COL_PANTRY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PANTRY, null, null, null, null, null, COL_PANTRY_NAME + " ASC");
    }

    public Cursor getRecipeById(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RECIPES, null, COL_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
    }

    public Cursor getIngredientsForRecipe(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RECIPE_INGREDIENTS, null, COL_RI_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
    }

    public Cursor getAllRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RECIPES, null, null, null, null, null, COL_RECIPE_NAME + " ASC");
    }
}