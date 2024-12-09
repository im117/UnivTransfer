#!/usr/bin/env python3

import csv
import sqlite3

WCC_COLLEGE_ID = 1
UM_COLLEGE_ID = 4

if __name__ == "__main__":
    # Get a handle on the database
    connection = sqlite3.connect("../app/src/main/assets/transfer-db.db")
    cur = connection.cursor()

    # Read the CSV
    with open("wcc.ccaa.csv") as csvfile:
        reader = csv.reader(csvfile)
        # Add courses to database
        first_row = True
        for row in reader:
            # Skip the first row
            if first_row:
                first_row = False
                continue

            # Check if the wcc course exists in the database
            wcc_subject_code = row[0]
            wcc_course_number = row[1]
            wcc_course_name = row[2]
            query_result = cur.execute(
                "SELECT * FROM Course WHERE college_id = ? AND subject_code = ? AND course_number = ?;",
                (WCC_COLLEGE_ID, wcc_subject_code, wcc_course_number)
            )
            wcc_course_in_db = query_result.fetchone()

            # Add course if necessary
            if wcc_course_in_db is None:
                # Add the course
                cur.execute("""INSERT INTO course (college_id, subject_code, course_number, course_name)
                            VALUES (?, ?, ?, ?);
                            """,
                            (WCC_COLLEGE_ID, wcc_subject_code, wcc_course_number, wcc_course_name))

            # Get the course ID
            wcc_course_id = cur.execute(
                "SELECT course_id FROM course WHERE college_id = ? AND subject_code = ? and course_number = ?",
                (WCC_COLLEGE_ID, wcc_subject_code, wcc_course_number)
            ).fetchone()[0]

            # Check if the um course exists in the database
            um_subject_code = row[3]
            um_course_number = row[4]
            um_course_name = row[5]
            query_result = cur.execute(
                "SELECT * FROM Course WHERE college_id = ? AND subject_code = ? AND course_number = ?;",
                (UM_COLLEGE_ID, um_subject_code, um_course_number)
            )
            um_course_in_db = query_result.fetchone()

            # Add course if necessary
            if um_course_in_db is None:
                # Add the course
                cur.execute("""INSERT INTO course (college_id, subject_code, course_number, course_name)
                            VALUES (?, ?, ?, ?);
                            """, (UM_COLLEGE_ID, um_subject_code, um_course_number, um_course_name))

            # Get the course ID:
            um_course_id = cur.execute(
                "SELECT course_id FROM course WHERE college_id = ? AND subject_code = ? AND course_number = ?",
                (UM_COLLEGE_ID, um_subject_code, um_course_number)
            ).fetchone()[0]

            # Add the relationship to the database (if does not already exist)
            relationship_match = cur.execute(
                "SELECT * FROM transfer_equiv WHERE two_year_course_id = ? and four_year_course_id = ?",
                (wcc_course_id, um_course_id)
            )

            if relationship_match.fetchone() is None:
                cur.execute(
                    "INSERT INTO transfer_equiv (two_year_course_id, four_year_course_id) VALUES (?, ?)",
                    (wcc_course_id, um_course_id)
                )
    # Commit the database transaction
    connection.commit()
