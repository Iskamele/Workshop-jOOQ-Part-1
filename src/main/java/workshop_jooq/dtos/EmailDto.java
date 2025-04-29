package workshop_jooq.dtos;

/**
 * Record-based Data Transfer Object for email information.
 * <p>
 * This DTO represents an email address with its associated type/purpose.
 * It uses Java's record feature for a concise, immutable data container.
 */
public record EmailDto
        (String email, String type) {
}
