/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.tools.eclipse.util;

import com.google.cloud.tools.eclipse.util.status.StatusUtil;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;

public class Templates {
  public static final String APPENGINE_WEB_XML_TEMPLATE = "appengine-web.xml.ftl";
  public static final String HELLO_APPENGINE_TEMPLATE = "HelloAppEngine.java.ftl";
  public static final String INDEX_HTML_TEMPLATE = "index.html.ftl";
  public static final String WEB_XML_TEMPLATE = "web.xml.ftl";
  public static final String HELLO_APPENGINE_TEST_TEMPLATE = "HelloAppEngineTest.java.ftl";
  public static final String MOCK_HTTPSERVLETRESPONSE_TEMPLATE = "MockHttpServletResponse.java.ftl";
  public static final String APP_YAML_TEMPLATE = "app.yaml.ftl";
  public static final String POM_XML_STANDARD_TEMPLATE = "pom.xml.standard.ftl";
  public static final String POM_XML_FLEX_TEMPLATE = "pom.xml.flex.ftl";

  private static Configuration configuration;

  public static void createFileContent(
      String outputFileLocation, String templateName, Map<String, String> dataMap)
      throws CoreException {
    Preconditions.checkNotNull(outputFileLocation, "output file is null");
    Preconditions.checkNotNull(templateName, "template name is null");
    Preconditions.checkNotNull(dataMap, "data map is null");

    if (configuration == null) {
      configuration = createConfiguration();
    }
    Path outputFile = Paths.get(outputFileLocation);
    try (Writer writer =
        new OutputStreamWriter(Files.newOutputStream(outputFile), StandardCharsets.UTF_8)) {
      Template template = configuration.getTemplate(templateName);
      template.process(dataMap, writer);
    } catch (IOException | TemplateException ex) {
      throw new CoreException(StatusUtil.error(Templates.class, ex.getMessage()));
    }
  }

  public static void copyFileContent(String outputFileLocation, String sourceName)
      throws CoreException {
    Preconditions.checkNotNull(outputFileLocation, "output file is null");
    Preconditions.checkNotNull(sourceName, "source file name is null");

    try (
        InputStream inputStream = Templates.class
            .getResourceAsStream("/templates/appengine/" + sourceName);
        OutputStream outputStream = Files.newOutputStream(Paths.get(outputFileLocation))) {
      ByteStreams.copy(inputStream, outputStream);
    } catch (IOException ex) {
      throw new CoreException(StatusUtil.error(Templates.class, ex.getMessage()));
    }
  }

  private Templates() {
  }

  private static Configuration createConfiguration() {
    Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);
    configuration.setClassForTemplateLoading(
        Templates.class, "/templates/appengine");
    configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    configuration.setLogTemplateExceptions(false);
    return configuration;
  }

}
