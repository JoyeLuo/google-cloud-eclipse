/*
 * Copyright 2017 Google Inc.
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

package com.google.cloud.tools.eclipse.appengine.deploy;

import com.google.cloud.tools.appengine.api.deploy.DefaultDeployConfiguration;
import org.junit.Assert;
import org.junit.Test;

public class DeployJobTest {

  @Test
  public void testGetDeployedAppUrl_internal() {
    DeployJob deployJob = createDeployJob(true);
    AppEngineDeployOutput deployOutput =
        createDeployOutput("google.com:notable-torch", "version", "default");

    Assert.assertEquals("https://notable-torch.googleplex.com",
        deployJob.getDeployedAppUrl(deployOutput));
  }

  @Test
  public void testGetDeployedAppUrl_withPartition() {
    DeployJob deployJob = createDeployJob(true);
    AppEngineDeployOutput deployOutput =
        createDeployOutput("s~google.com:notable-torch", "version", "default");

    Assert.assertEquals("https://notable-torch.googleplex.com",
        deployJob.getDeployedAppUrl(deployOutput));
  }

  @Test
  public void testGetDeployedAppUrl_promoteWithDefaultService() {
    DeployJob deployJob = createDeployJob(true);
    AppEngineDeployOutput deployOutput = createDeployOutput("testProject", "version", "default");

    Assert.assertEquals("https://testProject.appspot.com",
        deployJob.getDeployedAppUrl(deployOutput));
  }

  @Test
  public void testGetDeployedAppUrl_promoteWithNonDefaultService() {
    DeployJob deployJob = createDeployJob(true);
    AppEngineDeployOutput deployOutput = createDeployOutput("testProject", "version", "service");

    Assert.assertEquals("https://service-dot-testProject.appspot.com",
        deployJob.getDeployedAppUrl(deployOutput));
  }

  @Test
  public void testGetDeployedAppUrl_noPromoteWithDefaultService() {
    DeployJob deployJob = createDeployJob(false);
    AppEngineDeployOutput deployOutput = createDeployOutput("testProject", "version", "default");

    Assert.assertEquals("https://version-dot-testProject.appspot.com",
        deployJob.getDeployedAppUrl(deployOutput));
  }

  @Test
  public void testGetDeployedAppUrl_noPromoteWithNonDefaultService() {
    DeployJob deployJob = createDeployJob(false);
    AppEngineDeployOutput deployOutput = createDeployOutput("testProject", "version", "service");

    Assert.assertEquals("https://version-dot-service-dot-testProject.appspot.com",
        deployJob.getDeployedAppUrl(deployOutput));
  }

  private static DeployJob createDeployJob(boolean setPromote) {
    DefaultDeployConfiguration config = new DefaultDeployConfiguration();
    config.setPromote(setPromote);
    config.setProject("testProject");
    return new DeployJob(null, null, null, null, null, config, false);
  }

  private static AppEngineDeployOutput createDeployOutput(String project, String version,
      String service) {
    String jsonOutput =
        "{\n" +
            "  \"configs\": [],\n" +
            "  \"versions\": [\n" +
            "    {\n" +
            "      \"id\": \"" + version + "\",\n" +
            "      \"last_deployed_time\": null,\n" +
            "      \"project\": \"" + project + "\",\n" +
            "      \"service\": \"" + service + "\",\n" +
            "      \"traffic_split\": null,\n" +
            "      \"version\": null\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";
    return AppEngineDeployOutput.parse(jsonOutput);
  }
}