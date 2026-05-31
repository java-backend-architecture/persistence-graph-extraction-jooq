# persistence-graph-extraction-jooq

Demonstrates how to assemble an object graph from a relational database using jOOQ (`DSLContext`) and `MULTISET` without ORM or pagination.

## What it shows

* Assembling a three-level object graph (`Owner → Pet → Visit`) using jOOQ `MULTISET` nesting
* Using `package-private` class as an encapsulation boundary inside the infrastructure layer
* Two query strategies:
  * full graph — Owner with Pets and Visits (`OwnerView`)
  * flat list — Owner with pet names only (`OwnerListView`)

## Stack

* Java 25
* Spring Boot
* jOOQ (`DSLContext`)
* H2 (in-memory database)

## Structure

```
application/
    OwnerReadRepository       # port (interface)
    view/                     # read models: OwnerView, OwnerListView, PetView, VisitView

infrastructure/
    JooqOwnerReadRepository   # jOOQ implementation (package-private)
```

## Key idea

jOOQ `MULTISET` assembles the object graph directly in SQL — no flat rows, no deduplication, no intermediate projection classes:

```
Owner(jack)
  └── Pet(buddy1)
        ├── Visit(2026-01-10)
        └── Visit(2026-02-15)
  └── Pet(buddy2)
        └── Visit(2026-03-01)
```

A single DSL query with nested `MULTISET` expressions returns the fully structured result, mapped directly into view records via `Records.mapping()`.

Compare with the JDBC and JPA + Blaze approaches:

|                       | JDBC                                      | jOOQ                  | JPA + Blaze                        |
|-----------------------|-------------------------------------------|-----------------------|------------------------------------|
| SQL result            | flat rows (cartesian JOIN)                | nested collections    | optimized per-view query           |
| Deduplication         | `LinkedHashMap` in projections            | not needed            | not needed                         |
| Intermediate classes  | `OwnerProjection`, `PetProjection`, etc.  | none                  | `@EntityView` interfaces           |
| Mapping               | `ViewMapper`                              | `Records.mapping()`   | `ViewMapper`                       |
| N+1 problem           | no (single JOIN query)                    | no (single query)     | no (Blaze optimizes automatically) |

No ORM, no JSON parsing — just jOOQ and a single query.

`JooqOwnerReadRepository` is `package-private` by design; only `OwnerReadRepository` (the interface) is exposed outside the infrastructure package.

## Tests

Integration tests in `src/test/java` cover graph assembly, flat list extraction, and edge cases.

## Related

* [persistence-graph-extraction-jdbc](https://github.com/java-backend-architecture/persistence-graph-extraction-jdbc) — same approach with plain JDBC
* [persistence-graph-extraction-jpa](https://github.com/java-backend-architecture/persistence-graph-extraction-jpa) — same approach with JPA + Blaze Persistence
* [persistence-graph-pagination-jdbc](https://github.com/java-backend-architecture/persistence-graph-pagination-jdbc) — same approach with pagination support

## Run

```bash
./mvnw spring-boot:run
```
