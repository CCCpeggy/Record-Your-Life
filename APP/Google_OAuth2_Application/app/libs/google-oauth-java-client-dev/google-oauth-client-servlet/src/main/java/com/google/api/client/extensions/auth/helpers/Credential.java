/*
 * Copyright (c) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.extensions.auth.helpers;

import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.util.Beta;

import javax.jdo.annotations.PersistenceCapable;

/**
 * {@link Beta} <br/>
 * Implementations of this class will have all of the information necessary to create and install
 * the necessary handlers on an {@link HttpTransport} object to allow for a streamlined auth
 * experience.
 *
 * Implementations of this interface should use the {@link PersistenceCapable} annotation to allow
 * credentials to be managed on behalf of the application.
 *
 * <p>
 * Warning: starting with version 1.7, usage of this for OAuth 2.0 is deprecated. Instead use
 * {@link com.google.api.client.auth.oauth2.Credential}.
 * </p>
 *
 * @author moshenko@google.com (Jacob Moshenko)
 * @since 1.4
 */
@Beta
public interface Credential
    extends HttpRequestInitializer, HttpExecuteInterceptor, HttpUnsuccessfulResponseHandler {

  /**
   * Determine if the Credential is no longer valid, after being revoked for example.
   *
   * @since 1.5
   */
  boolean isInvalid();
}
