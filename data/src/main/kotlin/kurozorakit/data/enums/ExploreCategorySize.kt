package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Defines the visual layout and scaling constraints for items in the Explore section.
 *
 * The Kurozora API provides this hint to help the client application determine
 * which UI component or aspect ratio to use when rendering media cards or
 * featured highlights in discovery feeds.
 */
@Serializable
enum class ExploreCategorySize {
    /** * A high-impact, wide-format display. Typically used for featured spotlights
     * at the top of the feed, often utilizing backdrops instead of posters.
     */
    banner,

    /** * A prominent card format. Used for major categories or trending titles
     * that require more visual detail.
     */
    large,

    /** * The standard grid or list size. Balances information density with
     * visual appeal for general browsing.
     */
    medium,

    /** * A compact format used for dense lists or secondary suggestions where
     * screen real estate is limited.
     */
    small,

    /** * A specialized layout tailored for unreleased media, often emphasizing
     * release dates or "remind me" functionality.
     */
    upcoming,

    /** * A layout optimized for video content, typically using a 16:9 aspect
     * ratio suited for trailers or promotional clips.
     */
    video
}