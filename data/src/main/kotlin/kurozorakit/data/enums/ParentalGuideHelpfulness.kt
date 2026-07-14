package kurozorakit.data.enums

enum class ParentalGuideHelpfulness(val rawValue: Int) {
    unhelpful(-1),
    disabled(0),
    helpful(1);

    companion object {
        val allCases: Array<ParentalGuideHelpfulness>
            get() = entries.toTypedArray()

        fun fromBoolean(bool: Boolean?): ParentalGuideHelpfulness {
            return if (bool == null) disabled else if (bool) helpful else unhelpful
        }
    }
}
