package edu.weber.neildalton.cs3270.cs3270a9;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AddEditFragment extends Fragment
{
    // callback method implemented by MainActivity
    public interface AddEditFragmentListener
    {
        // called after edit completed so course can be redisplayed
        public void onAddEditCompleted(long rowID);
    }

    private AddEditFragmentListener listener;

    private long rowID; // database row ID of the course
    private Bundle courseInfoBundle; // arguments for editing a course

    // EditTexts for course information
    private EditText idEditText;
    private EditText nameEditText;
    private EditText courseCodeEditText;
    private EditText startEditText;
    private EditText endEditText;

    // set AddEditFragmentListener when Fragment attached
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        listener = (AddEditFragmentListener) activity;
    }

    // remove AddEditFragmentListener when Fragment detached
    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    // called when Fragment's view needs to be created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true); // save fragment across config changes
        setHasOptionsMenu(true); // fragment has menu items to display

        // inflate GUI and get references to EditTexts
        View view =
                inflater.inflate(R.layout.fragment_add_edit, container, false);
        idEditText = (EditText) view.findViewById(R.id.idEditText);
        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        courseCodeEditText = (EditText) view.findViewById(R.id.courseCodeEditTest);
        startEditText = (EditText) view.findViewById(R.id.startEditText);
        endEditText = (EditText) view.findViewById(R.id.endEditText);

        courseInfoBundle = getArguments(); // null if creating new course

        if (courseInfoBundle != null)
        {
            rowID = courseInfoBundle.getLong(MainActivity.ROW_ID);
            idEditText.setText(courseInfoBundle.getString("id"));
            nameEditText.setText(courseInfoBundle.getString("name"));
            courseCodeEditText.setText(courseInfoBundle.getString("course_code"));
            startEditText.setText(courseInfoBundle.getString("start_at"));
            endEditText.setText(courseInfoBundle.getString("end_at"));
        }

        // set Save course Button's event listener
        Button savecourseButton =
                (Button) view.findViewById(R.id.savecourseButton);
        savecourseButton.setOnClickListener(savecourseButtonClicked);
        return view;
    }

    // responds to event generated when user saves a course
    OnClickListener savecourseButtonClicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (nameEditText.getText().toString().trim().length() != 0)
            {
                // AsyncTask to save course, then notify listener
                AsyncTask<Object, Object, Object> savecourseTask =
                        new AsyncTask<Object, Object, Object>()
                        {
                            @Override
                            protected Object doInBackground(Object... params)
                            {
                                savecourse(); // save course to the database
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object result)
                            {
                                // hide soft keyboard
                                InputMethodManager imm = (InputMethodManager)
                                        getActivity().getSystemService(
                                                Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(
                                        getView().getWindowToken(), 0);

                                listener.onAddEditCompleted(rowID);
                            }
                        }; // end AsyncTask

                // save the course to the database using a separate thread
                savecourseTask.execute((Object[]) null);
            }
            else // required course name is blank, so display error dialog
            {
                DialogFragment errorSaving =
                        new DialogFragment()
                        {
                            @Override
                            public Dialog onCreateDialog(Bundle savedInstanceState)
                            {
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.error_message);
                                builder.setPositiveButton(R.string.ok, null);
                                return builder.create();
                            }
                        };

                errorSaving.show(getFragmentManager(), "error saving course");
            }
        } // end method onClick
    }; // end OnClickListener savecourseButtonClicked

    // saves course information to the database
    private void savecourse()
    {
        // get DatabaseConnector to interact with the SQLite database
        DatabaseConnector databaseConnector =
                new DatabaseConnector(getActivity());

        if (courseInfoBundle == null)
        {
            rowID = databaseConnector.insertCourse(
                    idEditText.getText().toString(),
                    nameEditText.getText().toString(),
                    courseCodeEditText.getText().toString(),
                    startEditText.getText().toString(),
                    endEditText.getText().toString()
            );
        }
        else
        {
            databaseConnector.updateCourse(rowID,
                    idEditText.getText().toString(),
                    nameEditText.getText().toString(),
                    courseCodeEditText.getText().toString(),
                    startEditText.getText().toString(),
                    endEditText.getText().toString());

        }
    } // end method savecourse
} // end class AddEditFragment
