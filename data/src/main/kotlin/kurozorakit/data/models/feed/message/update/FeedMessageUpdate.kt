package kurozorakit.data.models.feed.message.update

import kotlinx.serialization.Serializable

/**
 * A specialized Data Transfer Object (DTO) for mutating existing Feed Messages.
 *
 * [FeedMessageUpdate] implements the **Sparse Object Pattern**. Every property is
 * optional (nullable), allowing the client to transmit only the specific fields that
 * have changed rather than the entire resource. This model is primarily used for:
 *
 * 1. **Content Modification:** Updating text, HTML, or Markdown after an edit.
 * 2. **State Toggling:** Changing binary flags like NSFW, Spoiler, or Pinned status.
 * 3. **Social Interaction:** Communicating the "Heart" (Like) action to the server.
 *
 * This model is the primary input for the `update` logic within the `FeedMessage`
 * attributes, facilitating synchronization between local state and remote responses.
 *
 * @property content The updated raw text content. If null, the existing content is preserved.
 * @property contentHTML The updated pre-rendered HTML content, typically provided by the backend after a successful edit.
 * @property contentMarkdown The updated Markdown representation for flexible cross-platform rendering.
 * @property isHearted The toggle state for the "Heart" action. When applied, it triggers local metric adjustments.
 * @property isReShared Whether the feed message has been re-shared by the authenticated user.
 * @property myReShareID The identifier of the authenticated user's re-share of this message.
 * @property isNSFW The updated safety flag. Set to `true` to mark the message as sensitive/Not Safe For Work.
 * @property isPinned The updated pinning status. Set to `true` to keep the post at the top of the user's feed.
 * @property isSpoiler The updated spoiler flag. Set to `true` to hide content behind a spoiler warning.
 */
@Serializable
data class FeedMessageUpdate(
    val content: String? = null,
    val contentHTML: String? = null,
    val contentMarkdown: String? = null,
    val isHearted: Boolean? = null,
    val isReShared: Boolean? = null,
    val myReShareID: String? = null,
    val isNSFW: Boolean? = null,
    val isPinned: Boolean? = null,
    val isSpoiler: Boolean? = null
)