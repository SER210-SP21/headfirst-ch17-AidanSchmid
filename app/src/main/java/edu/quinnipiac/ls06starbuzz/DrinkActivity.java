package edu.quinnipiac.ls06starbuzz;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        try{
            SQLiteDatabase db = StarbuzzDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK",new String[] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID"},"_id = ?", new String[] {Integer.toString(drinkNo)},null,null,null);
//            move to the first record in the Cursor
            if (cursor.moveToFirst()){
                //get the drink details from the cursor
                String nameText = cursor.getString(0);
                String desctriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);

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
            }
            cursor.close();
            db.close();
        }catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }


        {
        }
    }
}