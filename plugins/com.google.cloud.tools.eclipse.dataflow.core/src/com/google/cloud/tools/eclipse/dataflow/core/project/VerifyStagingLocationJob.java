/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.tools.eclipse.dataflow.core.project;

import com.google.api.client.auth.oauth2.Credential;
import com.google.cloud.tools.eclipse.dataflow.core.proxy.ListenableFutureProxy;
import com.google.cloud.tools.eclipse.googleapis.IGoogleApiFactory;
import com.google.common.util.concurrent.SettableFuture;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * A job that verifies a Staging Location.
 */
public class VerifyStagingLocationJob extends Job {
  private final String email;
  private final Credential credential;
  private final String stagingLocation;
  private final SettableFuture<VerifyStagingLocationResult> future;
  private final IGoogleApiFactory apiFactory;

  public static VerifyStagingLocationJob create(
      String email, Credential credential, String stagingLocation, IGoogleApiFactory apiFactory) {
    return new VerifyStagingLocationJob(email, credential, stagingLocation, apiFactory);
  }

  private VerifyStagingLocationJob(String email, Credential credential, String stagingLocation,
      IGoogleApiFactory apiFactory) {
    super("Verify Staging Location " + stagingLocation);
    this.email = email;
    this.credential = credential;
    this.stagingLocation = stagingLocation;
    this.future = SettableFuture.create();
    this.apiFactory = apiFactory;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    GcsDataflowProjectClient gcsClient = GcsDataflowProjectClient.create(apiFactory, credential);
    VerifyStagingLocationResult result = new VerifyStagingLocationResult(
        email, stagingLocation, gcsClient.locationIsAccessible(stagingLocation));
    future.set(result);
    return Status.OK_STATUS;
  }

  public ListenableFutureProxy<VerifyStagingLocationResult> getVerifyResult() {
    return new ListenableFutureProxy<>(future);
  }

  /**
   * The result of verifying a staging location, containing the staging location, the account email,
   * and the verification result.
   */
  public static class VerifyStagingLocationResult {
    public final String email;
    public final String stagingLocation;
    public final boolean accessible;

    public VerifyStagingLocationResult(String email, String stagingLocation, boolean accessible) {
      this.email = email;
      this.stagingLocation = stagingLocation;
      this.accessible = accessible;
    }
  }
}
