package com.example.domain.model

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    BENGALI("bn", "Bengali", "বাংলা"),
    ENGLISH("en", "English", "English")
}

enum class TempleTheme(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val timeSlot: String,
    val descriptionBn: String,
    val descriptionEn: String
) {
    DAWN_MISTY(
        "dawn_misty",
        "উষা / কুয়াশাচ্ছন্ন",
        "Dawn / Misty",
        "04:00–06:00",
        "ভোরের শীতল ও প্রশান্ত কুয়াশাচ্ছন্ন পরিবেশ",
        "Cool, tranquil dawn ambiance"
    ),
    SUNRISE_MORNING(
        "sunrise_morning",
        "প্রভাত / সূর্যোদয়",
        "Sunrise / Morning",
        "06:00–11:00",
        "সোনালী রৌদ্র ও ভোরের স্তোত্র পাঠের আভা",
        "Golden morning radiance and spiritual awakening"
    ),
    DAYLIGHT(
        "daylight",
        "দিবা / মধ্যাহ্ন",
        "Daylight",
        "11:00–15:00",
        "উজ্জ্বল দিন ও পরিষ্কার আলো",
        "Bright crisp daylight illumination"
    ),
    GOLDEN_HOUR(
        "golden_hour",
        "অপরাহ্ন / গোধূলি",
        "Golden Hour",
        "15:00–17:00",
        "মধুর অপরাহ্ন ও মৃদু স্বর্ণালী আভা",
        "Warm amber sunset and peaceful dusk"
    ),
    EVENING_LAMPS(
        "evening_lamps",
        "সন্ধ্যা / প্রদীপ",
        "Evening / Temple Lamps",
        "17:00–19:00",
        "সন্ধ্যা আরতি ও প্রজ্বলিত মঙ্গল প্রদীপের আভা",
        "Evening aarti and sacred glowing temple lamps"
    ),
    NIGHT_DURGA(
        "night_durga",
        "নিশি / মাতৃ আভা",
        "Night / Maa Durga Ambience",
        "19:00–04:00",
        "গভীর রাত্রির নীল ও মা দুর্গার অলৌকিক স্নিগ্ধ মহিমা",
        "Midnight sacred blue with Maa Durga's divine aura"
    )
}

enum class AdminRole(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val defaultUsername: String
) {
    PRESIDENT(
        "president",
        "সভাপতি",
        "President",
        "president"
    ),
    SECRETARY(
        "secretary",
        "সম্পাদক",
        "Secretary",
        "secretary"
    ),
    TREASURER(
        "treasurer",
        "কোষাধ্যক্ষ",
        "Treasurer",
        "treasurer"
    )
}

enum class TransactionStatus(val bn: String, val en: String) {
    DRAFT("খসড়া", "Draft"),
    UNDER_REVIEW("পর্যালোচনাধীন", "Under Review"),
    PUBLISHED("প্রকাশিত", "Published"),
    REJECTED("প্রত্যাখ্যাত", "Rejected"),
    UNPUBLISHED("অপ্রকাশিত", "Unpublished")
}

enum class TransactionType(val bn: String, val en: String) {
    DEPOSIT("সদস্য জমা", "Member Deposit"),
    EXPENSE("ব্যয় / খরচ", "Expense"),
    DONATION("সাধারণ অনুদান", "General Donation")
}

enum class ComplaintStatus(val bn: String, val en: String) {
    NEW("নতুন গৃহীত", "New"),
    UNDER_REVIEW("তদন্তাধীন / পর্যালোচনাধীন", "Under Review"),
    RESOLVED("নিষ্পত্তি হয়েছে", "Resolved"),
    REJECTED("বাতিল করা হয়েছে", "Rejected")
}

data class FinancialSummary(
    val totalPublishedIncome: Double,
    val totalPublishedExpense: Double,
    val currentBalance: Double,
    val totalDraftDeposits: Double,
    val totalDraftExpenses: Double,
    val pendingReviewCount: Int
)
