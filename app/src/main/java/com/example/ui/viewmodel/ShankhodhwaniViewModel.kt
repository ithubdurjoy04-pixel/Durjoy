package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.repository.ShankhodhwaniRepository
import com.example.domain.model.*
import com.example.util.DhakaTimeHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NoticesEvents : Screen("notices_events")
    object FinancesDonation : Screen("finances_donation")
    object HeritageGallery : Screen("heritage_gallery")
    object ComplaintPortal : Screen("complaint_portal")
    object AboutCommittee : Screen("about_committee")
    object AdminDashboard : Screen("admin_dashboard")
    object AdminFinanceWorkflow : Screen("admin_finance_workflow")
    object AdminMembers : Screen("admin_members")
    object AdminSettings : Screen("admin_settings")
}

class ShankhodhwaniViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShankhodhwaniRepository(AppDatabase.getInstance(application))

    // Navigation State
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Language State (Bengali default)
    private val _language = MutableStateFlow(AppLanguage.BENGALI)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.BENGALI) AppLanguage.ENGLISH else AppLanguage.BENGALI
    }

    // Dynamic Time-Based Theme State
    private val _isAutoTheme = MutableStateFlow(true)
    val isAutoTheme: StateFlow<Boolean> = _isAutoTheme.asStateFlow()

    private val _manualTheme = MutableStateFlow(TempleTheme.DAYLIGHT)
    val manualTheme: StateFlow<TempleTheme> = _manualTheme.asStateFlow()

    private val _dhakaTimeTheme = MutableStateFlow(DhakaTimeHelper.getCurrentDhakaTheme())
    val activeTheme: StateFlow<TempleTheme> = combine(_isAutoTheme, _manualTheme, _dhakaTimeTheme) { isAuto, manual, dhaka ->
        if (isAuto) dhaka else manual
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TempleTheme.DAYLIGHT)

    private val _dhakaTimeString = MutableStateFlow("")
    val dhakaTimeString: StateFlow<String> = _dhakaTimeString.asStateFlow()

    private val _dhakaDateString = MutableStateFlow("")
    val dhakaDateString: StateFlow<String> = _dhakaDateString.asStateFlow()

    // Admin Session State
    private val _currentAdminRole = MutableStateFlow<AdminRole?>(null)
    val currentAdminRole: StateFlow<AdminRole?> = _currentAdminRole.asStateFlow()

    // Feedback message (Snackbars / Toasts)
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    fun clearFeedback() {
        _feedbackMessage.value = null
    }

    // Data streams from Repository
    val financialSummary: StateFlow<FinancialSummary> = repository.financialSummary
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FinancialSummary(0.0, 0.0, 0.0, 0.0, 0.0, 0))

    val allMembers: StateFlow<List<MemberEntity>> = repository.allMembers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDeposits: StateFlow<List<DepositEntity>> = repository.allDeposits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allExpenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDonations: StateFlow<List<DonationEntity>> = repository.allDonations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val publishedNotices: StateFlow<List<NoticeEntity>> = repository.publishedNotices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotices: StateFlow<List<NoticeEntity>> = repository.allNotices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val publishedEvents: StateFlow<List<EventEntity>> = repository.publishedEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEvents: StateFlow<List<EventEntity>> = repository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val publishedGallery: StateFlow<List<GalleryEntity>> = repository.publishedGallery
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGallery: StateFlow<List<GalleryEntity>> = repository.allGallery
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allComplaints: StateFlow<List<ComplaintEntity>> = repository.allComplaints
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val socialLinks: StateFlow<List<SocialLinkEntity>> = repository.allSocialLinks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLogEntity>> = repository.auditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Modal & Dialog state
    var lastDonationReceipt = MutableStateFlow<DonationEntity?>(null)
    var trackedComplaintResult = MutableStateFlow<ComplaintEntity?>(null)
    var newComplaintTrackingId = MutableStateFlow<String?>(null)

    init {
        // Ticker to update Dhaka time and dynamic theme
        viewModelScope.launch {
            while (true) {
                _dhakaTimeString.value = DhakaTimeHelper.getFormattedDhakaTime(_language.value == AppLanguage.BENGALI)
                _dhakaDateString.value = DhakaTimeHelper.getFormattedDhakaDate(_language.value == AppLanguage.BENGALI)
                _dhakaTimeTheme.value = DhakaTimeHelper.getCurrentDhakaTheme()
                delay(30000) // update every 30 seconds
            }
        }
    }

    // Navigation
    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    // Theme controls
    fun setAutoTheme(enabled: Boolean) {
        _isAutoTheme.value = enabled
    }

    fun setManualTheme(theme: TempleTheme) {
        _isAutoTheme.value = false
        _manualTheme.value = theme
    }

    // Admin Role Switch / Login
    fun loginAsAdmin(role: AdminRole) {
        _currentAdminRole.value = role
        _feedbackMessage.value = "${role.titleBn} (${role.titleEn}) হিসেবে লগইন সফল"
    }

    fun logoutAdmin() {
        _currentAdminRole.value = null
        _feedbackMessage.value = "প্রশাসনিক প্যানেল থেকে লগআউট করা হয়েছে"
        _currentScreen.value = Screen.Home
    }

    // Member actions
    fun addMember(name: String, phone: String, address: String, notes: String) {
        val role = _currentAdminRole.value?.id ?: "ADMIN"
        viewModelScope.launch {
            repository.addMember(name, phone, address, notes, role)
            _feedbackMessage.value = "সদস্য সফলভাবে যুক্ত করা হয়েছে"
        }
    }

    fun toggleMemberStatus(member: MemberEntity) {
        val role = _currentAdminRole.value?.id ?: "ADMIN"
        viewModelScope.launch {
            repository.toggleMemberStatus(member, role)
            _feedbackMessage.value = "সদস্যের সক্রিয় অবস্থা হালনাগাদ করা হয়েছে"
        }
    }

    // Financial Draft -> Review -> Publish workflow
    fun createDraftDeposit(
        memberId: Long,
        memberName: String,
        amount: Double,
        depositDate: String,
        paymentMethod: String,
        transactionRef: String,
        description: String
    ) {
        val role = _currentAdminRole.value?.id ?: "TREASURER"
        viewModelScope.launch {
            repository.createDraftDeposit(memberId, memberName, amount, depositDate, paymentMethod, transactionRef, description, role)
            _feedbackMessage.value = "জমা খসড়া হিসেবে সংরক্ষিত হয়েছে (অনুমোদন প্রয়োজন)"
        }
    }

    fun createDraftExpense(
        title: String,
        category: String,
        amount: Double,
        expenseDate: String,
        paymentMethod: String,
        description: String
    ) {
        val role = _currentAdminRole.value?.id ?: "TREASURER"
        viewModelScope.launch {
            repository.createDraftExpense(title, category, amount, expenseDate, paymentMethod, description, "", role)
            _feedbackMessage.value = "ব্যয় খসড়া হিসেবে সংরক্ষিত হয়েছে (অনুমোদন প্রয়োজন)"
        }
    }

    fun markDepositUnderReview(id: Long) {
        val role = _currentAdminRole.value?.id ?: "PRESIDENT"
        viewModelScope.launch {
            repository.markDepositUnderReview(id, role)
            _feedbackMessage.value = "জমা পর্যালোচনায় নেওয়া হয়েছে"
        }
    }

    fun markExpenseUnderReview(id: Long) {
        val role = _currentAdminRole.value?.id ?: "PRESIDENT"
        viewModelScope.launch {
            repository.markExpenseUnderReview(id, role)
            _feedbackMessage.value = "ব্যয় পর্যালোচনায় নেওয়া হয়েছে"
        }
    }

    fun publishDeposit(id: Long) {
        val role = _currentAdminRole.value?.id ?: "PRESIDENT"
        viewModelScope.launch {
            repository.publishDeposit(id, role)
            _feedbackMessage.value = "জমা অনুমোদিত ও সর্বজনীনভাবে প্রকাশিত হয়েছে"
        }
    }

    fun publishExpense(id: Long) {
        val role = _currentAdminRole.value?.id ?: "PRESIDENT"
        viewModelScope.launch {
            repository.publishExpense(id, role)
            _feedbackMessage.value = "ব্যয় অনুমোদিত ও সর্বজনীনভাবে প্রকাশিত হয়েছে"
        }
    }

    fun unpublishDeposit(id: Long) {
        val role = _currentAdminRole.value?.id ?: "PRESIDENT"
        viewModelScope.launch {
            repository.unpublishDeposit(id, role)
            _feedbackMessage.value = "জমা অপ্রকাশিত করা হয়েছে"
        }
    }

    fun unpublishExpense(id: Long) {
        val role = _currentAdminRole.value?.id ?: "PRESIDENT"
        viewModelScope.launch {
            repository.unpublishExpense(id, role)
            _feedbackMessage.value = "ব্যয় অপ্রকাশিত করা হয়েছে"
        }
    }

    fun rejectDeposit(id: Long) {
        val role = _currentAdminRole.value?.id ?: "PRESIDENT"
        viewModelScope.launch {
            repository.rejectDeposit(id, role)
            _feedbackMessage.value = "জমা বাতিল করা হয়েছে"
        }
    }

    fun rejectExpense(id: Long) {
        val role = _currentAdminRole.value?.id ?: "PRESIDENT"
        viewModelScope.launch {
            repository.rejectExpense(id, role)
            _feedbackMessage.value = "ব্যয় বাতিল করা হয়েছে"
        }
    }

    // Donation checkout
    fun processDonation(
        name: String,
        phone: String,
        email: String,
        amount: Double,
        purpose: String,
        method: String,
        isAnonymous: Boolean,
        prayerNote: String
    ) {
        viewModelScope.launch {
            val receipt = repository.processDonation(name, phone, email, amount, purpose, method, isAnonymous, prayerNote)
            lastDonationReceipt.value = receipt
            _feedbackMessage.value = "অনুদানের জন্য ধন্যবাদ! রসিদ তৈরি হয়েছে।"
        }
    }

    // Complaint submission & tracking
    fun submitComplaint(name: String, phone: String, email: String, subject: String, message: String, isAnonymous: Boolean) {
        viewModelScope.launch {
            val trackingId = repository.submitComplaint(name, phone, email, subject, message, isAnonymous)
            newComplaintTrackingId.value = trackingId
            _feedbackMessage.value = "অভিযোগ গৃহীত হয়েছে। ট্র্যাকিং আইডি: $trackingId"
        }
    }

    fun trackComplaint(trackingId: String) {
        viewModelScope.launch {
            val complaint = repository.getComplaintByTrackingId(trackingId)
            trackedComplaintResult.value = complaint
            if (complaint == null) {
                _feedbackMessage.value = "এই ট্র্যাকিং আইডি দিয়ে কোনো অভিযোগ পাওয়া যায়নি"
            }
        }
    }

    fun updateComplaintStatus(complaint: ComplaintEntity, newStatus: String, adminNote: String) {
        val role = _currentAdminRole.value?.id ?: "SECRETARY"
        viewModelScope.launch {
            repository.updateComplaintStatus(complaint, newStatus, adminNote, role)
            _feedbackMessage.value = "অভিযোগের অগ্রগতি হালনাগাদ করা হয়েছে"
        }
    }

    // Notice actions
    fun createNotice(titleBn: String, titleEn: String, contentBn: String, contentEn: String, isUrgent: Boolean) {
        val role = _currentAdminRole.value?.id ?: "SECRETARY"
        viewModelScope.launch {
            repository.createNotice(titleBn, titleEn, contentBn, contentEn, isUrgent, role)
            _feedbackMessage.value = "নতুন বিজ্ঞপ্তি প্রকাশ করা হয়েছে"
        }
    }

    fun deleteNotice(notice: NoticeEntity) {
        val role = _currentAdminRole.value?.id ?: "SECRETARY"
        viewModelScope.launch {
            repository.deleteNotice(notice, role)
            _feedbackMessage.value = "বিজ্ঞপ্তি মুছে ফেলা হয়েছে"
        }
    }

    // Event actions
    fun createEvent(
        titleBn: String,
        titleEn: String,
        dateStr: String,
        timeStr: String,
        locationBn: String,
        locationEn: String,
        descBn: String,
        descEn: String
    ) {
        val role = _currentAdminRole.value?.id ?: "SECRETARY"
        viewModelScope.launch {
            repository.createEvent(titleBn, titleEn, dateStr, timeStr, locationBn, locationEn, descBn, descEn, role)
            _feedbackMessage.value = "নতুন ধর্মীয় পর্ব তালিকাভুক্ত করা হয়েছে"
        }
    }

    // Gallery actions
    fun addGalleryItem(titleBn: String, titleEn: String, album: String, mediaType: String, caption: String) {
        val role = _currentAdminRole.value?.id ?: "SECRETARY"
        viewModelScope.launch {
            repository.addGalleryItem(titleBn, titleEn, album, mediaType, caption, role)
            _feedbackMessage.value = "চিত্রশালায় নতুন সামগ্রী যুক্ত হয়েছে"
        }
    }

    // Social actions
    fun updateSocialLink(link: SocialLinkEntity) {
        val role = _currentAdminRole.value?.id ?: "SECRETARY"
        viewModelScope.launch {
            repository.updateSocialLink(link, role)
            _feedbackMessage.value = "${link.name} লিংক হালনাগাদ হয়েছে"
        }
    }
}
