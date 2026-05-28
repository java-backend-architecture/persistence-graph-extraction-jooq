package dev.dmitriirussu.graphextraction.jooq.infrastructure;

import dev.dmitriirussu.graphextraction.jooq.application.OwnerReadRepository;
import dev.dmitriirussu.graphextraction.jooq.application.view.OwnerListView;
import dev.dmitriirussu.graphextraction.jooq.application.view.OwnerView;
import dev.dmitriirussu.graphextraction.jooq.application.view.PetView;
import dev.dmitriirussu.graphextraction.jooq.application.view.VisitView;
import org.jooq.DSLContext;
import org.jooq.Records;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.generated.tables.Owners.OWNERS;
import static com.example.generated.tables.Pets.PETS;
import static com.example.generated.tables.Visits.VISITS;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

@Repository
class JooqOwnerReadRepository implements OwnerReadRepository {

    private final DSLContext dsl;

    JooqOwnerReadRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<OwnerView> findByIdWithGraph(String id) {
        return dsl
                .select(
                        OWNERS.ID,
                        OWNERS.NAME,
                        multiset(
                                select(
                                        PETS.ID,
                                        PETS.NAME,
                                        PETS.OWNER_ID,
                                        multiset(
                                                select(VISITS.ID, VISITS.DATE, VISITS.PET_ID)
                                                        .from(VISITS)
                                                        .where(VISITS.PET_ID.eq(PETS.ID))
                                        ).convertFrom(r -> r.map(Records.mapping(VisitView::new)))
                                )
                                        .from(PETS)
                                        .where(PETS.OWNER_ID.eq(OWNERS.ID))
                        ).convertFrom(r -> r.map(Records.mapping(PetView::new)))
                )
                .from(OWNERS)
                .where(OWNERS.ID.eq(id))
                .fetchOptional(Records.mapping(OwnerView::new));
    }

    @Override
    public List<OwnerView> findAllWithGraph() {
        return dsl
                .select(
                        OWNERS.ID,
                        OWNERS.NAME,
                        multiset(
                                select(
                                        PETS.ID,
                                        PETS.NAME,
                                        PETS.OWNER_ID,
                                        multiset(
                                                select(VISITS.ID, VISITS.DATE, VISITS.PET_ID)
                                                        .from(VISITS)
                                                        .where(VISITS.PET_ID.eq(PETS.ID))
                                        ).convertFrom(r -> r.map(Records.mapping(VisitView::new)))
                                )
                                        .from(PETS)
                                        .where(PETS.OWNER_ID.eq(OWNERS.ID))
                        ).convertFrom(r -> r.map(Records.mapping(PetView::new)))
                )
                .from(OWNERS)
                .orderBy(OWNERS.ID)
                .fetch(Records.mapping(OwnerView::new));
    }

    @Override
    public List<OwnerListView> findAllFlat() {
        return dsl
                .select(
                        OWNERS.ID,
                        OWNERS.NAME,
                        multiset(
                                select(PETS.NAME)
                                        .from(PETS)
                                        .where(PETS.OWNER_ID.eq(OWNERS.ID))
                        ).convertFrom(r -> r.map(rec -> rec.get(PETS.NAME)))
                )
                .from(OWNERS)
                .orderBy(OWNERS.ID)
                .fetch(Records.mapping(OwnerListView::new));
    }
}
