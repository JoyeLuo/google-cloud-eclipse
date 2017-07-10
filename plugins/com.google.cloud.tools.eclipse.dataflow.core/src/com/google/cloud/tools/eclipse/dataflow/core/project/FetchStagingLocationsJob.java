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
import com.google.cloud.tools.eclipse.googleapis.GoogleApiException;
import com.google.cloud.tools.eclipse.googleapis.IGoogleApiFactory;
import com.google.common.util.concurrent.SettableFuture;
import java.io.IOException;
import java.util.SortedSet;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * A job that retrieves a collection of potential Staging Locations from a {@link
 * GcsDataflowProjectClient}.
 */
public class FetchStagingLocationsJob extends Job {
  private final Credential credential;
  private final String cloudProject;
  private final SettableFuture<SortedSet<String>> stagingLocations;
  private final IGoogleApiFactory apiFactory;

  private FetchStagingLocationsJob(Credential credential, String cloudProject,
      IGoogleApiFactory apiFactory) {
    super("Update Status Locations for project " + cloudProject);
    this.credential = credential;
    this.cloudProject = cloudProject;
    this.apiFactory = apiFactory;
    this.stagingLocations = SettableFuture.create();
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    try {
      GcsDataflowProjectClient gcsClient = GcsDataflowProjectClient.create(apiFactory, credential);
      stagingLocations.set(gcsClient.getPotentialStagingLocations(cloudProject));
    } catch (IOException | GoogleApiException ex) {
      stagingLocations.setException(ex);
    }
    return Status.OK_STATUS;
  }

  /**
   * Creates a new {@link FetchStagingLocationsJob} for the specified project using the specified
   * client, schedules the job, and returns a future containing the results of the job.
   */
  public static ListenableFutureProxy<SortedSet<String>> schedule(
      Credential credential, String project, IGoogleApiFactory apiFactory) {
    FetchStagingLocationsJob job = new FetchStagingLocationsJob(credential, project, apiFactory);
    job.schedule();
    return new ListenableFutureProxy<>(job.stagingLocations);
  }
}
