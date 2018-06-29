package com.app.imbeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DirectoriesActivity extends AbstractMediaPlayerActivity {

    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directories);

        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
            }
        });
    }

    //TODO: Populate list view, save/load directories externally, checkbox/delete button on list items

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 9999:
                Log.i("Test", "Result URI " + data.getData());
                break;
        }
    }
}
