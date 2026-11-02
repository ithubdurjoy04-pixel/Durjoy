package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY id DESC")
    fun getAllMembers(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE status = 'ACTIVE' ORDER BY fullName ASC")
    fun getActiveMembers(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE id = :memberId LIMIT 1")
    suspend fun getMemberById(memberId: Long): MemberEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity): Long

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Delete
    suspend fun deleteMember(member: MemberEntity)
}

@Dao
interface FinanceDao {
    // Deposits
    @Query("SELECT * FROM deposits ORDER BY createdAt DESC")
    fun getAllDeposits(): Flow<List<DepositEntity>>

    @Query("SELECT * FROM deposits WHERE status = :status ORDER BY createdAt DESC")
    fun getDepositsByStatus(status: String): Flow<List<DepositEntity>>

    @Query("SELECT * FROM deposits WHERE memberId = :memberId ORDER BY depositDate DESC")
    fun getDepositsByMember(memberId: Long): Flow<List<DepositEntity>>

    @Query("SELECT * FROM deposits WHERE id = :id LIMIT 1")
    suspend fun getDepositById(id: Long): DepositEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeposit(deposit: DepositEntity): Long

    @Update
    suspend fun updateDeposit(deposit: DepositEntity)

    @Delete
    suspend fun deleteDeposit(deposit: DepositEntity)

    // Expenses
    @Query("SELECT * FROM expenses ORDER BY createdAt DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE status = :status ORDER BY createdAt DESC")
    fun getExpensesByStatus(status: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    suspend fun getExpenseById(id: Long): ExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    // Financial Aggregate Calculations
    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM deposits WHERE status = 'PUBLISHED'")
    fun getPublishedDepositsTotal(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM expenses WHERE status = 'PUBLISHED'")
    fun getPublishedExpensesTotal(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM deposits WHERE status = 'DRAFT' OR status = 'UNDER_REVIEW'")
    fun getDraftDepositsTotal(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM expenses WHERE status = 'DRAFT' OR status = 'UNDER_REVIEW'")
    fun getDraftExpensesTotal(): Flow<Double>

    @Query("SELECT COUNT(*) FROM deposits WHERE status = 'UNDER_REVIEW' OR status = 'DRAFT'")
    fun getPendingReviewDepositsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM expenses WHERE status = 'UNDER_REVIEW' OR status = 'DRAFT'")
    fun getPendingReviewExpensesCount(): Flow<Int>
}

@Dao
interface DonationDao {
    @Query("SELECT * FROM donations ORDER BY createdAt DESC")
    fun getAllDonations(): Flow<List<DonationEntity>>

    @Query("SELECT * FROM donations WHERE id = :id LIMIT 1")
    suspend fun getDonationById(id: Long): DonationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonation(donation: DonationEntity): Long

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM donations WHERE status = 'SUCCESS'")
    fun getTotalSuccessfulDonations(): Flow<Double>
}

@Dao
interface NoticeDao {
    @Query("SELECT * FROM notices WHERE isPublished = 1 ORDER BY isUrgent DESC, createdAt DESC")
    fun getPublishedNotices(): Flow<List<NoticeEntity>>

    @Query("SELECT * FROM notices ORDER BY createdAt DESC")
    fun getAllNotices(): Flow<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: NoticeEntity): Long

    @Update
    suspend fun updateNotice(notice: NoticeEntity)

    @Delete
    suspend fun deleteNotice(notice: NoticeEntity)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE isPublished = 1 ORDER BY dateStr ASC")
    fun getPublishedEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events ORDER BY dateStr ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)
}

@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery_items WHERE isPublished = 1 ORDER BY createdAt DESC")
    fun getPublishedGallery(): Flow<List<GalleryEntity>>

    @Query("SELECT * FROM gallery_items ORDER BY createdAt DESC")
    fun getAllGallery(): Flow<List<GalleryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGallery(item: GalleryEntity): Long

    @Update
    suspend fun updateGallery(item: GalleryEntity)

    @Delete
    suspend fun deleteGallery(item: GalleryEntity)
}

@Dao
interface ComplaintDao {
    @Query("SELECT * FROM complaints ORDER BY createdAt DESC")
    fun getAllComplaints(): Flow<List<ComplaintEntity>>

    @Query("SELECT * FROM complaints WHERE trackingId = :trackingId LIMIT 1")
    suspend fun getComplaintByTrackingId(trackingId: String): ComplaintEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity): Long

    @Update
    suspend fun updateComplaint(complaint: ComplaintEntity)
}

@Dao
interface SocialDao {
    @Query("SELECT * FROM social_links")
    fun getAllSocialLinks(): Flow<List<SocialLinkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialLinks(links: List<SocialLinkEntity>)

    @Update
    suspend fun updateSocialLink(link: SocialLinkEntity)
}

@Dao
interface AuditDao {
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 200")
    fun getAllLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AuditLogEntity): Long
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM site_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<SiteSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SiteSettingsEntity)
}
