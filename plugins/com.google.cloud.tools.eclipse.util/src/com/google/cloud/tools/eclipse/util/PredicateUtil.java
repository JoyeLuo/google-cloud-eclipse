/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.tools.eclipse.util;

import com.google.common.base.Predicate;
import java.util.Collection;

/**
 * Utility functions for Guava {@link Predicate}s.
 */
public class PredicateUtil {

  /** Returns a predicate that tests that there are exactly {@code count} elements. */
  public static <T> Predicate<Collection<T>> isOfSize(final int expectedSize) {
    return new Predicate<Collection<T>>() {
      @Override
      public boolean apply(Collection<T> errors) {
        return errors != null && errors.size() == expectedSize;
      }
    };
  }

  /**
   * Returns a predicate that applies the provided sub-predicate to each of the children, returning
   * true if the sub-predicate returns success for any element. If the collection is empty, returns
   * false.  This is a short-circuiting operation.
   */
  public static <T> Predicate<Collection<T>> any(final Predicate<? super T> subPredicate) {
    return new Predicate<Collection<T>>() {
      @Override
      public boolean apply(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
          return false;
        }
        for(T o : collection) {
          if(subPredicate.apply(o)) {
            return true;
          }
        }
        return false;
      }
    };
  }

  /**
   * Returns a predicate that applies the provided sub-predicate to each of the children, returning
   * true if the sub-predicate returns success for all elements. If the collection is empty, returns
   * true. This is a short-circuiting operation.
   */
  public static <T> Predicate<Collection<T>> all(final Predicate<? super T> subPredicate) {
    return new Predicate<Collection<T>>() {
      @Override
      public boolean apply(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
          return true;
        }
        for (T o : collection) {
          if (!subPredicate.apply(o)) {
            return false;
          }
        }
        return true;
      }
    };
  }

  private PredicateUtil() {}
}
