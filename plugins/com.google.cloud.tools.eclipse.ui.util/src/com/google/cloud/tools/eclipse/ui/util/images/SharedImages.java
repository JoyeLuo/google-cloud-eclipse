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

package com.google.cloud.tools.eclipse.ui.util.images;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class SharedImages {

  public static final ImageDescriptor CLOUDSDK_IMAGE_DESCRIPTOR =
      AbstractUIPlugin.imageDescriptorFromPlugin("com.google.cloud.tools.eclipse.ui.util",
          "icons/obj16/cloudsdk.png");

  public static final ImageDescriptor REFRESH_IMAGE_DESCRIPTOR =
      AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.debug.ui",
                                                 "icons/full/obj16/refresh_tab.png");
}
