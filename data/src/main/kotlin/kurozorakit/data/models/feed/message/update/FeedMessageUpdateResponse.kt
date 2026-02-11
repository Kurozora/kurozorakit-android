package kurozorakit.data.models.feed.message.update

import kotlinx.serialization.Serializable

/**
 * The standard API response envelope for a message update operation.
 *
 * This model is returned after a successful `PATCH` or `POST` request to update
 * a feed message's attributes. It encapsulates the [FeedMessageUpdate] object,
 * which contains the final, server-validated values for the modified fields.
 *
 * Receiving this response confirms that the social interaction (e.g., a "Heart"
 * toggle or a "Spoiler" tag update) has been persisted in the database, allowing
 * the client to finalize its local state.
 *
 * @property data The encapsulated [FeedMessageUpdate] reflecting the confirmed changes.
 */
@Serializable
data class FeedMessageUpdateResponse(
    val data: FeedMessageUpdate
)