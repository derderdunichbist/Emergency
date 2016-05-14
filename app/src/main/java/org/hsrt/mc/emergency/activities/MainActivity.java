package org.hsrt.mc.emergency.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.hsrt.mc.emergency.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "Andy stinkt", Toast.LENGTH_LONG).show();
    }
}
