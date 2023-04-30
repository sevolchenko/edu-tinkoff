package ru.tinkoff.edu.java.scrapper.model.jooq.codegen;

import org.jooq.codegen.GenerationTool;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.meta.jaxb.*;

public class JooqCodegen {

    private static final String MIGRATIONS_DIR = "./scrapper/migrations";
    private static final String GENERATED_CODE_PACKAGE = "ru.tinkoff.edu.java.scrapper.model.jooq.generated";
    private static final String SCRAPPER_SOURCE_ROOT = "scrapper/src/main/java";

    public static void main(String[] args) throws Exception {
        Database database = new Database()
                .withName("org.jooq.meta.extensions.liquibase.LiquibaseDatabase")
                .withProperties(
                        new Property().withKey("rootPath").withValue(MIGRATIONS_DIR),
                        new Property().withKey("scripts").withValue("master.xml"),
                        new Property().withKey("includeLiquibaseTables").withValue("false")
                );

        Generate options = new Generate()
                .withGeneratedAnnotation(true)
                .withGeneratedAnnotationDate(false)
                .withNullableAnnotation(true)
                .withNullableAnnotationType("org.jetbrains.annotations.Nullable")
                .withNonnullAnnotation(true)
                .withNonnullAnnotationType("org.jetbrains.annotations.NotNull")
                .withJpaAnnotations(false)
                .withValidationAnnotations(true)
                .withSpringAnnotations(true)
                .withConstructorPropertiesAnnotation(true)
                .withConstructorPropertiesAnnotationOnPojos(true)
                .withConstructorPropertiesAnnotationOnRecords(true)
                .withFluentSetters(false)
                .withDaos(false)
                .withPojos(true);

        Target target = new Target()
                .withPackageName(GENERATED_CODE_PACKAGE)
                .withDirectory(SCRAPPER_SOURCE_ROOT);

        Configuration configuration = new Configuration()
                .withGenerator(
                        new Generator()
                                .withDatabase(database)
                                .withGenerate(options)
                                .withTarget(target)
                );

        GenerationTool.generate(configuration);
    }
}