package kurozorakit.store.mapper

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kurozorakit.data.models.person.Person
import kurozorakit.store.db.PersonEntity

object PersonMapper {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun toDomain(entity: PersonEntity): Person {
        return Person(
            id = entity.id,
            type = entity.type,
            href = entity.href,
            attributes = Person.Attributes(
                slug = entity.slug,
                fullName = entity.full_name,
                fullGivenName = entity.full_given_name,
                about = entity.about,
                shortDescription = entity.short_description,
                age = entity.age,
                birthdateTimestamp = entity.birthdate_timestamp,
                deceasedDateTimestamp = entity.deceased_date_timestamp,
                astrologicalSign = entity.astrological_sign,
                givenRating = entity.given_rating,
                givenReview = entity.given_review,
                profile = entity.profile_json?.let { json.decodeFromString(it) },
                alternativeNames = entity.alternative_names_json?.let { json.decodeFromString(it) },
                websiteURLs = entity.website_urls_json?.let { json.decodeFromString(it) },
                stats = entity.stats_json?.let { json.decodeFromString(it) }
            ),
            relationships = entity.relationships_json?.let { json.decodeFromString(it) }
        )
    }

    fun toEntity(person: Person): PersonEntity {
        val attr = person.attributes
        return PersonEntity(
            id = person.id,
            type = person.type,
            href = person.href,
            slug = attr.slug,
            full_name = attr.fullName,
            full_given_name = attr.fullGivenName,
            about = attr.about,
            short_description = attr.shortDescription,
            age = attr.age,
            birthdate_timestamp = attr.birthdateTimestamp,
            deceased_date_timestamp = attr.deceasedDateTimestamp,
            astrological_sign = attr.astrologicalSign,
            given_rating = attr.givenRating,
            given_review = attr.givenReview,
            profile_json = attr.profile?.let { json.encodeToString(it) },
            alternative_names_json = attr.alternativeNames?.let { json.encodeToString(it) },
            website_urls_json = attr.websiteURLs?.let { json.encodeToString(it) },
            stats_json = attr.stats?.let { json.encodeToString(it) },
            relationships_json = person.relationships?.let { json.encodeToString(it) },
            created_at = System.currentTimeMillis(),
            updated_at = System.currentTimeMillis()
        )
    }
}