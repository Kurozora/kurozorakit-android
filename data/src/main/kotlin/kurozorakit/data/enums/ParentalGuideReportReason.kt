package kurozorakit.data.enums

enum class ParentalGuideReportReason(val value: String) {
    inaccurate("inaccurate"),
    spoiler("spoiler"),
    spam("spam"),
    inappropriate("inappropriate"),
    other("other");

    companion object {
        val allCases: Array<ParentalGuideReportReason>
            get() = entries.toTypedArray()
    }
}
