package com.app.imbeat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.net.URI;

public class DirectoriesActivity extends AbstractMediaPlayerActivity {

    private Button addButton;
    private Button deleteButton;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directories);

        listView = findViewById(R.id.directory_list_view);
        populateListView();
        /**TODO: Need custom adapter for this
        deleteButton = findViewById(R.id.delete_directory_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               int position = (Integer) view.getTag();
               deleteDirectoryList(position);
               populateListView();
           }
        });
        **/
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

    protected void populateListView() {
        ArrayAdapter<Directory> adapter = new ArrayAdapter<Directory>(this,
                R.layout.directory_list_item, R.id.directory_list_view_item, getDirectoryList());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    //TODO: Populate list view, checkbox/delete button on list items, clicking on list item opens that directory in a file viewer, no result

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 9999:
                try {
                    String result = data.getData().toString();
                    Directory newDirectory = new Directory(result, true);
                    addDirectoryList(newDirectory);
                    saveVars();
                    populateListView();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
