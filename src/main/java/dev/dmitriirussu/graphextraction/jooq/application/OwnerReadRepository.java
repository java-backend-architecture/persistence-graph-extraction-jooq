package dev.dmitriirussu.graphextraction.jooq.application;

import dev.dmitriirussu.graphextraction.jooq.application.view.OwnerListView;
import dev.dmitriirussu.graphextraction.jooq.application.view.OwnerView;

import java.util.List;
import java.util.Optional;

/**
 * Read-only repository for querying owner data.
 *
 * <p>Defined in the application layer as a port —
 * infrastructure provides the implementation.
 */
public interface OwnerReadRepository {
    Optional<OwnerView> findByIdWithGraph(String id);
    List<OwnerListView> findAllFlat();
    List<OwnerView> findAllWithGraph();
}
