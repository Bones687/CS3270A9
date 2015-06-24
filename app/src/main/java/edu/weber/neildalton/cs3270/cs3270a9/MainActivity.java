package edu.weber.neildalton.cs3270.cs3270a9;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.net.URL;
////import java.net.ssl.HttpsURLConnection;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import org.apache.http.protocol.HTTP;
//
//import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity
        implements CourseListFragment.CourseListFragmentListener,
        DetailsFragment.DetailsFragmentListener,
        AddEditFragment.AddEditFragmentListener
{
    // keys for storing row ID in Bundle passed to a fragment
    public static final String ROW_ID = "row_id";

    CourseListFragment courseListFragment; // displays course list

    // display courseListFragment when MainActivity first loads
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // return if Activity is being restored, no need to recreate GUI
        if (savedInstanceState != null)
            return;

        // check whether layout contains fragmentContainer (phone layout);
        // courseListFragment is always displayed
        if (findViewById(R.id.fragmentContainer) != null)
        {
            // create courseListFragment
            courseListFragment = new CourseListFragment();

            // add the fragment to the FrameLayout
            FragmentTransaction transaction =
                    getFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, courseListFragment);
            transaction.commit(); // causes courseListFragment to display
        }
    }

    // called when MainActivity resumes
    @Override
    protected void onResume()
    {
        super.onResume();

    }

    // display DetailsFragment for selected course
    @Override
    public void onCourseSelected(long rowID)
    {
        displayCourse(rowID, R.id.fragmentContainer);
    }

    @Override
    public void onCourseLongSelected(long rowID) { displayAssignments(rowID, R.id.fragmentContainer); }

    // display a course
    private void displayAssignments(long rowID, int viewID){
        CourseListFragment clf = new CourseListFragment();
        clf.onGetImportAssignments();
    }

    private void displayCourse(long rowID, int viewID)
    {
        DetailsFragment detailsFragment = new DetailsFragment();

        // specify rowID as an argument to the DetailsFragment
        Bundle arguments = new Bundle();
        arguments.putLong(ROW_ID, rowID);
        detailsFragment.setArguments(arguments);

        // use a FragmentTransaction to display the DetailsFragment
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.replace(viewID, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailsFragment to display
    }

    // display the AddEditFragment to add a new course
    @Override
    public void onAddCourse()
    {
        displayAddEditFragment(R.id.fragmentContainer, null);
    }

    // display fragment for adding a new or editing an existing course
    private void displayAddEditFragment(int viewID, Bundle arguments)
    {
        AddEditFragment addEditFragment = new AddEditFragment();

        if (arguments != null) // editing existing course
            addEditFragment.setArguments(arguments);

        // use a FragmentTransaction to display the AddEditFragment
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes AddEditFragment to display
    }
    // return to course list when displayed course deleted
    @Override
    public void onCourseDelete()
    {
        getFragmentManager().popBackStack(); // removes top of back stack

        courseListFragment.updateCourseList();
    }

    // display the AddEditFragment to edit an existing course
    @Override
    public void onEditCourse(Bundle arguments)
    {
        displayAddEditFragment(R.id.fragmentContainer, arguments);
    }

    // update GUI after new course or updated course saved
    @Override
    public void onAddEditCompleted(long rowID)
    {
        getFragmentManager().popBackStack(); // removes top of back stack
        getFragmentManager().popBackStack(); // removes top of back stack
        courseListFragment.updateCourseList(); // refresh courses
    }
}
