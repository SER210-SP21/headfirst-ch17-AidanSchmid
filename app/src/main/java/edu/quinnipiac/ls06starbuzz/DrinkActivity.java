package edu.quinnipiac.ls06starbuzz;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.os.AsyncTask;

public class DrinkActivity extends Activity {
    public static final String EXTRA_DRINKNO = "drinkNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        //Get the drink from the intent
        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);
//        Drink drink = Drink.drinks[drinkNo];

//        create cursor
        SQLiteOpenHelper StarbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = StarbuzzDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK", new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"}, "_id = ?", new String[]{Integer.toString(drinkNo)}, null, null, null);
//            move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                //get the drink details from the cursor
                String nameText = cursor.getString(0);
                String desctriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                //Populate the drink image
                ImageView photo = (ImageView) findViewById(R.id.photo);
//                photo.setImageResource(drink.getImageResourceId());
//                photo.setContentDescription(drink.getName());
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);
                //Populate the drink name
                TextView name = (TextView) findViewById(R.id.name);
//                name.setText(drink.getName());
                name.setText(nameText);
                //Populate the drink description
                TextView description = (TextView) findViewById(R.id.description);
//                description.setText(drink.getDescription());
                description.setText(desctriptionText);

                CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onFavoriteClicked(View view) {
        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);
        new UpdateDrinkTask().execute(drinkNo);
    }
    private class UpdateDrinkTask extends AsyncTask<Integer, Void,Boolean>{
        private ContentValues drinkValues;

        protected void onPreExecute(){
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }
        protected Boolean doInBackground(Integer... drinks){
            int drinkNo = drinks[0];
            SQLiteOpenHelper StarbuzzDatabaseHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);
            try{
                SQLiteDatabase db = StarbuzzDatabaseHelper.getWritableDatabase();
                db.update("DRINK", drinkValues, "_id = ?", new String[] {Integer.toString(drinkNo)});
                db.close();
                return true;
            }catch(SQLiteException e){
                return false;
            }
        }
        protected void onPostExecute(Boolean success) {
            if (!success){
                Toast toast = Toast.makeText(DrinkActivity.this,"Database unavailable",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
//    CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
//            ContentValues drinkValues = new ContentValues();
//            drinkValues.put("FAVORITE", favorite.isChecked());
//
//            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
//            try{
//                SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
//                db.update("DRINK",drinkValues,"_id = ?", new String[] {Integer.toString(drinkNo)});
//                db.close();
//            } catch (SQLiteException e){
//                Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
//                toast.show();
//            }