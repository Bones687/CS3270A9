package edu.weber.neildalton.cs3270.cs3270a9;

import android.content.SharedPreferences;

/**
 * Created by neildalton on 6/8/15.
 */
public class CanvasObjects {
    protected class Course{
        protected String id;
        protected String sis_course_id;
        protected String name;
        protected String course_code;
        protected String account_id;
        protected String start_at;
        protected String end_at;
        protected String syllabus_body;
        protected String needs_grading_count;
        protected Enrollment[] enrollments;
        protected Calendar calendar;
        protected Term term;
    }

    protected class Term{
        protected String id;
        protected String name;
        protected String start_at;
        protected String end_at;
    }

    protected class Calendar{
        protected String ics;
    }

    protected class Enrollment{
        protected String type;
        protected String role;
        protected String computed_final_score;
        protected String computed_current_score;
        protected String computed_final_grade;
    }
}
