package dev.dmitriirussu.graphextraction.jooq.application.view;

import java.util.List;

/**
 * Application read model.
 */
public record OwnerListView(
        String id,
        String name,
        List<String> pets
) {
    public OwnerListView { pets = List.copyOf(pets); }
}
