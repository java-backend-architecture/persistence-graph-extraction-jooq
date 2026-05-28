package dev.dmitriirussu.graphextraction.jooq;

import dev.dmitriirussu.graphextraction.jooq.application.OwnerReadRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GraphExtractionJooqApp {
	public static void main(String[] args) {
		SpringApplication.run(GraphExtractionJooqApp.class, args);
	}
	// Demo output for manual verification of graph extraction queries
	@Bean
	CommandLineRunner demo(OwnerReadRepository repository) {
		return args -> {
			System.out.println("\n=== All owners - full graph ===\n");
			repository.findAllWithGraph().forEach(System.out::println);

			System.out.println("\n=== All owners - flat projection ===\n");
			repository.findAllFlat().forEach(System.out::println);

			System.out.println("\n=== Find owner by id='1' ===\n");
			repository.findByIdWithGraph("1").ifPresent(System.out::println);
		};
	}
}

//mvn jooq-codegen:generate