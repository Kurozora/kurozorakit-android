package kurozorakit.store.mapper

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kurozorakit.data.models.character.Character
import kurozorakit.store.db.CharacterEntity

object CharacterMapper {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun toDomain(entity: CharacterEntity): Character {
        return Character(
            id = entity.id,
            type = entity.type,
            href = entity.href,
            attributes = Character.Attributes(
                slug = entity.slug,
                name = entity.name,
                about = entity.about,
                shortDescription = entity.short_description,
                debut = entity.debut,
                status = entity.status,
                bloodType = entity.blood_type,
                favoriteFood = entity.favorite_food,
                bustSize = entity.bust_size,
                waistSize = entity.waist_size,
                hipSize = entity.hip_size,
                height = entity.height,
                weight = entity.weight,
                age = entity.age,
                birthdate = entity.birthdate,
                astrologicalSign = entity.astrological_sign,
                givenRating = entity.given_rating,
                givenReview = entity.given_review,
                profile = entity.profile_json?.let { json.decodeFromString(it) },
                stats = entity.stats_json?.let { json.decodeFromString(it) }
            ),
            relationships = entity.relationships_json?.let { json.decodeFromString(it) }
        )
    }

    fun toEntity(character: Character): CharacterEntity {
        val attr = character.attributes
        return CharacterEntity(
            id = character.id,
            type = character.type,
            href = character.href,
            slug = attr.slug,
            name = attr.name,
            about = attr.about,
            short_description = attr.shortDescription,
            debut = attr.debut,
            status = attr.status,
            blood_type = attr.bloodType,
            favorite_food = attr.favoriteFood,
            bust_size = attr.bustSize,
            waist_size = attr.waistSize,
            hip_size = attr.hipSize,
            height = attr.height,
            weight = attr.weight,
            age = attr.age,
            birthdate = attr.birthdate,
            astrological_sign = attr.astrologicalSign,
            given_rating = attr.givenRating,
            given_review = attr.givenReview,
            profile_json = attr.profile?.let { json.encodeToString(it) },
            stats_json = attr.stats?.let { json.encodeToString(it) },
            relationships_json = character.relationships?.let { json.encodeToString(it) },
            created_at = System.currentTimeMillis(),
            updated_at = System.currentTimeMillis()
        )
    }
}