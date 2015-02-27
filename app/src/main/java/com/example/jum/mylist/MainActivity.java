package com.example.jum.mylist;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    String[] colournames;
    int bckColour= Color.argb(255,255,255,255);
    String bckColourName = "White";
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize resources
        colournames = getResources().getStringArray(R.array.listArray);
        String[] colourvalues = getResources().getStringArray(R.array.listValues);

        //map values to names
        final HashMap<String, String> link = new HashMap<>();
        for(int i =0 ; i < colournames.length; i++) {
          link.put(colournames[i],colourvalues[i]);
        }
        lv = (ListView) findViewById(R.id.listView);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.activity_listview, colournames);
        lv.setAdapter(aa);

        //set default color
        if(!colourFromFile()) {
          lv.setBackgroundColor(Color.argb(255,255,255,255));
        }



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                bckColourName = (String) ((TextView) view).getText();

                String colour = link.get(bckColourName);
                int r = Integer.parseInt(colour.substring(2, 4), 16);
                int g = Integer.parseInt(colour.substring(4, 6), 16);
                int b = Integer.parseInt(colour.substring(6), 16);
                bckColour = Color.argb(255, r, g, b);

                lv.setBackgroundColor(bckColour);

                Toast.makeText(getApplicationContext(), bckColourName, Toast.LENGTH_SHORT).show();
            }
        });

        registerForContextMenu(lv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select the action");
        menu.add(0,v.getId(),0,"Write colour to SDcard");
        menu.add(0,v.getId(),0,"Read colour from SDcard");
    }
    public boolean colourToFile(){
        try {
            File sdfile = new File("/sdcard/sdcolour.dat");
            sdfile.createNewFile();
            FileOutputStream sdColour = new FileOutputStream(sdfile);
            ObjectOutputStream fO = new ObjectOutputStream(sdColour);
            fO.writeInt(bckColour);
            fO.writeObject(bckColourName);
            fO.close();
            sdColour.close();
        }catch(Exception e) {
            return false;
        }
        return true;
    }
    public boolean colourFromFile(){
        try {
            File sdfile = new File("/sdcard/sdcolour.dat");
            FileInputStream sdColour = new FileInputStream(sdfile);
            ObjectInputStream fO = new ObjectInputStream(sdColour);
            bckColour = fO.readInt();
            bckColourName = (String)fO.readObject();
            lv.setBackgroundColor(bckColour);
            fO.close();
            sdColour.close();

        }catch(Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Write colour to SDcard") {
                if(colourToFile())
                    Toast.makeText(getApplicationContext(), "Write colour: " + bckColourName, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Colour not saved", Toast.LENGTH_LONG).show();

        } else if (item.getTitle() == "Read colour from SDcard") {

                if(colourFromFile())
                    Toast.makeText(getApplicationContext(), "Read colour: " + bckColourName, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "No colour found", Toast.LENGTH_LONG).show();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
