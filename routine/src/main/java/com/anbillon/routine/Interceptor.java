/*
 * Copyright (C) 2017 Tourbillon Group
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

package com.anbillon.routine;

/**
 * Typically interceptors add, remove, or transform intent on router.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface Interceptor {

  Router intercept(Chain chain) throws RoutineException;

  interface Chain {

    Router router();

    Router proceed(Router router) throws RoutineException;
  }
}
