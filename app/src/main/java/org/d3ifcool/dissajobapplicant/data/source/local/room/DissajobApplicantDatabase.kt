package org.d3ifcool.dissajobapplicant.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.history.SearchHistoryEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.SavedJobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.notification.NotificationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity

@Database(
    entities = [
        ApplicantEntity::class,
        ApplicationEntity::class,
        EducationEntity::class,
        ExperienceEntity::class,
        InterviewEntity::class,
        JobEntity::class,
        JobDetailsEntity::class,
        MediaEntity::class,
        SavedJobEntity::class,
        RecruiterEntity::class,
        SearchHistoryEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DissajobApplicantDatabase : RoomDatabase() {

    abstract fun applicantDao(): ApplicantDao
    abstract fun applicationDao(): ApplicationDao
    abstract fun mediaDao(): MediaDao
    abstract fun educationDao(): EducationDao
    abstract fun experienceDao(): ExperienceDao
    abstract fun interviewDao(): InterviewDao
    abstract fun jobDao(): JobDao
    abstract fun recruiterDao(): RecruiterDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun notificationDao(): NotificationDao

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