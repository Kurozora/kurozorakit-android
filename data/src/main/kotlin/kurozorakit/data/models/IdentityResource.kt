package kurozorakit.data.models

/**
 * A core internal interface that defines the standard metadata for any identifiable API resource.
 *
 * `IdentityResource` acts as the base contract for resources that follow a JSON:API-like
 * structure, ensuring they possess a unique identity, a type classification, and a
 * self-referencing URI.
 *
 * @property id The unique string identifier for the specific resource.
 * @property type The classification or name of the resource type (e.g., "users", "tokens", "media").
 * This is often used for polymorphic serialization and determining the schema of the attributes.
 * @property href The absolute or relative URL string (Hyperlink Reference) pointing to the
 * resource itself. This allows for HATEOAS-compliant navigation within the API.
 */
internal interface IdentityResource {
    val id: String
    val type: String
    val href: String
}