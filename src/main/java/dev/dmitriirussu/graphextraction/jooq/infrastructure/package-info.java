/**
 * jOOQ implementation of the read repository.
 *
 * <p>Package-private by design — the public API is
 * {@link dev.dmitriirussu.graphextraction.jooq.application.OwnerReadRepository}.
 *
 * <p>Internal: none — jOOQ MULTISET eliminates the need for intermediate
 * projection classes ({@code OwnerProjection}, {@code PetProjection}, etc.)
 * and manual deduplication via {@code LinkedHashMap}. The full object graph
 * is assembled in a single SQL query and mapped directly into view records
 * via {@code Records.mapping()}.
 */
package dev.dmitriirussu.graphextraction.jooq.infrastructure;