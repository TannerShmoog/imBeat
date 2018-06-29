package com.app.imbeat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

public class DirectoriesActivity extends AbstractMediaPlayerActivity {

    private Button addButton;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directories);

        listView = findViewById(R.id.directory_list_view);
        populateListView();

        //Opens file browser and allows selection of a system storage directory
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

    @Override
    public void onBackPressed() {
        saveVars();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveVars();
    }

    //Update the list view with new Directories
    protected void populateListView() {
        ArrayAdapter<Directory> adapter = new ArrayAdapter<Directory>(this,
                R.layout.directory_list_item, R.id.directory_list_view_item, getDirectoryList());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    //TODO: checkbox button on list items, clicking on list item opens that directory in a file viewer, no result

    @Override
    //After selecting a directory get the result as a URI path and make a Directory object from it
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

    //On click handler for delete button on each listView row
    public void onClickDeleteButton(final View view) {

        ConstraintLayout constraintLayout = (ConstraintLayout) view.getParent();
        TextView textView = (TextView) constraintLayout.findViewById(R.id.directory_list_view_item);
        final Button deleteButton = (Button) constraintLayout.findViewById(R.id.delete_directory_button);

        deleteButton.setEnabled(false);
        new AlertDialog.Builder(DirectoriesActivity.this)
                .setTitle("")
                .setMessage("Delete this directory?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        ListView listView = (ListView) view.getParent().getParent();
                        int position = listView.getPositionForView(view);
                        deleteDirectoryList(position);
                        populateListView();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteButton.setEnabled(true);
                    }
                }).show();
    }
}

