package com.orvix.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

/**
 * Extracts lightweight structural facts from Java source using JavaParser. Tolerant: a file that
 * fails to parse yields {@link ParsedJava#empty()} rather than throwing, so a single odd file
 * never breaks a review.
 */
@Component
public class JavaSourceParser {

    private final JavaParser parser =
            new JavaParser(new ParserConfiguration().setLanguageLevel(LanguageLevel.JAVA_21));

    public ParsedJava parse(String source) {
        if (source == null || source.isBlank()) {
            return ParsedJava.empty();
        }
        try {
            ParseResult<CompilationUnit> result = parser.parse(source);
            Optional<CompilationUnit> parsed = result.getResult();
            if (parsed.isEmpty()) {
                return ParsedJava.empty();
            }
            return extract(parsed.get());
        } catch (RuntimeException ex) {
            return ParsedJava.empty();
        }
    }

    private ParsedJava extract(CompilationUnit cu) {
        String packageName = cu.getPackageDeclaration()
                .map(pd -> pd.getNameAsString())
                .orElse("");

        List<String> typeNames = new ArrayList<>();
        for (TypeDeclaration<?> type : cu.getTypes()) {
            typeNames.add(type.getNameAsString());
        }

        List<String> imports = new ArrayList<>();
        cu.getImports().forEach(imp -> {
            if (!imp.isAsterisk() && !imp.isStatic()) {
                imports.add(imp.getNameAsString());
            }
        });

        List<String> supertypes = new ArrayList<>();
        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(decl -> {
            decl.getExtendedTypes().forEach(t -> supertypes.add(t.getNameAsString()));
            decl.getImplementedTypes().forEach(t -> supertypes.add(t.getNameAsString()));
        });

        List<String> methodNames = new ArrayList<>();
        cu.findAll(MethodDeclaration.class).forEach(m -> methodNames.add(m.getNameAsString()));

        return new ParsedJava(packageName, typeNames, imports, supertypes, methodNames);
    }
}
