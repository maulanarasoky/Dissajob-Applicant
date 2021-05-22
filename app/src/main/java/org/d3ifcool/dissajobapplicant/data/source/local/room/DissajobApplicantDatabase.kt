package org.d3ifcool.dissajobapplicant.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.cv.CvEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.history.SearchHistoryEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.SavedJobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity

@Database(
    entities = [JobEntity::class,
        JobDetailsEntity::class,
        SavedJobEntity::class,
        ApplicationEntity::class,
        InterviewEntity::class,
        ApplicantEntity::class,
        RecruiterEntity::class,
        SearchHistoryEntity::class,
        CvEntity::class,
        ExperienceEntity::class,
        EducationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DissajobApplicantDatabase : RoomDatabase() {

    abstract fun jobDao(): JobDao
    abstract fun applicationDao(): ApplicationDao
    abstract fun interviewDao(): InterviewDao
    abstract fun applicantDao(): ApplicantDao
    abstract fun recruiterDao(): RecruiterDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun cvDao(): CvDao
    abstract fun experienceDao(): ExperienceDao
    abstract fun educationDao(): EducationDao

    companion object {

        @Volatile
        private var INSTANCE: DissajobApplicantDatabase? = null

        fun getInstance(context: Context): DissajobApplicantDatabase {
            if (INSTANCE == null) {
                synchronized(DissajobApplicantDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            DissajobApplicantDatabase::class.java,
                            "dissajobapplicant.db"
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE as DissajobApplicantDatabase
        }
    }
}