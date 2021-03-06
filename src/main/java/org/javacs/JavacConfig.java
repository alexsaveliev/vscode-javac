package org.javacs;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public class JavacConfig {
    public final Collection<Path> sourcePath, classPath;
    public final Path outputDirectory;

    public JavacConfig(Collection<Path> sourcePath, Collection<Path> classPath, Path outputDirectory) {
        this.sourcePath = sourcePath;
        this.classPath = classPath;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavacConfig that = (JavacConfig) o;
        return Objects.equals(sourcePath, that.sourcePath) &&
               Objects.equals(classPath, that.classPath) &&
               Objects.equals(outputDirectory, that.outputDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourcePath, classPath, outputDirectory);
    }
}
