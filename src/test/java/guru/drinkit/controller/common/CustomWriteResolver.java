package guru.drinkit.controller.common;

/**
 * @author pkolmykov
 */
/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.snippet.WriterResolver;

/**
 * COPY-PASTED from {@link org.springframework.restdocs.snippet.StandardWriterResolver}
 * To write into single file
 * <p>
 * Standard implementation of {@link WriterResolver}.
 *
 * @author Andy Wilkinson
 */
public final class CustomWriteResolver implements WriterResolver {

    private String encoding = "UTF-8";

    private boolean init = true;

    @Override
    public Writer resolve(String operationName, String snippetName,
                          RestDocumentationContext context) throws IOException {
        File outputFile = resolveFile(context.getOutputDirectory().getAbsolutePath(), "api-snippets.adoc", context);

        createDirectoriesIfNecessary(outputFile);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile, !init), this.encoding);
        if (init) {
            writer.append("= Auto-Generated API snippets\n" +
                    ":doctype: book\n" +
                    ":icons: font\n" +
                    ":source-highlighter: highlightjs\n" +
                    ":toc: left\n" +
                    ":toclevels: 4\n" +
                    ":sectlinks:\n\n"
            );
            init = false;
        } else {
            writer.append("\n");
        }
        writer
                .append("==== ")
                .append(context.getTestClass().getSimpleName())
                .append(".")
                .append(context.getTestMethodName())
                .append(":")
                .append(snippetName).append("\n");
        return writer;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    File resolveFile(String outputDirectory, String fileName,
                     RestDocumentationContext context) {
        File outputFile = new File(outputDirectory, fileName);
        if (!outputFile.isAbsolute()) {
            outputFile = makeRelativeToConfiguredOutputDir(outputFile, context);
        }
        return outputFile;
    }

    private File makeRelativeToConfiguredOutputDir(File outputFile,
                                                   RestDocumentationContext context) {
        File configuredOutputDir = context.getOutputDirectory();
        if (configuredOutputDir != null) {
            return new File(configuredOutputDir, outputFile.getPath());
        }
        return null;
    }

    private void createDirectoriesIfNecessary(File outputFile) {
        File parent = outputFile.getParentFile();
        if (!parent.isDirectory() && !parent.mkdirs()) {
            throw new IllegalStateException("Failed to create directory '" + parent + "'");
        }
    }
}
